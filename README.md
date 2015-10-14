Public life in the Team City
============================

Piazza is a build monitor plugin for the JetBrains [TeamCity](http://www.jetbrains.com/teamcity/) [continuous integration](http://www.martinfowler.com/articles/continuousIntegration.html) server.
Piazza provides a high-visibility display of the current state of the build to alert the team as soon as the build breaks.


For any build, Piazza displays:

* The project and build name
* The current build number
* Whether the build is "red" or "green"
* Textual success/failure indication for the colour-blind
* Whether the Team City server is currently building
* The changes that caused the current build to be kicked off
* Pictures of the team members that made those changes
* The progress of the build
* The build step that the Team City agent is currently running
* The number of passed, failed and ignored tests

Piazza can also display an overview of all the builds in a project.

Installation
------------
1. [Download](https://github.com/timomeinen/team-piazza/releases) the Piazza plugin zip file
1. Open TeamCity Server website and go to 'Administration > Plugins List'
1. Click 'Upload plugin zip'
1. Choose the Piazza plugin zip file and save it to '\<TeamCity Data Directory\>/plugins', which is preselected
1. Restart TeamCity Server
1. For every build configuration you want to see in Piazza go to 'Build Configuration Settings > General Settings', click 'Show advanced options' and 'enable status widget'
![Enable status widget](https://github.com/timomeinen/team-piazza/wiki/images/team_piazza-enable_status_widget.png)

More information available from the TeamCity documentation: [Installing Additional Plugins](https://confluence.jetbrains.com/display/TCD9/Installing+Additional+Plugins)

Use Piazza Build Monitor
------------------------
Go to a project page to see all build configurations in the monitor or choose a single build configuration and click on the 'Team Piazza Build Monitor' link:
![Launch Piazza Build Monitor](https://github.com/timomeinen/team-piazza/wiki/images/team_piazza-launch_piazza.png)

Documentation
-------------
[Documentation](https://github.com/timomeinen/team-piazza/wiki)

Introduction
------------
[![Video Software Success Disciplines.03 - Continuous Integration 4/4](http://img.youtube.com/vi/MoSbwXVmjOQ/0.jpg)](http://www.youtube.com/watch?v=MoSbwXVmjOQ)

Screencast by Mike Hogan of [Software Confidence](http://www.softwareconfidence.com).

Licensing
---------
Team Piazza is licensed under the [GPL3](http://www.gnu.org/copyleft/gpl.html).
If that license causes difficulties, create an issue to discuss alternative license options.
