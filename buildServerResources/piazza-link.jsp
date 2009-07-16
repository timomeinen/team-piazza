<%--
  Copyright (C) 2007-2009 Nat Pryce.

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
--%>

<jsp:useBean id="piazzaHref"
			 type="java.lang.String"
			 scope="request"/>

<div>
    <a href="${piazzaHref}" title="Team Piazza Build Monitor">Team Piazza Build Monitor</a>
</div>