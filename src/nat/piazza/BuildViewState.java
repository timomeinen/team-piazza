/*
 *  Copyright (C) 2007 Nat Pryce.
 *  
 *  This file is part of Team Piazza.
 *  
 *  Team Piazza is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Team Piazza is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nat.piazza;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import jetbrains.buildServer.Build;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SRunningBuild;
import jetbrains.buildServer.vcs.SelectPrevBuildPolicy;
import jetbrains.buildServer.vcs.VcsModification;


public class BuildViewState {
	private static final String BUILDING = "Building";
	private static final String SUCCESS = "Success";
	private static final String UNKNOWN = "Unknown";
	private static final String FAILURE = "Failure";

	private final SBuildType buildType;
	
	private final List<String> commitMessages;
	private final Set<String> picturesOfCommitters;
	
	private Build lastFinishedBuild;
	private Build latestBuild;

	public BuildViewState(SBuildServer server, SBuildType buildType, UserPictures userPictures) {
		this.buildType = buildType;
		
		lastFinishedBuild = buildType.getLastFinished();
		latestBuild = buildType.getLastStartedBuild();
		
		commitMessages = commitMessagesForBuild(latestBuild);
		picturesOfCommitters = userPictures.picturesForComments(commitMessages);
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<String> commitMessagesForBuild(Build latestBuild) {
		List<VcsModification> changesSinceLastSuccessfulBuild = 
			latestBuild.getChanges(SelectPrevBuildPolicy.SINCE_LAST_SUCCESSFULLY_FINISHED_BUILD, true);
		
		ArrayList<String> commitMessages = new ArrayList<String>();
		for (VcsModification vcsModification : changesSinceLastSuccessfulBuild) {
			commitMessages.add(vcsModification.getDescription());
		}
		return commitMessages;
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

	public Set<String> getPicturesOfCommitters() {
		return picturesOfCommitters;
	}
}
