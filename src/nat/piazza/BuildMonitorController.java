/*
   Copyright (c) 2007-2009 Nat Pryce.

   This file is part of Team Piazza.

   Team Piazza is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 3 of the License, or
   (at your option) any later version.

   Team Piazza is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nat.piazza;

import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SProject;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BuildMonitorController extends BaseController {
	private final Piazza piazza;
	
	public BuildMonitorController(SBuildServer server, Piazza piazza) {
		super(server);
		this.piazza = piazza;
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
		
		BuildMonitorViewState buildViewState = new BuildMonitorViewState(
            piazza.version(),
            myServer,
            buildType,
            piazza.userGroup());
		
		String viewJspPath = piazza.resourcePath("piazza.jsp");
		ModelAndView view = new ModelAndView(viewJspPath);
		view.addObject("build", buildViewState);
		view.addObject("buildType", buildType);
		view.addObject("project", project);
		view.addObject("resourceRoot", piazza.resourcePath(""));
		return view;
	}
}
