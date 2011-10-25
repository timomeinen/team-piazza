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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserGroup {

	private List<PiazzaUser> piazzaUsers = new ArrayList<PiazzaUser>();

	public void add (PiazzaUser piazzaUser) {
		piazzaUsers.add(piazzaUser);
	}

	public Set<PiazzaUser> usersInvolvedInCommit (Collection<String> userIds, Collection<String> commitComments) {
		Set<PiazzaUser> involvedPiazzaUsers = new HashSet<PiazzaUser>();
		collectUsersByCommitterId(involvedPiazzaUsers, userIds);
		collectUsersByCommitComment(involvedPiazzaUsers, commitComments);
		return involvedPiazzaUsers;
	}

	private void collectUsersByCommitterId (Set<PiazzaUser> involvedPiazzaUsers, Collection<String> userIds) {
		for (PiazzaUser piazzaUser : piazzaUsers) {
			for (String userId : userIds) {
				if (piazzaUser.hasNickname(userId)) {
					involvedPiazzaUsers.add(piazzaUser);
				}
			}
		}
	}

	private void collectUsersByCommitComment (Set<PiazzaUser> involvedPiazzaUsers, Collection<String> commitComments) {
		for (PiazzaUser piazzaUser : piazzaUsers) {
			for (String commitComment : commitComments) {
				if (piazzaUser.hasNicknameWithin(commitComment)) {
					involvedPiazzaUsers.add(piazzaUser);
				}
			}
		}
	}

}
