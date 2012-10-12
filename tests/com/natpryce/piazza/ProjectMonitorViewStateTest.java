package com.natpryce.piazza;

import com.natpryce.piazza.pluginConfiguration.PiazzaConfiguration;
import com.natpryce.piazza.projectConfiguration.PiazzaProjectSettings;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SProject;
import jetbrains.buildServer.users.SUser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author Timo Meinen
 * @since 08.10.11
 */
public class ProjectMonitorViewStateTest {

    @Mock
    private SProject projectMock;

    @Mock
    private UserGroup userGroupMock;

    @Mock
    private SBuildType sBuildTypeMock;
    @Mock
    private SUser user;
    @Mock
    private PiazzaConfiguration configuration;
    @Mock
    private PiazzaProjectSettings projectSettings;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testNoNPEWhenNoBuildWasExecutedYet() throws Exception {
        // Issue #31
        when(sBuildTypeMock.isAllowExternalStatus()).thenReturn(true);
        when(projectMock.getBuildTypes()).thenReturn(Collections.singletonList(sBuildTypeMock));
        when(user.getOrderedBuildTypes(projectMock)).thenReturn(new ArrayList<SBuildType>());
        ProjectMonitorViewState projectMonitorViewState = new ProjectMonitorViewState(projectMock, userGroupMock, configuration, projectSettings, user);

        assertNotNull(projectMonitorViewState);
    }
}
