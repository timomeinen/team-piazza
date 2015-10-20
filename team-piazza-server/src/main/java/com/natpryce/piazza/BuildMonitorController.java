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

import com.natpryce.piazza.projectConfiguration.PiazzaProjectSettings;
import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.serverSide.ProjectManager;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SProject;
import jetbrains.buildServer.serverSide.auth.AuthorityHolder;
import jetbrains.buildServer.serverSide.settings.ProjectSettingsManager;
import jetbrains.buildServer.users.SUser;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BuildMonitorController extends BaseController {

    public static final String BUILD_TYPE_ID = "buildTypeId";
    public static final String PROJECT_ID = "projectId";
    private static final String SHOW_FEATURE_BRANCH_BUILDS_ONLY = "featureBranchBuildsOnly";

    private final ProjectManager projectManager;
    private ProjectSettingsManager projectSettingsManager;
    private final Piazza piazza;

    public BuildMonitorController(SBuildServer server, ProjectManager projectManager, ProjectSettingsManager projectSettingsManager, Piazza piazza) {
        super(server);
        this.projectManager = projectManager;
        this.projectSettingsManager = projectSettingsManager;
        this.piazza = piazza;
    }

    protected ModelAndView doHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (requestHasParameter(request, BUILD_TYPE_ID)) {
            return showBuildType(request.getParameter(BUILD_TYPE_ID), response);
        } else if (requestHasParameter(request, PROJECT_ID)) {
            return showProject(request.getParameter(PROJECT_ID), Boolean.parseBoolean(request.getParameter(SHOW_FEATURE_BRANCH_BUILDS_ONLY)), response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "no build type id specified");
            return null;
        }
    }

    private ModelAndView showBuildType(String buildTypeId, HttpServletResponse response) throws IOException {
        SBuildType buildType = projectManager.findBuildTypeByExternalId(buildTypeId);
        if (buildType == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "no build type with id " + buildTypeId);
            return null;
        }

        BuildTypeMonitorViewState viewState = new BuildTypeMonitorViewState(buildType, piazza.userGroup(), piazza.isShowOnFailureOnly());
        return modelWithView("piazza-build-type-monitor.jsp").addObject("build", viewState);
    }

    private ModelAndView showProject(String projectId, boolean showFeatureBranchBuildsOnly, HttpServletResponse response) throws IOException {
        SProject project = projectManager.findProjectByExternalId(projectId);
        if (project == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "no project with id " + projectId);
            return null;
        }

        PiazzaProjectSettings projectSettings = (PiazzaProjectSettings) projectSettingsManager.getSettings(project.getProjectId(), PiazzaProjectSettings.PROJECT_SETTINGS_NAME);

        SUser associatedUser = getAssociatedUser();
        String view = showFeatureBranchBuildsOnly ? "piazza-project-monitor-feature-branches.jsp" : "piazza-project-monitor.jsp";

        return modelWithView(view).addObject("project", new ProjectMonitorViewState(project, piazza.userGroup(), piazza.getConfiguration(), projectSettings, associatedUser));
    }

    private ModelAndView modelWithView(String viewJSP) {
        return new ModelAndView(piazza.resourcePath(viewJSP))
                .addObject("version", piazza.version())
                .addObject("resourceRoot", piazza.resourcePath(""));
    }

    private boolean requestHasParameter(HttpServletRequest request, String parameterName) {
        return request.getParameterMap().containsKey(parameterName);
    }

    private SUser getAssociatedUser() {
        AuthorityHolder authorityHolder = piazza.getSecurityContext().getAuthorityHolder();
        SUser associatedUser = (SUser) authorityHolder.getAssociatedUser();
        if (associatedUser == null) {
            return piazza.getGuestUser();
        } else {
            return associatedUser;
        }
    }
}
