/*
 * Copyright (c) 2012 Nat Pryce, Timo Meinen, Frank Bregulla.
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

import jetbrains.buildServer.responsibility.ResponsibilityEntry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author fbregulla
 */
public class InvestigationViewStateTest {

    public static final String LONG_COMMENT_THAT_WILL_BE_TRUNCATED = "will fix it because blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah!";
    public static final String NAME = "fixer";

    @Test
    public void testGetState() {
        //given
        InvestigationViewState viewState = new InvestigationViewState(ResponsibilityEntry.State.TAKEN, NAME, null);
        //when
        ResponsibilityEntry.State description = viewState.getState();

        //then
        assertEquals(ResponsibilityEntry.State.TAKEN, description);
    }

    @Test
    public void testNoArgConstructor() {
        //given
        InvestigationViewState investigationViewState = new InvestigationViewState();
        //when
        String description = investigationViewState.getDescription();
        ResponsibilityEntry.State state = investigationViewState.getState();
        //then
        assertEquals("", description);
        assertEquals(ResponsibilityEntry.State.NONE, state);
    }

    @Test
    public void testGetDescriptionWithStateTakenWithLongComment() {
        assertDescription(ResponsibilityEntry.State.TAKEN, LONG_COMMENT_THAT_WILL_BE_TRUNCATED, "fixer is investigating - \"will fix it because blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah bl...\"");
    }

    @Test
    public void testGetDescriptionWithoutComment() {
        assertDescription(ResponsibilityEntry.State.TAKEN, null, "fixer is investigating");
    }

    @Test
    public void testGetDescriptionStateNone() {
        assertDescription(ResponsibilityEntry.State.NONE, null, "");
    }

    @Test
    public void testGetDescriptionStateGivenUp() {
        assertDescription(ResponsibilityEntry.State.GIVEN_UP, LONG_COMMENT_THAT_WILL_BE_TRUNCATED, "fixer gave up fixing - \"will fix it because blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah bl...\"");
    }

    @Test
    public void testGetDescriptionStateFixed() {
        assertDescription(ResponsibilityEntry.State.FIXED, LONG_COMMENT_THAT_WILL_BE_TRUNCATED, "fixer fixed the build - \"will fix it because blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah bl...\"");
    }

    private void assertDescription(ResponsibilityEntry.State state, String comment, String expectedDescription) {
        //given
        InvestigationViewState viewState = new InvestigationViewState(state, NAME, comment);
        //when
        String description = viewState.getDescription();

        //then
        assertEquals(expectedDescription, description);
    }
}
