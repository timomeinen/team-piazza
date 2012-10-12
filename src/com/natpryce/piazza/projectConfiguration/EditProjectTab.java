package com.natpryce.piazza.projectConfiguration;

import jetbrains.buildServer.serverSide.ProjectManager;
import jetbrains.buildServer.serverSide.SProject;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PlaceId;
import jetbrains.buildServer.web.openapi.SimpleCustomTab;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public abstract class EditProjectTab extends SimpleCustomTab {
    private final ProjectManager myProjectManager;

    protected EditProjectTab(final PagePlaces pagePlaces, final String pluginName, final String includeUrl, final String title, final ProjectManager projectManager) {
        super(pagePlaces, PlaceId.EDIT_PROJECT_PAGE_TAB, pluginName, includeUrl, title);
        myProjectManager = projectManager;
        register();
    }

    public SProject getProject(@NotNull HttpServletRequest request) {
        String projectId = request.getParameter("projectId");
        if (projectId != null) {
            return myProjectManager.findProjectById(projectId);
        }
        return null;
    }

    public void fillModel(@NotNull final Map<String, Object> model, @NotNull final HttpServletRequest request) {
        super.fillModel(model, request);
        model.put("currentProject", getProject(request));
    }

    public boolean isAvailable(@NotNull final HttpServletRequest request) {
        return getProject(request) != null;
    }
}