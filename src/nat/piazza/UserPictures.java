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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class UserPictures {
	private Map<String,String> picturesByNickname = new HashMap<String,String>();
	
	public void add(String nickname, String pictureUrl) {
		picturesByNickname.put(nickname.toLowerCase(), pictureUrl);
	}
	
	public void loadFrom(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		try {
			loadFrom(reader);
		}
		finally {
			reader.close();
		}
	}
	
	public void loadFrom(Reader reader) throws IOException {
		loadFrom(new BufferedReader(reader));
	}
	
	public void loadFrom(BufferedReader reader) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			parseLine(line);
		}
	}
	
	private void parseLine(String line) {
		int sep = line.indexOf('=');
		if (sep < 0) {
			return; // skip malformed lines
		}
		
		String pictureUrl = line.substring(sep+1).trim();
		String nicknames = line.substring(0, sep);
		
		for (String nickname : nicknames.split(",")) {
			picturesByNickname.put(nickname.trim(), pictureUrl);
		}
	}
	
	public Set<String> picturesForComments(Collection<String> commitComments) {
		return picturesForWords(wordsOf(commitComments));
	}
	
	public Set<String> picturesForCommitterUserIds(Collection<String> userIds) {
		return picturesForWords(userIds);
	}
	
	private Set<String> picturesForWords(Collection<String> words) {
		Set<String> picturesOfCommitters = new HashSet<String>();
		for (String userName : picturesByNickname.keySet()) {
			if (words.contains(userName)) {
				picturesOfCommitters.add(picturesByNickname.get(userName));
			}
		}
		
		return picturesOfCommitters;
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
}
