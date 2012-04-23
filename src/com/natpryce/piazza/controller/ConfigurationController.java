/*
 * Copyright (c) 2012 Nat Pryce, Timo Meinen.
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
package com.natpryce.piazza.controller;

import com.natpryce.piazza.Piazza;
import com.natpryce.piazza.PiazzaConfiguration;
import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tmeinen
 */
public class ConfigurationController extends BaseController {
	private final PiazzaConfiguration piazzaConfiguration;
	private final Piazza piazza;

	public ConfigurationController(SBuildServer server, WebControllerManager manager,
								   PiazzaConfiguration piazzaConfiguration, Piazza piazza) {
		super(server);
		manager.registerController("/configurePiazza.html", this);
		this.piazza = piazza;
		this.piazzaConfiguration = piazzaConfiguration;
	}

	@Override
	protected ModelAndView doHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean showOnFailureOnly = getShowOnFailureOnlyValueFromView(request);
		this.piazzaConfiguration.setShowOnFailureOnly(showOnFailureOnly);
		return new ModelAndView(piazza.resourcePath("piazza-settings.jsp"))
				.addObject("version", piazza.version())
				.addObject("resourceRoot", piazza.resourcePath(""));
	}

	private boolean getShowOnFailureOnlyValueFromView(HttpServletRequest request) {
		return request.getParameter("showOnFailureOnly") != null;
	}
}
