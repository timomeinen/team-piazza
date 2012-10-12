/*
 * Copyright (c) 2012 Nat Pryce, Timo Meinen, Frank Bregulla.
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
package com.natpryce.piazza.projectConfiguration;

import com.natpryce.piazza.Piazza;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.ProjectManager;
import jetbrains.buildServer.serverSide.settings.ProjectSettingsManager;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author tmeinen, fbregulla
 */
public class PiazzaProjectConfigurationPageExtension extends EditProjectTab {

    private static final String TAB_TITLE = "Piazza Notifier";

    private ProjectSettingsManager projectSettingsManager;
    private Piazza piazza;

    public PiazzaProjectConfigurationPageExtension(@NotNull final WebControllerManager manager,
                                                   @NotNull final ProjectManager projectManager,
                                                   @NotNull ProjectSettingsManager projectSettingsManager,
                                                   @NotNull Piazza piazza) {
        super(manager, Piazza.PLUGIN_NAME, "project-settings.jsp", TAB_TITLE, projectManager);
        this.projectSettingsManager = projectSettingsManager;
        this.piazza = piazza;

        Loggers.SERVER.info(Piazza.PLUGIN_NAME + ": EditProjectPage for TeamPiazza registered");
    }

    @Override
    public void fillModel(@NotNull Map<String, Object> model, @NotNull HttpServletRequest request) {
        super.fillModel(model, request);
        PiazzaProjectSettings projectSettings = (PiazzaProjectSettings) projectSettingsManager.getSettings(request.getParameter("projectId"), PiazzaProjectSettings.PROJECT_SETTINGS_NAME);
        model.put("showFeatureBranches", projectSettings.isShowFeatureBranches());
        model.put("maxNumberOfFeatureBranches", projectSettings.getMaxNumberOfFeatureBranchesToShow());
        model.put("maxAgeInDaysOfFeatureBranches", projectSettings.getMaxAgeInDaysOfFeatureBranches());
        model.put("resourceRoot", this.piazza.resourcePath(""));
        model.put("projectId", request.getParameter("projectId"));
    }

}
