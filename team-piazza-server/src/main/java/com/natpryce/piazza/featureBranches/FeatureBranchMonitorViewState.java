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
package com.natpryce.piazza.featureBranches;

import com.natpryce.piazza.BuildStatus;
import jetbrains.buildServer.serverSide.Branch;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildType;

import java.util.*;

/**
 * Contains all information for one feature branch.
 * Contains all build types where at least one build has been run for that feature branch together with the latest
 * build for each of those build types.
 *
 * @author fbregulla
 */
public class FeatureBranchMonitorViewState {

    private SortedMap<SBuildType, BuildTypeWithLatestBuildMonitorViewState> buildTypeWithLatestBuildViewState = new TreeMap<SBuildType, BuildTypeWithLatestBuildMonitorViewState>();
    private Branch branch;
    private List<SBuildType> orderedBuildTypes;
    private Date latestBuildStartDateInAnyBuildTypeOfThisBranch = new Date(0);

    public FeatureBranchMonitorViewState(Branch branch, List<SBuildType> orderedBuildTypes) {
        this.branch = branch;
        this.orderedBuildTypes = orderedBuildTypes;
    }

    public void addBuild(SBuild build) {
        //noinspection ConstantConditions
        if ((build.getBranch() == null) || !build.getBranch().getName().equals(branch.getName())) {
            throw new IllegalArgumentException("Only builds of branch " + branch + " may be added here!");
        }

        updateLatestStartDate(build);

        if (buildTypeWithLatestBuildViewState.containsKey(build.getBuildType())) {
            replaceBuildIfNewer(build);
        } else {
            buildTypeWithLatestBuildViewState.put(build.getBuildType(), new BuildTypeWithLatestBuildMonitorViewState(build));
        }
    }

    private void updateLatestStartDate(SBuild build) {
        if (build.getStartDate().after(latestBuildStartDateInAnyBuildTypeOfThisBranch)) {
            latestBuildStartDateInAnyBuildTypeOfThisBranch = build.getStartDate();
        }
    }

    private void replaceBuildIfNewer(SBuild build) {
        BuildTypeWithLatestBuildMonitorViewState currentlyLatestBuild = buildTypeWithLatestBuildViewState.get(build.getBuildType());
        if (currentlyLatestBuild.getStartDate().before(build.getStartDate())) {
            buildTypeWithLatestBuildViewState.put(build.getBuildType(), new BuildTypeWithLatestBuildMonitorViewState(build));
        }
    }

    public Collection<BuildTypeWithLatestBuildMonitorViewState> getBuildTypes() {
        List<BuildTypeWithLatestBuildMonitorViewState> buildTypesWithBuild = new ArrayList<BuildTypeWithLatestBuildMonitorViewState>();
        for (SBuildType orderedBuildType : orderedBuildTypes) {
            if (buildTypeWithLatestBuildViewState.containsKey(orderedBuildType)) {
                buildTypesWithBuild.add(buildTypeWithLatestBuildViewState.get(orderedBuildType));
            }
        }
        return buildTypesWithBuild;
    }

    public BuildTypeWithLatestBuildMonitorViewState getLatestBuildForBuildType(SBuildType buildType) {
        return buildTypeWithLatestBuildViewState.get(buildType);
    }

    public String getName() {
        return branch.getDisplayName();
    }

    public String getCombinedBuildStatus() {
        for (BuildTypeWithLatestBuildMonitorViewState buildTypes : getBuildTypes()) {
            if (buildTypes.getBuildStatus() == BuildStatus.FAILURE) {
                return BuildStatus.FAILURE.toString();
            }
        }

        return BuildStatus.SUCCESS.toString();
    }

    public boolean isBuilding() {
        for (BuildTypeWithLatestBuildMonitorViewState buildType : getBuildTypes()) {
            if (buildType.isBuilding()) {
                return true;
            }
        }
        return false;
    }

    public Date getLatestBuildDate() {
        return latestBuildStartDateInAnyBuildTypeOfThisBranch;
    }
}
