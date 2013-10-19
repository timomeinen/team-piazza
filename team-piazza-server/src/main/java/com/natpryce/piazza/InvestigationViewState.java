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

/**
 * Contains information about ongoing investigations of a build.
 *
 * @author fbregulla
 */
public class InvestigationViewState {

    private ResponsibilityEntry.State state;
    private String name;
    private String comment;

    public InvestigationViewState(ResponsibilityEntry.State state, String name, String comment) {
        this.state = state;
        this.name = name;
        this.comment = comment;
    }

    public InvestigationViewState() {
        this.state = ResponsibilityEntry.State.NONE;
        this.name = "";
        this.comment = "";
    }

    public ResponsibilityEntry.State getState() {
        return state;
    }

    public String getDescription() {
        if (state == ResponsibilityEntry.State.NONE) {
            return "";
        }
        String description = "";
        switch (state) {
            case TAKEN:
                description += name + " is investigating";
                break;
            case FIXED:
                description += name + " fixed the build";
                break;
            case GIVEN_UP:
                description += name + " gave up fixing";
                break;
        }
        if ((comment != null) && (!comment.isEmpty())) {
            description += " - \"" + getCommentTruncated(comment, 100) + "\"";
        }

        return description;
    }

    private String getCommentTruncated(String originalComment, int maxLength) {
        String comment = originalComment.trim();
        if (comment.length() > maxLength) {
            comment = comment.substring(0, maxLength - 3) + "...";
        }
        return comment;
    }
}
