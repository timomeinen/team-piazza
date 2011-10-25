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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class PiazzaUser {

	private final String name;
	private final Set<String> nicknames;
	private final Set<Pattern> nicknamePatterns;
	private final String portraitURL;

	public PiazzaUser (String name, Collection<String> nicknames, String portraitURL) {
		this.name = name;
		this.portraitURL = portraitURL;
		this.nicknames = Collections.unmodifiableSet(new LinkedHashSet(nicknames));

		this.nicknamePatterns = new HashSet<Pattern>();
		for (String nickname : nicknames) {
			nicknamePatterns.add(
					Pattern.compile("\\b" + Pattern.quote(nickname) + "\\b",
							Pattern.CASE_INSENSITIVE));
		}
	}

	@Override
	public String toString () {
		return name;
	}

	@SuppressWarnings({"UnusedDeclaration"})
	public String getName () {
		return name;
	}

	@SuppressWarnings({"UnusedDeclaration"})
	public String getPortraitURL () {
		return portraitURL;
	}

	public boolean hasNickname (String nickname) {
		return nicknames.contains(nickname);
	}

	public boolean hasNicknameWithin (String commitComment) {
		for (Pattern nicknamePattern : nicknamePatterns) {
			if (nicknamePattern.matcher(commitComment).find()) {
				return true;
			}
		}
		return false;
	}
}
