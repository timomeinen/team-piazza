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
package com.natpryce.piazza.pluginConfiguration;

import com.natpryce.piazza.Piazza;
import jetbrains.buildServer.controllers.admin.AdminPage;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author tmeinen
 */
public class PiazzaConfigurationPageExtension extends AdminPage {

	private static final String TAB_TITLE = "Piazza Notifier";

	private PiazzaConfiguration piazzaConfiguration;
    private Piazza piazza;

    public PiazzaConfigurationPageExtension(@NotNull final WebControllerManager manager,
											@NotNull PiazzaConfiguration piazzaConfiguration,
                                            @NotNull Piazza piazza) {
		super(manager);

		this.piazza = piazza;
        this.piazzaConfiguration = piazzaConfiguration;

		setIncludeUrl("settings.jsp");
		setTabTitle(TAB_TITLE);
		setPluginName(Piazza.PLUGIN_NAME);

		register();
		Loggers.SERVER.info(Piazza.PLUGIN_NAME + ": AdminPage registered");
	}

	@Override
	public void fillModel(@NotNull Map<String, Object> model, @NotNull HttpServletRequest request) {
		super.fillModel(model, request);
        model.put("showOnFailureOnly", piazzaConfiguration.isShowOnFailureOnly());
        model.put("resourceRoot", this.piazza.resourcePath(""));
    }

	@NotNull
	@Override
	public String getGroup() {
		return SERVER_RELATED_GROUP;
	}
}
