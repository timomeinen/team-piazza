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

import java.util.regex.Pattern;

public class Text {
	public static final Pattern WORD_BOUNDARIES = Pattern.compile("\\W+");

	public static String toTitleCase(String input) {
		String[] words = WORD_BOUNDARIES.split(input);
		
		StringBuilder result = new StringBuilder();
		
		for (String word : words) {
			if (result.length() > 0) result.append(" ");
			result.append(Character.toTitleCase(word.charAt(0)));
			result.append(word.substring(1));
		}
		
		return result.toString();
	}
    
	public static String withoutExtension(String string) {
		int end = string.lastIndexOf('.');
		if (end == -1) {
			return string;
		}
		else {
			return string.substring(0, end);
		}
	}
}
