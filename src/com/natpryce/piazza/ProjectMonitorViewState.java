package com.natpryce.piazza;

import static com.natpryce.piazza.BuildStatus.SUCCESS;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SProject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProjectMonitorViewState {
    private final SProject project;
    private final Set<User> committers = new HashSet<User>();
    private List<BuildTypeMonitorViewState> builds;

    public ProjectMonitorViewState(SProject project, UserGroup userGroup) {
        this.project = project;

        builds = new ArrayList<BuildTypeMonitorViewState>();
        for (SBuildType buildType : project.getBuildTypes()) {
            builds.add(new BuildTypeMonitorViewState(buildType, userGroup));
        }

        for (BuildTypeMonitorViewState build : builds) {
            committers.addAll(build.getCommitters());
        }
    }

    public String getCombinedStatusClasses() {
        return getStatus().toStringReflectingCurrentlyBuilding(isBuilding());
    }
    
    public BuildStatus getStatus() {
        BuildStatus status = SUCCESS;
        for (BuildTypeMonitorViewState build : builds) {
            status = status.mostSevere(build.getStatus());
        }
        return status;
    }

    public boolean isBuilding() {
        for (BuildTypeMonitorViewState build : builds) {
            if (build.isBuilding()) return true;
        }
        return false;
    }

    public List<BuildTypeMonitorViewState> getBuilds() {
        return builds;
    }
    
    public Set<User> getCommitters() {
        return committers;
    }
}
