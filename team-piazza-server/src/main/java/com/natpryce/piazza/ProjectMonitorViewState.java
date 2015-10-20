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

import com.natpryce.piazza.featureBranches.FeatureBranchesMonitorViewState;
import com.natpryce.piazza.pluginConfiguration.PiazzaConfiguration;
import com.natpryce.piazza.projectConfiguration.PiazzaProjectSettings;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SProject;
import jetbrains.buildServer.users.SUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.natpryce.piazza.BuildStatus.SUCCESS;

/**
 * @author Nat Pryce, Timo Meinen
 */
public class ProjectMonitorViewState {

    private final SProject project;
    private final PiazzaConfiguration piazzaConfiguration;
    private PiazzaProjectSettings projectSettings;
    private final Set<PiazzaUser> committers = new HashSet<>();
    private final List<BuildTypeMonitorViewState> builds;
    private final FeatureBranchesMonitorViewState featureBranchesView;

    public ProjectMonitorViewState(SProject project, UserGroup userGroup, PiazzaConfiguration piazzaConfiguration, PiazzaProjectSettings projectSettings, SUser user) {
        this.project = project;
        this.piazzaConfiguration = piazzaConfiguration;
        this.projectSettings = projectSettings;

        builds = new ArrayList<>();
        for (SBuildType buildType : project.getBuildTypes()) {
            if (hasAtLeastOneBuild(buildType)) {
                if (buildType.isAllowExternalStatus()) {
                    builds.add(new BuildTypeMonitorViewState(buildType, userGroup, piazzaConfiguration.isShowOnFailureOnly()));
                }
            }
        }

        for (BuildTypeMonitorViewState build : builds) {
            committers.addAll(build.getCommitters());
        }

        featureBranchesView = new FeatureBranchesMonitorViewState(project, projectSettings, getOrderedBuildTypesOfProjectAndSubprojects(project, user));
    }

    private List<SBuildType> getOrderedBuildTypesOfProjectAndSubprojects(SProject project, SUser user) {
        List<SBuildType> orderedBuildTypes = user.getOrderedBuildTypes(project);
        for (SProject childProject : project.getProjects()) {
            orderedBuildTypes.addAll(user.getOrderedBuildTypes(childProject));
        }
        return orderedBuildTypes;
    }

    private boolean hasAtLeastOneBuild(SBuildType buildType) {
        return buildType.getLastChangesStartedBuild() != null;
    }

    public String getProjectName() {
        return project.getName();
    }

    public String getStatus() {
        return status().toString();
    }

    public BuildStatus status() {
        if (builds.isEmpty()) {
            return BuildStatus.UNKNOWN;
        } else {
            BuildStatus status = SUCCESS;
            for (BuildTypeMonitorViewState build : builds) {
                status = status.mostSevere(build.status());
            }
            return status;
        }
    }

    public boolean isBuilding() {
        return isBuildingDefaultBranches() || (projectSettings.isShowFeatureBranches() && getFeatureBranchesView().isBuilding());
    }

    private boolean isBuildingDefaultBranches() {
        for (BuildTypeMonitorViewState build : builds) {
            if (build.isBuilding()) {
                return true;
            }
        }
        return false;
    }

    public String getBuildingStatus() {
        return isBuildingDefaultBranches() ? "Building" : "";
    }

    public List<BuildTypeMonitorViewState> getBuilds() {
        return builds;
    }

    public Set<PiazzaUser> getCommitters() {
        return committers;
    }

    public FeatureBranchesMonitorViewState getFeatureBranchesView() {
        return featureBranchesView;
    }

    public String getPortraitMaxSize() {
        return String.valueOf(piazzaConfiguration.getMaxPortraitSize()).concat("px");
    }
}
