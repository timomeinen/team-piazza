/*
 * Copyright (c) 2012 Nat Pryce, Timo Meinen.
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
package com.natpryce.piazza.pluginConfiguration;

import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tmeinen, fbregulla
 */
public class ConfigurationController extends BaseController {

    public static final int MAX_PORTRAIT_SIZE_LIMIT = 512; // 512px is the maximum value for Gravatar
    private final PiazzaConfiguration piazzaConfiguration;

    public ConfigurationController(SBuildServer server, WebControllerManager manager, PiazzaConfiguration piazzaConfiguration) {
        super(server);
        manager.registerController("/configurePiazza.html", this);
        this.piazzaConfiguration = piazzaConfiguration;
    }

    @Override
    protected ModelAndView doHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws Exception {
        try {
            readConfigurationFromView(request);
            this.piazzaConfiguration.save();
            addSuccessMessage(request);
        } catch (SaveConfigFailedException e) {
            Loggers.SERVER.error(e);
            addErrorMessage(request, e.getLocalizedMessage());
        }
        return null;
    }

    private void readConfigurationFromView(@NotNull HttpServletRequest request) {
        boolean showOnFailureOnly = getShowOnFailureOnlyValueFromView(request);
        this.piazzaConfiguration.setShowOnFailureOnly(showOnFailureOnly);

        int maxPortraitSize = tryParseMaxPortraitSize(request);
        this.piazzaConfiguration.setMaxPortraitSize(maxPortraitSize);
    }

    private int tryParseMaxPortraitSize(@NotNull HttpServletRequest request) {
        try {
            int maxPortraitSize = Integer.parseInt(request.getParameter("maxPortraitSize"));

            if (maxPortraitSize > MAX_PORTRAIT_SIZE_LIMIT)
                throw new SaveConfigFailedException("Maximum size is 512px.");

            return maxPortraitSize;
        } catch (NumberFormatException e) {
            throw new SaveConfigFailedException("Please enter only a number as portrait size in px.");
        }
    }

    private boolean getShowOnFailureOnlyValueFromView(HttpServletRequest request) {
        return getBooleanParameter(request, "showOnFailureOnly");
    }

    private boolean getBooleanParameter(HttpServletRequest request, String parameterName) {
        String parameter = request.getParameter(parameterName);
        if (parameter == null)
            return false;
        return Boolean.valueOf(parameter);
    }

    private void addSuccessMessage(HttpServletRequest request) {
        addPiazzaMessage(request, "Saved");
    }

    private void addErrorMessage(HttpServletRequest request, String message) {
        addPiazzaMessage(request, "Save failed: " + message);
    }

    private void addPiazzaMessage(HttpServletRequest request, String message) {
        getOrCreateMessages(request).addMessage("piazzaMessage", message);
    }
}
