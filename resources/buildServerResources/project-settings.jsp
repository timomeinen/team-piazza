<%--
  ~ Copyright (c) 2012 Nat Pryce, Timo Meinen, Frank Bregulla.
  ~
  ~ This file is part of Team Piazza.
  ~
  ~ Team Piazza is free software; you can redistribute it and/or modify
  ~ it under the terms of the GNU General Public License as published by
  ~ the Free Software Foundation; either version 3 of the License, or
  ~ (at your option) any later version.
  ~
  ~ Team Piazza is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~ GNU General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License
  ~ along with this program.  If not, see <http://www.gnu.org/licenses/>.
  --%>
<%@ include file="/include.jsp" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<%--<%@ taglib prefix="forms" uri="http://www.springframework.org/tags/form" %>--%>
<jsp:useBean id="resourceRoot" type="java.lang.String" scope="request"/>

<bs:linkScript>
    ${resourceRoot}js/piazza.js
</bs:linkScript>

<c:url var="actionUrl" value="/configurePiazzaProject.html?projectId=${projectId}"/>
<bs:refreshable containerId="piazzaProjectComponent" pageUrl="${pageUrl}">
    <h2>Piazza Build Monitor Settings</h2>

    <bs:messages key="piazzaMessage"/>

    <form action="${actionUrl}" id="piazzaProjectForm">
        <table>
            <tr>
                <th>Feature Branches</th>
                <td>
                    <p>
                        <forms:checkbox name="showFeatureBranches" checked="${showFeatureBranches}"/>
                        <label for="showFeatureBranches">Show feature branches in project view</label>
                    </p>
                    <p>
                        <input type="text" id="maxAgeInDaysOfFeatureBranches" value="${maxAgeInDaysOfFeatureBranches}" accept="number" maxlength="3" size="3"/>
                        <label for="maxAgeInDaysOfFeatureBranches">Max age in days of feature branches</label>
                    </p>
                    <p>
                        <input type="text" id="maxNumberOfFeatureBranches" value="${maxNumberOfFeatureBranches}" accept="number" maxlength="3" size="3"/>
                        <label for="maxNumberOfFeatureBranches">Max number of feature branches with recent activity to show</label>
                    </p>
                </td>
            </tr>
            <tr>
                <th>Save Settings:</th>
                <td>
                    <div>
                        <input type="button" id="piazzaSaveButton" onclick="$('piazzaSaveButton').disabled='true';  BS.Util.show($('piazzaSaveProgress')); return Piazza.saveProjectSettings();" value="Save"/>
                        <forms:saving id="piazzaSaveProgress" style="float:none"/>
                    </div>
                </td>
            </tr>
        </table>
    </form>
</bs:refreshable>