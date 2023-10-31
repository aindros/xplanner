/*-
 * XPlanner is a software to keep track of your working activities
 * Copyright (C) 2021, 2023  Alessandro Iezzi <aiezzi AT alessandroiezzi PERIOD it>
 *
 * XPlanner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * XPlanner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with XPlanner.  If not, see <https://www.gnu.org/licenses/>.
 * package xplanner.controller;
 */

package xplanner.controller;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;
import net.sf.xplanner.domain.Permission;
import net.sf.xplanner.domain.Project;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xplanner.ThymeLeafTemplate;
import xplanner.repository.ProjectRepository;
import xplanner.service.PermissionService;
import xplanner.sql.Order;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/projects")
public class ProjectController extends BaseController {
	public static final Logger log = Logger.getLogger(ProjectController.class);

	private @Autowired ProjectRepository projectRepository;

	private void pagePermissions(Model model, ControllerData data) throws AuthenticationException {
		Map<String, Boolean> permissions = new HashMap<>();

		boolean createProject = Permission.permissionMatches(Permission.PERM_CREATE_PROJECT,
		                                                     Permission.RES_SYSTEM_PROJECT,
		                                                     Project.ANY_PROJECT,
		                                                     data.getPermissions().get(Project.ANY_PROJECT));

		permissions.put(Permission.PERM_CREATE_PROJECT, createProject);

		model.addAttribute("pagePermissions", permissions);
	}

	@RequestMapping(method = GET)
	public String doViewProjects(final HttpServletRequest request,
	                             Model model,
	                             Locale locale) throws AuthenticationException {


		ControllerData data = new ControllerData();

		defaultModelAttributes(request, model, locale, data);
		pagePermissions(model, data);

		List<Project> projects = projectRepository.findAll(new Order(Order.Direction.ASC, "hidden", "name"),
		                                                   SecurityHelper.getRemoteUserId(request));
		boolean canViewHiddenProjects = true;
		for (Project project : projects) {
			boolean editable  = PermissionService.permissionMatches(Permission.PERM_ADMIN_EDIT,
			                                                        project,
			                                                        data.getPermissions());
			boolean deletable = PermissionService.permissionMatches(Permission.PERM_SYSADMIN_DELETE,
			                                                        project,
			                                                        data.getPermissions());
			project.setEditable(editable);
			project.setDeletable(deletable);

			if (canViewHiddenProjects) {
				canViewHiddenProjects = PermissionService.permissionMatches(Permission.PERM_HIDE_PROJECT,
				                                                            project,
				                                                            data.getPermissions());
			}
		}

		log.info("Found " + projects.size() + " projects");
		model.addAttribute("projects", projects);
		model.addAttribute("canViewHiddenProjects", canViewHiddenProjects);

		return ThymeLeafTemplate.PROJECTS.pageName;
	}
}
