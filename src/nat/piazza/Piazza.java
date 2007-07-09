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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.web.openapi.Privileges;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import jetbrains.buildServer.web.openapi.WebResourcesManager;


public class Piazza {
	public static final String PLUGIN_NAME = Piazza.class.getSimpleName().toLowerCase();
	
	private final WebResourcesManager webResourcesManager;
	private final String version;
	
	
	public Piazza(SBuildServer server, 
				  WebControllerManager webControllerManager,
				  WebResourcesManager webResourcesManager) 
	{
		this.webResourcesManager = webResourcesManager;
		this.version = loadVersionFromResource();
		
		webResourcesManager.addPluginResources(PLUGIN_NAME, PLUGIN_NAME + ".jar");
		webControllerManager.registerController(
			"/" + PLUGIN_NAME + "/*/*", 
			new BuildMonitorController(server, this), 
			Privileges.NONE);
	}
	
	public String resourcePath(String resourceName) {
		return webResourcesManager.resourcePath(PLUGIN_NAME, resourceName);
	}
	
	public String getVersion() {
		return version;
	}
	
	private String loadVersionFromResource() {
		Properties properties = new Properties();
		
		InputStream input = getClass().getResourceAsStream("/version.properties");
		try {
			try {
				properties.load(input);
			}
			finally {
				input.close();
			}
		}
		catch (IOException e) {
			throw new RuntimeException("version information incorrectly configured");
		}
		
		return properties.getProperty("piazza.version");
	}
}
