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

import org.jdom.Attribute;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Timo Meinen
 * @since 07.12.11
 */
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
	}

	@Test
	public void testSave() throws IOException {
		Element element = new Element("piazza");
		StringWriter stringWriter = new StringWriter();

		piazzaConfiguration.writeElementTo(element, stringWriter);

		assertEquals("<piazza />", stringWriter.toString());
	}

}
