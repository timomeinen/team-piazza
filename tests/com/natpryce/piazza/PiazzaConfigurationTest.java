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

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.log.Loggers;
import org.jdom.Attribute;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

/**
 * @author Timo Meinen
 * @since 07.12.11
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Loggers.class)
public class PiazzaConfigurationTest {

	private PiazzaConfiguration piazzaConfiguration;

	@Before
	public void setUp() {
		piazzaConfiguration = new PiazzaConfiguration();
	}

	@Test
	public void testRootXml() {
		Element element = piazzaConfiguration.createConfigAsXml();

		assertNotNull(element);
		assertEquals("piazza", element.getName());
	}

	@Test
	public void testAttributeShowOnFailureOnly() {
		assertAttributeForShowOnFailureOnly(true);
		assertAttributeForShowOnFailureOnly(false);
	}

	private void assertAttributeForShowOnFailureOnly(Boolean showOnFailureOnly) {
		piazzaConfiguration.setShowOnFailureOnly(showOnFailureOnly);

		Element element = piazzaConfiguration.createConfigAsXml();
		Attribute showOnFailureOnlyAttribute = element.getAttribute("showOnFailureOnly");

		assertNotNull(showOnFailureOnlyAttribute);
		assertEquals(showOnFailureOnly.toString(), showOnFailureOnlyAttribute.getValue());
		assertEquals(showOnFailureOnly, piazzaConfiguration.isShowOnFailureOnly());
	}

	@Test
	public void testErrorHandling() throws Exception {
		// given an element
		Element element = new Element("piazza");

		// given the writer is not able to write and throws an IO Exception
		Writer mockWriter = mock(Writer.class);
		IOException toBeThrown = new IOException("test exception");
		doThrow(toBeThrown).when(mockWriter).write("<");

		// given the TeamCity Log mechanism catches the Exception
		Logger mockLogger = mock(Logger.class);
		MemberModifier.stub(method(Loggers.class, "createLoggerInstance")).toReturn(mockLogger);

		PiazzaConfiguration spyPiazzaConfiguration = spy(piazzaConfiguration);
		when(spyPiazzaConfiguration.createConfigAsXml()).thenReturn(element);
		when(spyPiazzaConfiguration.createConfigFileWriter()).thenReturn(mockWriter);

		// when trying to output
		spyPiazzaConfiguration.save();

		// then a log message shall be written
		verify(mockLogger).error("[PIAZZA] Unable to save xml configuration", toBeThrown);
		// then the writer shall be closed
		verify(mockWriter).close();
	}

	@Test
	public void testWriteElementTo() throws IOException {
		Element element = new Element("piazza");
		element.setAttribute("testAttribute", "testValue");
		StringWriter stringWriter = new StringWriter();

		piazzaConfiguration.writeElementTo(element, stringWriter);

		assertEquals("<piazza testAttribute=\"testValue\" />", stringWriter.toString());
	}

	@Test
	public void testConfigFile() throws IOException {
		String teamcityConfigDir = "/teamcity-config-dir";
		piazzaConfiguration.setTeamcityConfigDir(teamcityConfigDir);

		File file = piazzaConfiguration.createConfigFile();

		assertNotNull(file);
		String expectedPath = String.format("%s/%s", teamcityConfigDir, PiazzaConfiguration.CONFIG_FILE_NAME);
		assertEquals(expectedPath, file.getAbsolutePath());
	}

	@Test
	public void testSaveAggregatesCreationMethods() throws IOException {
		PiazzaConfiguration spyPiazzaConfiguration = spy(piazzaConfiguration);
		doReturn(new StringWriter()).when(spyPiazzaConfiguration).createConfigFileWriter();
		doNothing().when(spyPiazzaConfiguration).writeElementTo(any(Element.class), any(Writer.class));

		spyPiazzaConfiguration.save();

		verify(spyPiazzaConfiguration).createConfigAsXml();
		verify(spyPiazzaConfiguration).createConfigFileWriter();
		verify(spyPiazzaConfiguration).writeElementTo(any(Element.class), any(Writer.class));
	}
}
