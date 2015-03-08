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

import com.natpryce.piazza.pluginConfiguration.PiazzaConfiguration;
import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.ProjectManager;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SProject;
import jetbrains.buildServer.serverSide.settings.ProjectSettingsManager;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

/**
 * @author tmeinen, fbregulla
 */
public class ProjectConfigurationController extends BaseController {

    private ProjectSettingsManager projectSettingsManager;
    private ProjectManager projectManager;

    public ProjectConfigurationController(SBuildServer server, WebControllerManager manager, @NotNull ProjectSettingsManager projectSettingsManager, @NotNull ProjectManager projectManager) {
        super(server);
        this.projectSettingsManager = projectSettingsManager;
        this.projectManager = projectManager;
        manager.registerController("/configurePiazzaProject.html", this);
    }

    @Override
    protected ModelAndView doHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws Exception {
        SProject project = getProject(request);
        if (project != null) {
			PiazzaProjectSettings projectSettings = (PiazzaProjectSettings) projectSettingsManager.getSettings(project.getProjectId(), PiazzaProjectSettings.PROJECT_SETTINGS_NAME);

			projectSettings.setShowFeatureBranches(getShowFeatureBranchesValueFromView(request));
			projectSettings.setMaxNumberOfFeatureBranchesToShow(getMaxNumberOfFeatureBranchesFromView(request));
			projectSettings.setMaxAgeInDaysOfFeatureBranches(getMaxAgeInDaysOfFeatureBranchesFromView(request));

			updateConfiguration(request, project);
		} else {
	        addPiazzaMessage(request, "Save failed: project not found");
		}
        return null;
    }

    private void updateConfiguration(HttpServletRequest request, SProject project) {
        try {
			project.persist();
			addSuccessMessage(request);
        } catch (PiazzaConfiguration.SaveConfigFailedException e) {
            Loggers.SERVER.error(e);
            addPiazzaMessage(request, "Save failed: " + e.getLocalizedMessage());
        }
    }

	private SProject getProject(HttpServletRequest request) {
		return projectManager.findProjectByExternalId(request.getParameter("projectId"));
	}

    private boolean getShowFeatureBranchesValueFromView(HttpServletRequest request) {
        return getBooleanParameter(request, "showFeatureBranches");
    }

    private boolean getBooleanParameter(HttpServletRequest request, String parameterName) {
        String parameter = request.getParameter(parameterName);
        if (parameter == null)
            return false;
        return Boolean.valueOf(parameter);
    }

    private int getMaxNumberOfFeatureBranchesFromView(HttpServletRequest request) {
        return getIntParameter(request, "maxNumberOfFeatureBranches");
    }

    private int getMaxAgeInDaysOfFeatureBranchesFromView(HttpServletRequest request) {
        return getIntParameter(request, "maxAgeInDaysOfFeatureBranches");
    }

    private int getIntParameter(HttpServletRequest request, String parameterName) {
        String parameter = request.getParameter(parameterName);
        if (parameter == null) {
            return 0;
        }
        return Integer.parseInt(parameter);
    }

    private void addSuccessMessage(HttpServletRequest request) {
        addPiazzaMessage(request, "Saved");
    }

    private void addPiazzaMessage(HttpServletRequest request, String message) {
        getOrCreateMessages(request).addMessage("piazzaMessage", message);
    }
}
