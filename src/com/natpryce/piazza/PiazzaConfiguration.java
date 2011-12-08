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

import jetbrains.buildServer.log.Loggers;
import org.apache.commons.io.IOUtils;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author tmeinen
 */
public class PiazzaConfiguration {

	private static final String XML_ROOT_NAME = "piazza";
	private static final String XML_ATTRIBUTE_NAME_SHOW_ON_FAILURE_ONLY = "showOnFailureOnly";
	static final String CONFIG_FILE_NAME = "piazza.xml";

	private boolean showOnFailureOnly;

	private String teamcityConfigDir;

/*
	public void readFrom(Element serverConfigRoot) {
		Element piazzaConfigRoot = serverConfigRoot.getChild("piazza");
		if (piazzaConfigRoot != null) {
			String showOnFailureOnlyFromConfig = piazzaConfigRoot.getAttributeValue(ATTRIBUTE_SHOW_ON_FAILURE_ONLY);
			showOnFailureOnly = Boolean.valueOf(showOnFailureOnlyFromConfig);
		}
	}
*/

	public synchronized void save() {
		try {
			writeElementTo(createConfigAsXml(), createConfigFileWriter());
		} catch (IOException e) {
			Loggers.SERVER.error("[PIAZZA] Unable to save xml configuration", e);
		}
	}

	Element createConfigAsXml() {
		Element piazzaConfigRoot = new Element(XML_ROOT_NAME);
		piazzaConfigRoot.setAttribute(XML_ATTRIBUTE_NAME_SHOW_ON_FAILURE_ONLY, String.valueOf(showOnFailureOnly));
		return piazzaConfigRoot;
	}

	Writer createConfigFileWriter() throws IOException {
		return new FileWriter(createConfigFile());
	}

	File createConfigFile() {
		return new File(teamcityConfigDir, CONFIG_FILE_NAME);
	}

	void writeElementTo(Element element, Writer writer) throws IOException {
		try {
			XMLOutputter outputter = new XMLOutputter();
			outputter.output(element, writer);
			writer.close();
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	public boolean isShowOnFailureOnly() {
		return showOnFailureOnly;
	}

	public void setShowOnFailureOnly(boolean showOnFailureOnly) {
		this.showOnFailureOnly = showOnFailureOnly;
	}

	public void setTeamcityConfigDir(@NotNull String teamcityConfigDir) {
		this.teamcityConfigDir = teamcityConfigDir;
	}
}
