/*
 * Copyright (c) 2011 Nat Pryce, Timo Meinen.
 *
 * This file is part of Team Piazza.
 *
 * Team Piazza is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Team Piazza is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.natpryce.piazza;

import jetbrains.buildServer.serverSide.ProjectManager;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.users.UserModel;
import jetbrains.buildServer.web.openapi.PlaceId;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;

public class Piazza {

	public static final String PLUGIN_NAME = Piazza.class.getSimpleName().toLowerCase();
	public static final String PATH = "/" + PLUGIN_NAME + ".html";

	private final PluginDescriptor pluginDescriptor;
	private final PiazzaUserAdapter piazzaUserAdapter;

	public Piazza (SBuildServer server, ProjectManager projectManager, WebControllerManager webControllerManager, PluginDescriptor pluginDescriptor, UserModel userManager) {
		this.pluginDescriptor = pluginDescriptor;
		this.piazzaUserAdapter = new PiazzaUserAdapter(server, userManager);

		webControllerManager.registerController(PATH, new BuildMonitorController(server, projectManager, this));

		webControllerManager.getPlaceById(PlaceId.ALL_PAGES_FOOTER).addExtension(new PiazzaLinkPageExtension(this));
	}

	public String resourcePath (String resourceName) {
		return this.pluginDescriptor.getPluginResourcesPath(resourceName);
	}

	public String version () {
		return this.pluginDescriptor.getPluginVersion();
	}

	public UserGroup userGroup () {
		return this.piazzaUserAdapter.getUserGroup();
	}
}
