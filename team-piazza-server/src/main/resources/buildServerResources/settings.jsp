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
<jsp:useBean id="resourceRoot" type="java.lang.String" scope="request"/>

<bs:linkCSS>
    ${resourceRoot}piazza.css
</bs:linkCSS>

<bs:linkScript>
    ${resourceRoot}js/piazza.js
</bs:linkScript>

<c:url var="actionUrl" value="/configurePiazza.html"/>
<bs:refreshable containerId="piazzaComponent" pageUrl="${pageUrl}">
    <h2>Piazza Build Monitor Settings</h2>

    <bs:messages key="piazzaMessage"/>

    <form action="${actionUrl}" id="piazzaForm" onkeypress="return event.keyCode != 13;">
        <table>
            <tr>
                <th>Show Failure</th>
                <td>
                    <p>
                        <forms:checkbox name="showOnFailureOnly" checked="${showOnFailureOnly}"/>
                        <label for="showOnFailureOnly">Show user pictures only on build failure</label>
                    </p>
                </td>
            </tr>
            <tr>
                <th>Max Portrait Size</th>
                <td>
                    <p>
                        <forms:textField name="maxPortraitSize" value="${maxPortraitSize}" size="4" className="disableDefault"/>
                        <label for="maxPortraitSize">px. Maximum size of portraits. Maximum value is 512.</label>
                    </p>
                </td>
            </tr>
            <tr>
                <th>Save Settings</th>
                <td>
                    <div>
                        <input type="button" id="piazzaSaveButton"
                               onclick="$('piazzaSaveButton').disabled='true';  BS.Util.show($('piazzaSaveProgress')); return Piazza.save();"
                               value="Save"/>
                        <forms:saving id="piazzaSaveProgress" style="float:none"/>
                    </div>
                </td>
            </tr>
        </table>
    </form>
</bs:refreshable>