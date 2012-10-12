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
package com.natpryce.piazza.projectConfiguration;

import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.settings.ProjectSettings;
import org.jdom.Element;

/**
 * @author fbregulla
 */
public class PiazzaProjectSettings implements ProjectSettings {

    public static final String PROJECT_SETTINGS_NAME = "piazzaSettings";

    private static final String XML_ATTRIBUTE_NAME_SHOW_FEATURE_BRANCHES = "showFeatureBranches";
    private static final String XML_ATTRIBUTE_NAME_MAX_NUMBER_OF_FEATURE_BRANCHES = "maxNumberOfFeatureBranches";
    private static final String XML_ATTRIBUTE_NAME_MAX_AGE_IN_DAYS_OF_FEATURE_BRANCHES = "maxAgeInDaysOfFeatureBranches";

    private boolean showFeatureBranches;
    private int maxNumberOfFeatureBranchesToShow;
    private int maxAgeInDaysOfFeatureBranches;


    @Override
    public void dispose() {
        Loggers.SERVER.debug(this.getClass().getName() + ":dispose() called");
    }

    @Override
    public void readFrom(Element rootElement) {
        Loggers.SERVER.debug("readFrom :: " + rootElement.toString());

        this.showFeatureBranches = Boolean.parseBoolean(rootElement.getAttributeValue(XML_ATTRIBUTE_NAME_SHOW_FEATURE_BRANCHES, String.valueOf(false)));
        this.maxNumberOfFeatureBranchesToShow = Integer.parseInt(rootElement.getAttributeValue(XML_ATTRIBUTE_NAME_MAX_NUMBER_OF_FEATURE_BRANCHES, "0"));
        this.maxAgeInDaysOfFeatureBranches = Integer.parseInt(rootElement.getAttributeValue(XML_ATTRIBUTE_NAME_MAX_AGE_IN_DAYS_OF_FEATURE_BRANCHES, "5"));
    }

    @Override
    public void writeTo(Element parentElement) {
        parentElement.setAttribute(XML_ATTRIBUTE_NAME_SHOW_FEATURE_BRANCHES, String.valueOf(showFeatureBranches));
        parentElement.setAttribute(XML_ATTRIBUTE_NAME_MAX_NUMBER_OF_FEATURE_BRANCHES, String.valueOf(maxNumberOfFeatureBranchesToShow));
        parentElement.setAttribute(XML_ATTRIBUTE_NAME_MAX_AGE_IN_DAYS_OF_FEATURE_BRANCHES, String.valueOf(maxAgeInDaysOfFeatureBranches));
    }

    public boolean isShowFeatureBranches() {
        return showFeatureBranches;
    }

    public int getMaxNumberOfFeatureBranchesToShow() {
        return maxNumberOfFeatureBranchesToShow;
    }

    public int getMaxAgeInDaysOfFeatureBranches() {
        return maxAgeInDaysOfFeatureBranches;
    }

    public void setShowFeatureBranches(boolean showFeatureBranches) {
        this.showFeatureBranches = showFeatureBranches;
    }

    public void setMaxNumberOfFeatureBranchesToShow(int maxNumberOfFeatureBranchesToShow) {
        this.maxNumberOfFeatureBranchesToShow = maxNumberOfFeatureBranchesToShow;
    }

    public void setMaxAgeInDaysOfFeatureBranches(int maxAgeInDaysOfFeatureBranches) {
        this.maxAgeInDaysOfFeatureBranches = maxAgeInDaysOfFeatureBranches;
    }
}
