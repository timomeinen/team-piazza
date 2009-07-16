<!--
Copyright (C) 2007-2009 Nat Pryce.

This file is part of Team Piazza.

Team Piazza is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

Team Piazza is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
-->
<%@ include file="/include.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" errorPage="/runtimeError.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="builds"
             type="com.natpryce.piazza.ProjectMonitorViewState"
             scope="request"/>

<jsp:useBean id="resourceRoot"
             type="java.lang.String"
             scope="request"/>

<jsp:useBean id="version"
             type="java.lang.String"
             scope="request"/>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Piazza - ${build.buildTypeName}</title>
    <meta http-equiv="refresh" content="${build.building ? 1 : 10}">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/progress.css"/>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>${resourceRoot}piazza.css"/>
</head>
<body class="${builds.combinedStatusClasses}">
<h1>${project.name}</h1>

<h2>${builds.status}.</h2>


<div class="Content">

    <c:if test="${! empty builds.committers}">
        <div class="Portraits">
            <c:forEach var="committer" items="${builds.committers}">
                <img src="${fn:escapeXml(committer.portraitURL)}" title="${fn:escapeXml(committer.name)}">
            </c:forEach>
        </div>
    </c:if>


    <div class="Builds">
        <c:forEach var="build" items="${builds.builds}">
            <div class="Build ${build.combinedStatusClasses}">
                ${build.name}
            </div>
        </c:forEach>
    </div>
</div>

<div class="Version">
    Team Piazza version ${version}
</div>
</body>
</html>
