package nat.piazza;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SProject;

public class BuildMonitorController extends BaseController {
	private final Piazza piazza;
	
	public BuildMonitorController(SBuildServer server, Piazza piazza) {
		super(server);
		this.piazza = piazza;
	}
	
	protected ModelAndView doHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] path = request.getServletPath().split("/");
		if (path.length < 3) {
			throw new IllegalArgumentException("project and build missing from path");
		}
		
		String projectName = path[path.length-2];
		String buildTypeName = Text.withoutExtension(path[path.length-1]);
				
		SProject project = myServer.getProjectManager().findProjectByName(projectName);
		if (project == null) {
			throw new IllegalArgumentException("no project named " + projectName);
		}
		
		SBuildType buildType = project.findBuildTypeByName(buildTypeName);
		if (buildType == null) {
			throw new IllegalArgumentException("no build type named " + buildTypeName + " in project " + projectName);			
		}
		
		BuildMonitorViewState buildViewState = new BuildMonitorViewState(piazza.version(), myServer, buildType, piazza.userGroup());
		
		String viewJspPath = piazza.resourcePath("piazza.jsp");
		ModelAndView view = new ModelAndView(viewJspPath);
		view.addObject("build", buildViewState);
		view.addObject("buildType", buildType);
		view.addObject("project", project);
		view.addObject("resourceRoot", piazza.resourcePath(""));
		return view;
	}
}
