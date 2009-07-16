package com.natpryce.piazza;

public enum BuildStatus {
    // in order of severity
    SUCCESS,
    UNKNOWN,
    FAILURE;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    public BuildStatus mostSevere(BuildStatus other) {
        return this.compareTo(other) > 0 ? this : other;
    }

    public static final String BUILDING = "Building";

    public String toStringReflectingCurrentlyBuilding(boolean isBuilding) {
        return this + (isBuilding ? " " + BUILDING : "");
    }
}
