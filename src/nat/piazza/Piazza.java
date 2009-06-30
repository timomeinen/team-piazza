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
import jetbrains.buildServer.serverSide.MainConfigProcessor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import jetbrains.buildServer.web.openapi.WebResourcesManager;
import org.jdom.Element;


public class Piazza implements MainConfigProcessor {
	public static final String PLUGIN_NAME = Piazza.class.getSimpleName().toLowerCase();
	
	private final WebResourcesManager webResourcesManager;
	private final String version;
    
	private UserGroup userGroup = new UserGroup();

    public Piazza(SBuildServer server,
				  WebControllerManager webControllerManager,
				  WebResourcesManager webResourcesManager) 
	{
		this.webResourcesManager = webResourcesManager;
		this.version = loadVersionFromResource();

        server.registerExtension(MainConfigProcessor.class, "piazza", this);

        webResourcesManager.addPluginResources(PLUGIN_NAME, PLUGIN_NAME + ".jar");
        webControllerManager.registerController(
			"/" + PLUGIN_NAME + "/*/*",
			new BuildMonitorController(server, this));
	}
	
	public String resourcePath(String resourceName) {
		return webResourcesManager.resourcePath(PLUGIN_NAME, resourceName);
	}
	
	public String version() {
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

    public void readFrom(Element serverConfigRoot) {
        System.out.println("PIAZZA: read from! elementName = " + serverConfigRoot.getQualifiedName());
        Element piazzaConfigRoot = serverConfigRoot.getChild("piazza");
        if (piazzaConfigRoot == null) {
            this.userGroup = UserGroup.loadFrom(piazzaConfigRoot);
        }
        else {
            this.userGroup = new UserGroup();
        }
    }
    
    public void writeTo(Element serverConfigRoot) {
        System.out.println("PIAZZA: write to! elementName = " + serverConfigRoot.getQualifiedName());
        Element piazzaConfigRoot = new Element("piazza");
        userGroup.writeTo(piazzaConfigRoot);
        serverConfigRoot.addContent(piazzaConfigRoot);
    }
    
    public UserGroup userGroup() {
        return userGroup;
    }
}
