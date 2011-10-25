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
package com.natpryce.piazza.tests;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.natpryce.piazza.PiazzaUser;
import com.natpryce.piazza.UserGroup;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class UserGroupTests {

	private PiazzaUser alice = new PiazzaUser("Alice", set("alice"), "alice.png");
	private PiazzaUser bob = new PiazzaUser("Bob", set("bob"), "bob.png");
	private PiazzaUser carol = new PiazzaUser("Carol", set("carol", "cc rider"), "carol.png");
	private PiazzaUser dave = new PiazzaUser("David", set("david", "dave"), "dave.png");

	private UserGroup userGroup = new UserGroup();

	@Before
	public void setUp () throws Exception {
		userGroup.add(alice);
		userGroup.add(bob);
		userGroup.add(carol);
		userGroup.add(dave);
	}

	private Set<String> notRelevant = set("");

	@Test
	public void testReturnsPicturesOfWhoCheckedInBasedOnNicknamesInCommitComments () throws Exception {
		List<String> commitComments = asList("alice and bob did stuff", "bob and carol did something else");

		assertEquals(set(alice, bob, carol),
				userGroup.usersInvolvedInCommit(notRelevant, commitComments));
	}

	@Test
	public void testReturnsPicturesOfWhoCheckedInBasedOnUserIds () throws Exception {
		List<String> userIds = asList("alice", "bob", "dave");

		assertEquals(set(alice, bob, dave),
				userGroup.usersInvolvedInCommit(userIds, notRelevant));
	}

	@Test
	public void testUsesBothUserIdsAndCommitCommentsToSupportPairPRogramming () throws Exception {
		assertEquals(set(alice, bob),
				userGroup.usersInvolvedInCommit(set("alice"), set("alice and bob checked something in")));
	}

	@Test
	public void testAllowsMultipleNicknamesForTheSamePerson () throws Exception {
		List<String> commitComments = asList("alice and dave did stuff", "alice and david did some more");

		assertEquals(set(alice, dave),
				userGroup.usersInvolvedInCommit(notRelevant, commitComments));
	}

	@Test
	public void testCanParseNamesSeparatedByPunctuationRatherThanSpaces () {
		List<String> commitComments = asList("alice,bob: did stuff");

		assertEquals(set(alice, bob),
				userGroup.usersInvolvedInCommit(notRelevant, commitComments));
	}

	@Test
	public void testIsCaseInsensitive () {
		List<String> commitComments = asList("Alice & Bob: did stuff");

		assertEquals(set(alice, bob),
				userGroup.usersInvolvedInCommit(notRelevant, commitComments));
	}

	@Test
	public void testNicknamesCanContainSpaces () {
		List<String> commitComments = asList("alice and cc rider: did stuff");

		assertEquals(set(alice, carol),
				userGroup.usersInvolvedInCommit(notRelevant, commitComments));
	}

	@Test
	public void testNicknamesBeginAndEndAtWordBoundaries () {
		List<String> commitComments = asList("alice updated info for 125cc riders");

		assertEquals(set(alice),
				userGroup.usersInvolvedInCommit(notRelevant, commitComments));
	}

	@Test
	public void testMultiwordNicknamesCanBeAtEndOfCommitMessage () {
		List<String> commitComments = asList("alice & cc rider");

		assertEquals(set(alice, carol),
				userGroup.usersInvolvedInCommit(notRelevant, commitComments));
	}

	private <T> Set<T> set (T... elements) {
		return new LinkedHashSet<T>(asList(elements));
	}
}
