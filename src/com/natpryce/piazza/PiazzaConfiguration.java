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

import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.ServerPaths;
import org.apache.commons.io.IOUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * @author tmeinen, fbregulla
 */
public class PiazzaConfiguration {

    static final String XML_ROOT_NAME = "piazza";
    private static final String XML_ATTRIBUTE_NAME_SHOW_ON_FAILURE_ONLY = "showOnFailureOnly";
    private static final String XML_ATTRIBUTE_NAME_SHOW_FEATURE_BRANCHES = "showFeatureBranches";
    private static final String XML_ATTRIBUTE_NAME_MAX_NUMBER_OF_FEATURE_BRANCHES = "maxNumberOfFeatureBranches";
    private static final String XML_ATTRIBUTE_NAME_MAX_AGE_IN_DAYS_OF_FEATURE_BRANCHES = "maxAgeInDaysOfFeatureBranches";

    static final String CONFIG_FILE_NAME = "piazza.xml";
    String configFileName = CONFIG_FILE_NAME;
    private boolean showOnFailureOnly;
    private boolean showFeatureBranches;
    private int maxNumberOfFeatureBranchesToShow;
    private String teamCityConfigDir;
    private int maxAgeInDaysOfFeatureBranches;

    public PiazzaConfiguration(@NotNull ServerPaths serverPaths) {
        this.teamCityConfigDir = serverPaths.getConfigDir();
        this.loadConfigurationFromXmlFile();
    }

    public synchronized void save() throws SaveConfigFailedException {
        try {
            writeElementTo(createConfigAsXml(), createConfigFileWriter());
        } catch (IOException e) {
            Loggers.SERVER.error("[PIAZZA] Unable to save xml configuration", e);
            throw new SaveConfigFailedException(e);
        }
    }

    Element createConfigAsXml() {
        Element piazzaConfigRoot = new Element(XML_ROOT_NAME);
        piazzaConfigRoot.setAttribute(XML_ATTRIBUTE_NAME_SHOW_ON_FAILURE_ONLY, String.valueOf(showOnFailureOnly));
        piazzaConfigRoot.setAttribute(XML_ATTRIBUTE_NAME_SHOW_FEATURE_BRANCHES, String.valueOf(showFeatureBranches));
        piazzaConfigRoot.setAttribute(XML_ATTRIBUTE_NAME_MAX_NUMBER_OF_FEATURE_BRANCHES, String.valueOf(maxNumberOfFeatureBranchesToShow));
        piazzaConfigRoot.setAttribute(XML_ATTRIBUTE_NAME_MAX_AGE_IN_DAYS_OF_FEATURE_BRANCHES, String.valueOf(maxAgeInDaysOfFeatureBranches));
        return piazzaConfigRoot;
    }

    Writer createConfigFileWriter() throws IOException {
        return new FileWriter(createConfigFile());
    }

    File createConfigFile() {
        return new File(teamCityConfigDir, getConfigFileName());
    }

    String getConfigFileName() {
        return configFileName;
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

    void loadConfigurationFromXmlFile() {
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = builder.build(createConfigFile());
            Element rootElement = document.getRootElement();
            parseConfigFromXml(rootElement);
        } catch (FileNotFoundException e){
            save();
        } catch (JDOMException e) {
            Loggers.SERVER.error(e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            Loggers.SERVER.error(e);
            throw new RuntimeException(e);
        }

    }

    private void parseConfigFromXml(Element rootElement) {
        this.showOnFailureOnly = Boolean.parseBoolean(rootElement.getAttributeValue(XML_ATTRIBUTE_NAME_SHOW_ON_FAILURE_ONLY, String.valueOf(false)));
        this.showFeatureBranches = Boolean.parseBoolean(rootElement.getAttributeValue(XML_ATTRIBUTE_NAME_SHOW_FEATURE_BRANCHES, String.valueOf(false)));
        this.maxNumberOfFeatureBranchesToShow = Integer.parseInt(rootElement.getAttributeValue(XML_ATTRIBUTE_NAME_MAX_NUMBER_OF_FEATURE_BRANCHES, "0"));
        this.maxAgeInDaysOfFeatureBranches = Integer.parseInt(rootElement.getAttributeValue(XML_ATTRIBUTE_NAME_MAX_AGE_IN_DAYS_OF_FEATURE_BRANCHES, "5"));
    }

    public boolean isShowOnFailureOnly() {
        return showOnFailureOnly;
    }

    public void setShowOnFailureOnly(boolean showOnFailureOnly) {
        this.showOnFailureOnly = showOnFailureOnly;
    }

    public boolean isShowFeatureBranches() {
        return showFeatureBranches;
    }

    public void setShowFeatureBranches(boolean showFeatureBranches) {
        this.showFeatureBranches = showFeatureBranches;
    }

    public int getMaxNumberOfFeatureBranchesToShow() {
        return maxNumberOfFeatureBranchesToShow;
    }

    public void setMaxNumberOfFeatureBranchesToShow(int maxNumberOfFeatureBranchesToShow) {
        this.maxNumberOfFeatureBranchesToShow = maxNumberOfFeatureBranchesToShow;
    }

    public void setTeamCityConfigDir(@NotNull String teamCityConfigDir) {
        this.teamCityConfigDir = teamCityConfigDir;
    }

    public void setMaxAgeInDaysOfFeatureBranches(int maxAgeInDaysOfFeatureBranches) {
        this.maxAgeInDaysOfFeatureBranches = maxAgeInDaysOfFeatureBranches;
    }

    public int getMaxAgeInDaysOfFeatureBranches() {
        return maxAgeInDaysOfFeatureBranches;
    }

    public class SaveConfigFailedException extends RuntimeException {
        public SaveConfigFailedException(Throwable e) {
            super(e);
        }
    }
}
