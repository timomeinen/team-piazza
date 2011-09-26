#!/bin/sh
#
# This script cycles between two Piazza build monitors being displayed
# in virtual workspaces of the same computer.
#
# To use:
#
# - Open Firefox windows on two adjacent virtual workspaces
# - Hide the navigation bars on each firefox window
# - Make the Firefox windows fullscreen (by pressing F11, for example)
# - Navigate to another virtual workspace
# - Run this script
# - Navigate to the left-most virtual workspace containing a fullscreen 
#   Piazza build monitor
#
# For added impressiveness, turn on Desktop Effects (aka Compiz or Beryl) and configure
# a cool workspace switch effect.

echo "Navigate to workspace showing left-most build monitor"
sleep 10

while true; do
	sleep 10
	xsendkeys Control+Alt+Right
	sleep 10
	xsendkeys Control+Alt+Left
done

