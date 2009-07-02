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

import org.jdom.Element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class UserGroup {
    private static final String USER = "user";
    private static final String HREF = "href";
    private static final String NAME = "name";
    private static final String NICKNAME = "nickname";
    
    private List<User> users = new ArrayList<User>();
    private Map<String, User> usersByNickname = new HashMap<String, User>();

    public void add(User user) {
        users.add(user);
        for (String nickname : user.nicknames()) {
            usersByNickname.put(nickname.toLowerCase(), user);
        }
    }

    public Set<User> usersInvolvedInCommit(Collection<String> userIds, Collection<String> commitComments) {
        Set<User> involvedUsers = new HashSet<User>();
        collectUsersForWords(involvedUsers, userIds);
        collectUsersForWords(involvedUsers, wordsOf(commitComments));
        return involvedUsers;
    }

    private void collectUsersForWords(Set<User> involvedUsers, Collection<String> words) {
        for (String nickname : usersByNickname.keySet()) {
            if (words.contains(nickname)) {
                involvedUsers.add(usersByNickname.get(nickname));
            }
        }
    }

    private Set<String> wordsOf(Collection<String> commitComments) {
        Set<String> words = new HashSet<String>();
        for (String message : commitComments) {
            for (String word : Text.WORD_BOUNDARIES.split(message)) {
                words.add(word.toLowerCase());
            }
        }
        return words;
    }
    
    public static UserGroup loadFrom(Element element) {
        UserGroup userGroup = new UserGroup();

        for (Element userElement : (List<Element>)element.getChildren(USER)) {
            String portraitUrl = userElement.getAttribute(HREF).getValue();
            String name = userElement.getChild(NAME).getTextTrim();

            Set<String> nicknames = new HashSet<String>();
            for (Element nicknameElement : (List<Element>)userElement.getChildren(NICKNAME)) {
                String nickname = nicknameElement.getTextTrim();
                nicknames.add(nickname);
            }

            userGroup.add(new User(name, nicknames, portraitUrl));
        }

        return userGroup;
    }
    
    public void writeTo(Element piazzaConfigRoot) {
        for (User user : users) {
            Element userConfigElement = new Element(USER);
            
            userConfigElement.setAttribute(HREF, user.getPortraitURL());
            userConfigElement.addContent(elementWithText(NAME, user.getName()));
            for (String nickname : user.nicknames()) {
                userConfigElement.addContent(elementWithText(NICKNAME, nickname));
            }

            piazzaConfigRoot.addContent(userConfigElement);
        }
    }

    private Element elementWithText(String name, String text) {
        Element element = new Element(name);
        element.setText(text);
        return element;
    }
}
