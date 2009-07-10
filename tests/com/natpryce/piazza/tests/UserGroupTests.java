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
package com.natpryce.piazza.tests;

import com.natpryce.piazza.User;
import com.natpryce.piazza.UserGroup;
import junit.framework.TestCase;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import static java.util.Arrays.asList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class UserGroupTests extends TestCase {
    User alice = new User("Alice", set("alice"), "alice.png");
    User bob = new User("Bob", set("bob"), "bob.png");
    User carol = new User("Carol", set("carol", "cc rider"), "carol.png");
    User dave = new User("David", set("david", "dave"), "dave.png");

    UserGroup userGroup = new UserGroup();
    protected void setUp() throws Exception {
		userGroup.add(alice);
		userGroup.add(bob);
		userGroup.add(carol);
        userGroup.add(dave);
	}

    Set<String> notRelevant = set();

	public void testReturnsPicturesOfWhoCheckedInBasedOnNicknamesInCommitComments() throws Exception {
		List<String> commitComments = asList("alice and bob did stuff", "bob and carol did something else");
		
		assertEquals(set(alice, bob, carol),
					 userGroup.usersInvolvedInCommit(notRelevant, commitComments));
	}

    public void testReturnsPicturesOfWhoCheckedInBasedOnUserIds() throws Exception {
        List<String> userIds = asList("alice", "bob", "dave");

        assertEquals(set(alice, bob, dave),
                     userGroup.usersInvolvedInCommit(userIds, notRelevant));
    }

    public void testUsesBothUserIdsAndCommitCommentsToSupportPairPRogramming() throws Exception {
        assertEquals(set(alice, bob),
                     userGroup.usersInvolvedInCommit(set("alice"), set("alice and bob checked something in")));
    }

	public void testAllowsMultipleNicknamesForTheSamePerson() throws Exception {
		List<String> commitComments = asList("alice and dave did stuff", "alice and david did some more");
		
		assertEquals(set(alice, dave),
					 userGroup.usersInvolvedInCommit(notRelevant, commitComments));
	}
	
	public void testCanParseNamesSeparatedByPunctuationRatherThanSpaces() {
		List<String> commitComments = asList("alice,bob: did stuff");
		
		assertEquals(set(alice, bob), 
			         userGroup.usersInvolvedInCommit(notRelevant, commitComments));
	}
	
	public void testIsCaseInsensitive() {
		List<String> commitComments = asList("Alice & Bob: did stuff");
		
		assertEquals(set(alice, bob),
			         userGroup.usersInvolvedInCommit(notRelevant, commitComments));
	}

    public void testNicknamesCanContainSpaces() {
        List<String> commitComments = asList("alice and cc rider: did stuff");

        assertEquals(set(alice, carol),
                     userGroup.usersInvolvedInCommit(notRelevant, commitComments));
    }

    public void testNicknamesBeginAndEndAtWordBoundaries() {
        List<String> commitComments = asList("alice updated info for 125cc riders");

        assertEquals(set(alice),
                     userGroup.usersInvolvedInCommit(notRelevant, commitComments));
    }
    
    public void testMultiwordNicknamesCanBeAtEndOfCommitMessage() {
        List<String> commitComments = asList("alice & cc rider");

        assertEquals(set(alice, carol),
                     userGroup.usersInvolvedInCommit(notRelevant, commitComments));
    }


    private static final String CONFIG__XML =
        "<piazza>" +
        "  <user portrait='alice.png'>" +
        "    <name>Alice Band</name>" +
        "    <nickname>alice</nickname>" +
        "  </user>" +
        "  <user portrait='http://www.bob.com/bob.png'>" +
        "    <name>Bob Frapples</name>" +
        "    <nickname>robert</nickname>" +
        "    <nickname>bob</nickname>" +
        "  </user>" +
        "</piazza>";


	public void testCanBeLoadedFromXmlElement() throws IOException, JDOMException {
        Document doc = fromXML(CONFIG__XML);
        
        userGroup = UserGroup.loadFrom(doc.getRootElement());

        assertContainsUserWithName("Alice Band",
            userGroup.usersInvolvedInCommit(notRelevant, asList("alice & bob did stuff")));
        assertContainsUserWithName("Bob Frapples",
            userGroup.usersInvolvedInCommit(notRelevant, asList("alice & bob did stuff")));
	}

    public void testCanBeSavedToXmlElement() throws JDOMException, IOException {
        UserGroup userGroupToSave= new UserGroup();
        userGroupToSave.add(new User("Alice Band", set("alice"), "alice.png"));
        userGroupToSave.add(new User("Bob Frapples", set("robert", "bob"), "http://www.bob.com/bob.png"));

        Document savedDoc = new Document(new Element("piazza"));
        userGroupToSave.writeTo(savedDoc.getRootElement());

        Document expectedDoc = fromXML(CONFIG__XML);

        assertEquals(asXML(expectedDoc), asXML(savedDoc));
    }

    private String asXML(Document doc) throws IOException {
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        StringWriter writer = new StringWriter();
        outputter.output(doc, writer);
        return writer.toString();
    }

    private Document fromXML(String xml) throws JDOMException, IOException {
        return new SAXBuilder().build(new StringReader(xml));
    }

    private void assertContainsUserWithName(String name, Set<User> users) {
        for (User user : users) {
            if (user.getName().equals(name)) return;
        }

        fail("no user named \"" + name + "\" in " + users);
    }

    private <T> Set<T> set(T ... elements) {
		return new LinkedHashSet<T>(asList(elements));
	}
}
