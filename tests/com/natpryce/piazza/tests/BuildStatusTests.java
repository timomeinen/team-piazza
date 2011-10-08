package com.natpryce.piazza.tests;

import org.junit.Test;

import static com.natpryce.piazza.BuildStatus.*;
import static org.junit.Assert.assertEquals;


public class BuildStatusTests {

    @Test
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

    @Test
    public void testHasPrettyToString() {
        assertEquals("Success", SUCCESS.toString());
        assertEquals("Unknown", UNKNOWN.toString());
        assertEquals("Failure", FAILURE.toString());
    }
}
