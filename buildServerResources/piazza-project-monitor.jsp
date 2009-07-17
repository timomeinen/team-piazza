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

<jsp:useBean id="project"
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
    <title>Piazza - ${project.projectName}</title>
    <meta http-equiv="refresh" content="${project.building ? 1 : 10}">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>${resourceRoot}piazza.css"/>
</head>
<body class="${project.combinedStatusClasses}">
<h1>${project.projectName}</h1>

<h2>${project.status}.</h2>


<div class="Content">

    <c:if test="${! empty project.committers}">
        <div class="Portraits">
            <c:forEach var="committer" items="${project.committers}">
                <img src="${fn:escapeXml(committer.portraitURL)}" title="${fn:escapeXml(committer.name)}">
            </c:forEach>
        </div>
    </c:if>


    <div class="Builds">
        <c:forEach var="build" items="${project.builds}">
            <div class="Build ${build.combinedStatusClasses}">
                <h3>${build.name} #${build.buildNumber}</h3>

                <c:if test="${build.building}">
                    <c:if test="${build.building}">
                        <div class="ProgressBar">
                            <div class="Activity ${build.runningBuildStatus}" style="width: ${build.completedPercent}%">
                                ${build.activity}
                                <c:if test="${build.tests.anyHaveRun}">
                                    (Tests passed: ${build.tests.passed},
                                    failed: ${build.tests.failed},
                                    ignored: ${build.tests.ignored})
                                </c:if>
                            </div>
                        </div>
                    </c:if>
                </c:if>
            </div>
        </c:forEach>
    </div>

    <div class="Version">
        Team Piazza version ${version}
    </div>
</div>
</body>
</html>
