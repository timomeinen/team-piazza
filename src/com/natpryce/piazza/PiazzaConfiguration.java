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

import org.jdom.Element;

import jetbrains.buildServer.serverSide.MainConfigProcessor;
import jetbrains.buildServer.serverSide.SBuildServer;

/**
 * @author tmeinen
 */
public class PiazzaConfiguration implements MainConfigProcessor {

	private static final String ATTRIBUTE_SHOW_ON_FAILURE_ONLY = "showOnFailureOnly";

	private final SBuildServer server;

	private boolean showOnFailureOnly;

	public PiazzaConfiguration(SBuildServer server) {
		this.server = server;
	}

	public boolean isShowOnFailureOnly() {
		return showOnFailureOnly;
	}

	public void setShowOnFailureOnly(boolean showOnFailureOnly) {
		this.showOnFailureOnly = showOnFailureOnly;
	}

	public void readFrom(Element serverConfigRoot) {
		Element piazzaConfigRoot = serverConfigRoot.getChild("piazza");
		if (piazzaConfigRoot != null) {
			String showOnFailureOnlyFromConfig = piazzaConfigRoot.getAttributeValue(ATTRIBUTE_SHOW_ON_FAILURE_ONLY);
			showOnFailureOnly = Boolean.valueOf(showOnFailureOnlyFromConfig);
		}
	}

	public void writeTo(Element serverConfigRoot) {
		Element piazzaConfigRoot = new Element("piazza");
		piazzaConfigRoot.setAttribute(ATTRIBUTE_SHOW_ON_FAILURE_ONLY, String.valueOf(showOnFailureOnly));
		serverConfigRoot.addContent(piazzaConfigRoot);
	}

	public void register() {
		server.registerExtension(MainConfigProcessor.class, Piazza.PLUGIN_NAME, this);
	}

}
