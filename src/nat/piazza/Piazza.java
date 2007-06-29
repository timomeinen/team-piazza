/*
 *  Copyright (C) 2007 Nat Pryce.
 *  
 *  This file is part of Team Piazza.
 *  
 *  Team Piazza is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Team Piazza is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nat.piazza;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SProject;
import jetbrains.buildServer.web.openapi.Privileges;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import jetbrains.buildServer.web.openapi.WebResourcesManager;

import org.springframework.web.servlet.ModelAndView;


public class Piazza extends BaseController {
	private static final String PLUGIN_NAME = Piazza.class.getSimpleName().toLowerCase();
	
	private final WebResourcesManager webResourcesManager;
	
	
	public Piazza(SBuildServer server, 
				  WebControllerManager webControllerManager,
				  WebResourcesManager webResourcesManager) 
	{
		super(server);
		this.webResourcesManager = webResourcesManager;
		
		webControllerManager.registerController("/" + PLUGIN_NAME + "/*/*", this, Privileges.NONE);
		webResourcesManager.addPluginResources(PLUGIN_NAME, PLUGIN_NAME + ".jar");
	}
	
	protected ModelAndView doHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] path = request.getServletPath().split("/");
		if (path.length < 3) {
			throw new IllegalArgumentException("project and build missing from path");
		}
		
		String projectName = path[path.length-2];
		String buildTypeName = Text.withoutExtension(path[path.length-1]);
				
		SProject project = myServer.getProjectManager().findProjectByName(projectName);
		if (project == null) {
			throw new IllegalArgumentException("no project named " + projectName);
		}
		
		SBuildType buildType = project.findBuildTypeByName(buildTypeName);
		if (buildType == null) {
			throw new IllegalArgumentException("no build type named " + buildTypeName + " in project " + projectName);			
		}
		
		BuildViewState buildViewState = new BuildViewState(myServer, buildType, loadUserPictures(project));
		
		String viewJspPath = pathOfResource(PLUGIN_NAME + ".jsp");
		ModelAndView view = new ModelAndView(viewJspPath);
		view.addObject("build", buildViewState);
		view.addObject("buildType", buildType);
		view.addObject("project", project);
		view.addObject("resourceRoot", pathOfResource(""));
		return view;
	}
	
	private UserPictures loadUserPictures(SProject project) {
		UserPictures userPictures = new UserPictures();
		File configFile = new File(project.getConfigDirectory(), "portraits.cfg");
		
		if (configFile.exists()) {
			loadUserPicturesFromFile(userPictures, configFile);
		}
		
		return userPictures;
	}

	private void loadUserPicturesFromFile(UserPictures userPictures, File configFile) {
		try {
			userPictures.loadFrom(configFile);
		}
		catch (IOException e) {
			Loggers.SERVER.debug("Piazza plug-in could not load user portraits", e);
		}
	}
	
	private String pathOfResource(String resourceName) {
		return webResourcesManager.resourcePath(PLUGIN_NAME, resourceName);
	}
}
