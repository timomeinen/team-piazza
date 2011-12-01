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
package com.natpryce.piazza.extension;

import com.natpryce.piazza.Piazza;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PlaceId;
import jetbrains.buildServer.web.openapi.SimpleCustomTab;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tmeinen
 */
public class PiazzaConfigurationPageExtension extends SimpleCustomTab {

	private static final String TAB_TITLE = "Piazza Notifier";

	public PiazzaConfigurationPageExtension(PagePlaces pagePlaces) {
        super(pagePlaces);
        setIncludeUrl("piazza-settings.jsp");
        setPlaceId(PlaceId.ADMIN_SERVER_CONFIGURATION_TAB);
        setPluginName(Piazza.PLUGIN_NAME);
        register();
    }

	@NotNull
	public String getTabTitle() {
		return TAB_TITLE;
	}

	public boolean isAvailable(HttpServletRequest request) {
        return super.isAvailable(request);
    }

	public boolean isVisible() {
		return true;
	}
}
