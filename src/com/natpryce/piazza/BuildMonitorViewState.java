/*
   Copyright (c) 2007-2009 Nat Pryce.

   This file is part of Team Piazza.

   Team Piazza is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 3 of the License, or
   (at your option) any later version.

   Team Piazza is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.natpryce.piazza;

import jetbrains.buildServer.Build;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SRunningBuild;
import jetbrains.buildServer.serverSide.ShortStatistics;
import jetbrains.buildServer.vcs.SelectPrevBuildPolicy;
import jetbrains.buildServer.vcs.VcsModification;

import java.util.*;


public class BuildMonitorViewState {
	private static final String BUILDING = "Building";
	private static final String SUCCESS = "Success";
	private static final String UNKNOWN = "Unknown";
	private static final String FAILURE = "Failure";

	private final String version;
	private final SBuildType buildType;
	
	private final List<String> commitMessages;
	private Build lastFinishedBuild;
    private final Build latestBuild;
    private final TestStatisticsViewState tests;
    private final Set<User> committers;

    public BuildMonitorViewState(String version, SBuildServer server, SBuildType buildType, UserGroup userPictures) {
		this.version = version;
		this.buildType = buildType;
        this.lastFinishedBuild = buildType.getLastChangesFinished();
		this.latestBuild = buildType.getLastChangesStartedBuild();
		this.commitMessages = commitMessagesForBuild(latestBuild);

        committers = userPictures.usersInvolvedInCommit(
            committersForBuild(latestBuild),
            commitMessagesForBuild(latestBuild)
        );

        this.tests = testStatistics();
	}
	
	private Set<String> committersForBuild(Build latestBuild) {
		List<? extends VcsModification> changesSinceLastSuccessfulBuild = changesInBuild(latestBuild);
		
		HashSet<String> committers = new HashSet<String>();
		for (VcsModification vcsModification : changesSinceLastSuccessfulBuild) {
            String userName = vcsModification.getUserName();
            if (userName != null) {
			    committers.add(userName.trim());
            }
		}
		return committers;
	}
	
	private ArrayList<String> commitMessagesForBuild(Build latestBuild) {
		List<? extends VcsModification> changesSinceLastSuccessfulBuild = changesInBuild(latestBuild);
		
		ArrayList<String> commitMessages = new ArrayList<String>();
		for (VcsModification vcsModification : changesSinceLastSuccessfulBuild) {
			commitMessages.add(vcsModification.getDescription().trim());
		}
        
		return commitMessages;
	}
	
	private TestStatisticsViewState testStatistics() {
		if (isBuilding()) {
			ShortStatistics stats = ((SRunningBuild)latestBuild).getShortStatistics();
			return new TestStatisticsViewState(
				stats.getPassedTestCount(), stats.getFailedTestCount(), stats.getIgnoredTestCount());
		}
		else {
			return new TestStatisticsViewState(0,0,0);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<? extends VcsModification> changesInBuild(Build latestBuild) {
		return latestBuild.getChanges(SelectPrevBuildPolicy.SINCE_LAST_SUCCESSFULLY_FINISHED_BUILD, true);
	}
	
	public String getId() {
		return buildType.getBuildTypeId();
	}
	
	public String getBuildTypeName() {
		return Text.toTitleCase(buildType.getFullName());
	}
	
	public String getBuildNumber() {
		return latestBuild.getBuildNumber();
	}
	
	public String getCombinedStatusClasses() {
		String status = getStatus();
		if (isBuilding()) {
			status = status + " " + BUILDING;
		}
		return status;
	}
	
	public boolean isBuilding() {
		return !latestBuild.isFinished();
	}
	
	public Build getLatestBuild() {
		return latestBuild;
	}
	
	public String getActivity() {
		if (isBuilding()) {
			return ((SRunningBuild)latestBuild).getShortStatistics().getCurrentStage();
		}
		else {
			return getStatus();
		}
	}
	
	public int getCompletedPercent() {
		if (isBuilding()) {
			return ((SRunningBuild)latestBuild).getCompletedPercent();
		}
		else {
			return 100;
		}
	}
	
	
	public TestStatisticsViewState getTests() {
		return tests;
	}
	
	public long getDurationSeconds() {
		Date start = latestBuild.getStartDate();
		Date finished = latestBuild.getFinishDate();
		Date end = (finished != null) ? finished : now(); 
		
		return (end.getTime() - start.getTime())/1000L;
	}

	private Date now() {
		return new Date();
	}
	
	public String getStatus() {
		if (latestBuild == null) {
			return UNKNOWN;
		}
		else if (latestBuild.getBuildStatus().isFailed()) {
			return FAILURE;
		}
		if (lastFinishedBuild == null) {
			return UNKNOWN;
		} 
		else if (lastFinishedBuild.getBuildStatus().isFailed()) {
			return FAILURE;
		}
		else {
			return SUCCESS;
		}
	}
	
	public String getRunningBuildStatus() {
		if (latestBuild == null) {
			return UNKNOWN;
		}
		else if (latestBuild.getBuildStatus().isFailed()) {
			return FAILURE;
		}
		else {
			return SUCCESS;
		}
	}
	
	public List<String> getCommitMessages() {
		return commitMessages;
	}

	public Set<User> getCommitters() {
		return committers;
	}
	
	public String getPiazzaVersion() {
		return version;
	}
}
