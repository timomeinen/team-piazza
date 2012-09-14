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

import com.natpryce.piazza.PiazzaConfiguration;
import jetbrains.buildServer.serverSide.*;

import java.util.*;

/**
 * @author fbregulla
 */
public class FeatureBranchesMonitorViewState {

    private Map<Branch, FeatureBranchMonitorViewState> featureBranchMonitorViewState = new HashMap<Branch, FeatureBranchMonitorViewState>();
    private PiazzaConfiguration configuration;
    private List<SBuildType> orderedBuildTypes;

    public FeatureBranchesMonitorViewState(SProject project, PiazzaConfiguration configuration, List<SBuildType> orderedBuildTypes) {
        this.configuration = configuration;
        this.orderedBuildTypes = orderedBuildTypes;
        if (configuration.isShowFeatureBranches()) {
            assembleRecentFeatureBranchesWithBuildTypesAndLatestBuilds(project);
        }
    }

    private void assembleRecentFeatureBranchesWithBuildTypesAndLatestBuilds(SProject project) {
        for (SBuildType buildType : project.getBuildTypes()) {
            for (SRunningBuild runningBuild : buildType.getRunningBuilds()) {
                addBuildToFeatureBranch(runningBuild);
            }

            for (SFinishedBuild finishedBuild : buildType.getHistory()) {
                addBuildToFeatureBranch(finishedBuild);
            }
        }
    }

    private void addBuildToFeatureBranch(SBuild build) {
        Branch branch = build.getBranch();
        if (isSkipBuild(build, branch)) {
            return;
        }

        if (isNewBranch(branch)) {
            FeatureBranchMonitorViewState viewState = new FeatureBranchMonitorViewState(branch, orderedBuildTypes);
            viewState.addBuild(build);
            featureBranchMonitorViewState.put(branch, viewState);
        } else {
            featureBranchMonitorViewState.get(branch).addBuild(build);
        }
    }

    private boolean isSkipBuild(SBuild build, Branch branch) {
        return hasNoBranch(branch) || isBuildTooOld(build) || branch.isDefaultBranch();
    }

    private boolean hasNoBranch(Branch branch) {
        return branch == null;
    }

    private boolean isBuildTooOld(SBuild build) {
        return (build.getStartDate().before(getTodayMinusNDays(configuration.getMaxAgeInDaysOfFeatureBranches())));
    }

    private boolean isNewBranch(Branch branch) {
        return !featureBranchMonitorViewState.containsKey(branch);
    }

    public List<FeatureBranchMonitorViewState> getFeatureBranches() {
        ArrayList<FeatureBranchMonitorViewState> featureBranches = new ArrayList<FeatureBranchMonitorViewState>(featureBranchMonitorViewState.values());
        sortFeatureBranchesByStartDate(featureBranches);
        return featureBranches.subList(0, Math.min(featureBranches.size(), configuration.getMaxNumberOfFeatureBranchesToShow()));
    }

    private void sortFeatureBranchesByStartDate(ArrayList<FeatureBranchMonitorViewState> featureBranches) {
        Collections.sort(featureBranches, new Comparator<FeatureBranchMonitorViewState>() {
            @Override
            public int compare(FeatureBranchMonitorViewState branch1, FeatureBranchMonitorViewState branch2) {
                return branch2.getLatestBuildDate().compareTo(branch1.getLatestBuildDate());
            }
        });
    }

    public Map<Branch, FeatureBranchMonitorViewState> getFeatureBranchMonitorViewState() {
        return featureBranchMonitorViewState;
    }

    private Date getTodayMinusNDays(int numberOfDays) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -numberOfDays);
        return c.getTime();
    }
}
