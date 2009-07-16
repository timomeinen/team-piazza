/*
   Copyright (c) 2007-2009 Nat Pryce.

   This file is part of Team Piazza.

   Team Piazza is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 3 of the License, or
   (at your option) any later version.

   Team Piazza is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.natpryce.piazza;

import jetbrains.buildServer.BuildProject;
import jetbrains.buildServer.BuildType;
import jetbrains.buildServer.web.openapi.PageExtension;

import javax.servlet.http.HttpServletRequest;
import static java.util.Collections.emptyList;
import java.util.List;
import java.util.Map;


public class PiazzaLinkPageExtension implements PageExtension {
    private final Piazza piazza;

    public PiazzaLinkPageExtension(Piazza piazza) {
        this.piazza = piazza;
    }

    public String getIncludeUrl() {
        return piazza.resourcePath("piazza-link.jsp");
    }
    
    public List<String> getCssPaths() {
        return emptyList();
    }

    public List<String> getJsPaths() {
        return emptyList();
    }
    
    public String getPluginName() {
        return Piazza.PLUGIN_NAME;
    }

    public boolean isAvailable(HttpServletRequest request) {
        return isBuildTypeView(request)
            || isProjectView(request);
    }

    public void fillModel(Map<String, Object> model, HttpServletRequest request) {
        model.put("piazzaHref", request.getContextPath() + Piazza.PATH + "?" + queryParameter(model, request));
    }

    private String queryParameter(Map<String, Object> model, HttpServletRequest request) {
        if (isBuildTypeView(request)) {
            return buildTypeQuery(model);
        }
        else if (isProjectView(request)) {
            return projectQuery(model);
        }
        else {
            throw new IllegalStateException("cannot create link for page at " + request.getRequestURI());
        }
    }

    private String projectQuery(Map<String, Object> model) {
        BuildProject project = (BuildProject) model.get("project");
        return "projectId=" + project.getProjectId();
    }

    private String buildTypeQuery(Map<String, Object> model) {
        BuildType buildType = (BuildType) model.get("buildType");
        return "buildTypeId=" + buildType.getBuildTypeId();
    }


    private boolean isProjectView(HttpServletRequest request) {
        return request.getRequestURI().endsWith("/project.html");
    }

    private boolean isBuildTypeView(HttpServletRequest request) {
        return request.getRequestURI().endsWith("/viewType.html");
    }

}
