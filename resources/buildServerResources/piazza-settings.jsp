<%--
  ~ Copyright (c) 2012 Nat Pryce, Timo Meinen.
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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="showOnFailureOnly" type="java.lang.Boolean" scope="request"/>

<div id="serverSettings">

    <h2>Piazza Build Monitor Configuration</h2>

    <c:url var="actionUrl" value="/configurePiazza.html"/>
    <form action="${actionUrl}" method="post">
        <p>
            <label for="showOnFailureOnly">Show user pictures only on build failure</label>
            <forms:checkbox name="showOnFailureOnly" checked="${showOnFailureOnly}"/>
        </p>

        <div class="saveButtonsBlock">
            <input class="submitButton" type="submit" value="Save">
        </div>
    </form>

</div>