<!-- 
   Copyright (C) 2007 Nat Pryce.
   
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
 -->
<%@ include file="/include.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" errorPage="/runtimeError.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="build"
			 type="nat.piazza.BuildMonitorViewState"
			 scope="request"/>

<jsp:useBean id="resourceRoot"
			 type="java.lang.String"
			 scope="request"/>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Piazza - ${build.buildTypeName}</title>
    <meta http-equiv="refresh" content="${build.building ? 1 : 10}">
    <link rel="stylesheet" type="text/css" href="/css/progress.css"/>
    <link rel="stylesheet" type="text/css" href="${resourceRoot}piazza.css"/>
</head>
<body class="${build.combinedStatusClasses}">
<h1>${build.buildTypeName} #${build.buildNumber}</h1>
<h2>${build.status}. Duration: <bs:printTime time="${build.durationSeconds}"/>.</h2>

<div class="Content">
	<c:if test="${build.building}">
		<div class="Progress">
			<div id="bar">
				<div id="completed" class="${build.runningBuildStatus}" style="width: ${build.completedPercent}%"></div>
			</div>
			<div id="activity">
				${build.activity}
				
				<c:if test="${build.tests.anyHaveRun}">
				(Tests passed: ${build.tests.passed}, 
				failed: ${build.tests.failed},
				ignored: ${build.tests.ignored})
				</c:if>	
			</div>
		</div>
	</c:if>
	
	<c:if test="${! empty build.picturesOfCommitters}">
		<div class="Portraits">
			<c:forEach var="url" items="${build.picturesOfCommitters}">
				<img src="${url}"/>
			</c:forEach>
		</div>
	</c:if>
	
	<c:choose>
		<c:when test="${! empty build.commitMessages}">
			<div class="Changes">
				<ul>
				<c:forEach var="message" items="${build.commitMessages}">
					<li>
					<c:choose>
						<c:when test="${empty message}">
							<em class="NoCommitMessage">No commit message</em>
						</c:when>
						<c:otherwise>
							${message}
						</c:otherwise>
					</c:choose>
					</li>
				</c:forEach>
				</ul>
			</div>
		</c:when>
		<c:otherwise>
			<p class="NoChanges">No changes detected.</p>			
		</c:otherwise>
	</c:choose>
</div>

</body>
</html>
