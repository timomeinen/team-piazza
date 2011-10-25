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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import jetbrains.buildServer.Build;
import jetbrains.buildServer.notification.Notificator;
import jetbrains.buildServer.notification.NotificatorRegistry;
import jetbrains.buildServer.responsibility.ResponsibilityEntry;
import jetbrains.buildServer.responsibility.TestNameResponsibilityEntry;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SProject;
import jetbrains.buildServer.serverSide.SRunningBuild;
import jetbrains.buildServer.serverSide.STest;
import jetbrains.buildServer.serverSide.UserPropertyInfo;
import jetbrains.buildServer.serverSide.mute.MuteInfo;
import jetbrains.buildServer.tests.TestName;
import jetbrains.buildServer.users.NotificatorPropertyKey;
import jetbrains.buildServer.users.PropertyKey;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.vcs.VcsRoot;

/**
 * We use the {@link Notificator} to generate input fields on the user settings => notification rules page
 *
 * @author Timo Meinen
 */
public class PiazzaNotificator implements Notificator {

	private static final String TYPE = Piazza.PLUGIN_NAME;
	private static final String PROPERTY_USER_IMAGE_NAME = "userImage";
	private static final PropertyKey PROPERTY_USER_IMAGE_KEY = new NotificatorPropertyKey(TYPE, PROPERTY_USER_IMAGE_NAME);

	public PiazzaNotificator (NotificatorRegistry registry) {
		List<UserPropertyInfo> userProps = Collections.singletonList(new UserPropertyInfo(PROPERTY_USER_IMAGE_NAME, "Your image URL"));
		registry.register(this, userProps);
	}

	public String getNotificatorType () {
		return TYPE;
	}

	public String getDisplayName () {
		return "Piazza Build Monitor";
	}

	static String getPortraitUrl (SUser user) {
		return user.getPropertyValue(PROPERTY_USER_IMAGE_KEY);
	}

	public void notifyBuildStarted (SRunningBuild build, Set<SUser> users) {
		// ignored
	}

	public void notifyBuildSuccessful (SRunningBuild build, Set<SUser> users) {
		// ignored
	}

	public void notifyBuildFailed (SRunningBuild build, Set<SUser> users) {
		// ignored
	}

	public void notifyBuildFailedToStart (SRunningBuild build, Set<SUser> users) {
		// ignored
	}

	public void notifyLabelingFailed (Build build, VcsRoot root, Throwable exception, Set<SUser> users) {
		// ignored
	}

	public void notifyBuildFailing (SRunningBuild build, Set<SUser> users) {
		// ignored
	}

	public void notifyBuildProbablyHanging (SRunningBuild build, Set<SUser> users) {
		// ignored
	}

	public void notifyResponsibleChanged (SBuildType buildType, Set<SUser> users) {
		// ignored
	}

	public void notifyResponsibleAssigned (SBuildType buildType, Set<SUser> users) {
		// ignored
	}

	public void notifyResponsibleChanged (TestNameResponsibilityEntry oldValue, TestNameResponsibilityEntry newValue, SProject project, Set<SUser> users) {
		// ignored
	}

	public void notifyResponsibleAssigned (TestNameResponsibilityEntry oldValue, TestNameResponsibilityEntry newValue, SProject project, Set<SUser> users) {
		// ignored
	}

	public void notifyResponsibleChanged (Collection<TestName> testNames, ResponsibilityEntry entry, SProject project, Set<SUser> users) {
		// ignored
	}

	public void notifyResponsibleAssigned (Collection<TestName> testNames, ResponsibilityEntry entry, SProject project, Set<SUser> users) {
		// ignored
	}

	public void notifyTestsMuted (Collection<STest> tests, MuteInfo muteInfo, Set<SUser> users) {
		// ignored
	}

	public void notifyTestsUnmuted (Collection<STest> tests, MuteInfo muteInfo, Set<SUser> users) {
		// ignored
	}
}
