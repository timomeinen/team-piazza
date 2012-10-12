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

import jetbrains.buildServer.serverSide.settings.ProjectSettings;
import jetbrains.buildServer.serverSide.settings.ProjectSettingsFactory;
import jetbrains.buildServer.serverSide.settings.ProjectSettingsManager;
import org.jetbrains.annotations.NotNull;

/**
 * @author fbregulla
 */
public class PiazzaProjectSettingsFactory implements ProjectSettingsFactory{

    public PiazzaProjectSettingsFactory(@NotNull ProjectSettingsManager projectSettingsManager) {
        projectSettingsManager.registerSettingsFactory("piazzaSettings", this);
    }

    @NotNull
    @Override
    public ProjectSettings createProjectSettings(String projectId) {
        return new PiazzaProjectSettings();
    }
}
