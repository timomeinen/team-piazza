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
package nat.piazza.tests;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import nat.piazza.UserPictures;

public class UserPicturesTests extends TestCase {
	UserPictures userPictures = new UserPictures();
	
	protected void setUp() throws Exception {
		userPictures.add("alice", "alice.png");
		userPictures.add("bob", "bob.png");
		userPictures.add("carol", "carol.png");
		userPictures.add("eve", "eve.png");
	}
	
	public void testReturnsPicturesOfWhoCheckedInBasedOnUserComments() throws Exception {
		List<String> commitComments = asList("alice and bob did stuff", "bob and carol did something else");
		
		assertEquals(set("alice.png", "bob.png", "carol.png"), 
					 userPictures.picturesForComments(commitComments));
	}
	
	public void testAllowsMultipleNicknamesForTheSamePerson() throws Exception {
		userPictures.add("robert", "bob.png");
		List<String> commitComments = asList("alice and bob did stuff", "alice and robert did some more");
		
		assertEquals(set("alice.png", "bob.png"), 
					 userPictures.picturesForComments(commitComments));
	}
	
	public void testCanParseNamesSeparatedByPunctuationRatherThanSpaces() {
		List<String> commitComments = asList("alice,bob: did stuff");
		
		assertEquals(set("alice.png", "bob.png"), 
			         userPictures.picturesForComments(commitComments));
	}
	
	public void testIsCaseInsensitive() {
		List<String> commitComments = asList("Alice & Bob: did stuff");
		
		assertEquals(set("alice.png", "bob.png"), 
			         userPictures.picturesForComments(commitComments));
	}
	
	public void testCanBeLoadedFromReader() throws IOException {
		StringReader reader = new StringReader(
			"alice = alice.png\n" +
			"robert, bob = http://www.bob.com/bob.png\n");
		
		userPictures = new UserPictures();
		userPictures.loadFrom(reader);

		assertEquals(set("alice.png", "http://www.bob.com/bob.png"), 
	         userPictures.picturesForComments(asList("alice & bob did stuff")));
		
		assertEquals(set("alice.png", "http://www.bob.com/bob.png"), 
	         userPictures.picturesForComments(asList("alice & robert did stuff")));
	}
	
	public void testCanBeLoadedFromAConfigurationFile() throws IOException {
		File configFile = new File("testdata/pictures.cfg");
		
		userPictures = new UserPictures();
		userPictures.loadFrom(configFile);
		
		assertEquals(set("alice.png", "http://www.bob.com/bob.png"), 
	         userPictures.picturesForComments(asList("alice & bob did stuff")));
		
		assertEquals(set("alice.png", "http://www.bob.com/bob.png"), 
	         userPictures.picturesForComments(asList("alice & robert did stuff")));
	}
	
	private <T> Set<T> set(T ... elements) {
		return new HashSet<T>(asList(elements));
	}
}
