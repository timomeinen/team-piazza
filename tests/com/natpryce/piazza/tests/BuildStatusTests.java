package com.natpryce.piazza.tests;

import static com.natpryce.piazza.BuildStatus.*;
import junit.framework.TestCase;


public class BuildStatusTests extends TestCase {
    public void testCanReduceTwoStatusesToMostSevere() {
        assertEquals(SUCCESS, SUCCESS.mostSevere(SUCCESS));
        assertEquals(UNKNOWN, SUCCESS.mostSevere(UNKNOWN));
        assertEquals(FAILURE, SUCCESS.mostSevere(FAILURE));

        assertEquals(UNKNOWN, UNKNOWN.mostSevere(SUCCESS));
        assertEquals(UNKNOWN, UNKNOWN.mostSevere(UNKNOWN));
        assertEquals(FAILURE, UNKNOWN.mostSevere(FAILURE));

        assertEquals(FAILURE, FAILURE.mostSevere(SUCCESS));
        assertEquals(FAILURE, FAILURE.mostSevere(UNKNOWN));
        assertEquals(FAILURE, FAILURE.mostSevere(FAILURE));
    }

    public void testHasPrettyToString() {
        assertEquals("Success", SUCCESS.toString());
        assertEquals("Unknown", UNKNOWN.toString());
        assertEquals("Failure", FAILURE.toString());
    }
}
