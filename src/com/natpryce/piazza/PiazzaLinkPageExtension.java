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
        return request.getRequestURI().endsWith("/viewType.html");
    }
    
    public void fillModel(Map<String, Object> model, HttpServletRequest request) {
        model.put("piazzaHref", request.getContextPath() + Piazza.PATH);
    }
}
