/*
 * Copyright (c) 2011 Nat Pryce, Timo Meinen.
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

public enum BuildStatus {
	// in order of severity
	SUCCESS,
	UNKNOWN,
	FAILURE;

	@Override
	public String toString () {
		return name().charAt(0) + name().substring(1).toLowerCase();
	}

	public BuildStatus mostSevere (BuildStatus other) {
		return this.compareTo(other) > 0 ? this : other;
	}

	public static final String BUILDING = "Building";

	public String toStringReflectingCurrentlyBuilding (boolean isBuilding) {
		return this + (isBuilding ? " " + BUILDING : "");
	}
}
