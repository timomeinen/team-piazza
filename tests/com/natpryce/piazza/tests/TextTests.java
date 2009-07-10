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

import com.natpryce.piazza.Text;
import junit.framework.TestCase;

public class TextTests extends TestCase {
	public void testTimeCasesWordsInString() {
		assertEquals("JMock", Text.toTitleCase("jMock"));
		assertEquals("JMock2", Text.toTitleCase("jMock2"));
		assertEquals("More Cheese", Text.toTitleCase("more cheese"));
		assertEquals("Quick Build", Text.toTitleCase("quick-build"));
	}
	
	public void testRemovesExtensionFromString() {
		assertEquals("build", Text.withoutExtension("build.html"));
		assertEquals("quick-build", Text.withoutExtension("quick-build.xml"));
		assertEquals("fast.slow.fast", Text.withoutExtension("fast.slow.fast.txt"));
		assertEquals("cheese", Text.withoutExtension("cheese"));
	}
}
