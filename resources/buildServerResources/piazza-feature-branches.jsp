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
<c:if test="<%=!project.getFeatureBranchesView().getFeatureBranches().isEmpty()%>">
    <div class="FeatureBranches">
        <h2>Feature Branches</h2>

        <%--@elvariable id="featureBranch" type="com.natpryce.piazza.featureBranches.FeatureBranchMonitorViewState"--%>
        <c:forEach var="featureBranch" items="${project.featureBranchesView.featureBranches}">
            <div>
                <div class="Build ${featureBranch.combinedBuildStatus}">
                    <h3 class="FeatureBranchName">${featureBranch.name}</h3>

                    <div class="FeatureBranchBuilds">
                        <%--@elvariable id="buildType" type="com.natpryce.piazza.featureBranches.BuildTypeWithLatestBuildMonitorViewState"--%>
                        <c:forEach var="buildType" items="${featureBranch.buildTypes}">
                            <div class="FeatureBranchBuild">
                                <div class="ProgressBar ${buildType.runningBuildStatus}"
                                     style="width: ${buildType.completedPercent}%;"></div>
                                <div class="FeatureBranchBuildTypeName">${buildType.name}</div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</c:if>