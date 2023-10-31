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
import net.sf.xplanner.domain.Project;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xplanner.ThymeLeafTemplate;
import xplanner.repository.ProjectRepository;
import xplanner.sql.Order;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/projects")
public class ProjectController extends BaseController {
	public static final Logger log = Logger.getLogger(ProjectController.class);

	private @Autowired ProjectRepository projectRepository;

	@RequestMapping(method = GET)
	public String doViewProjects(HttpServletRequest request,
	                             Model model,
	                             Locale locale) throws AuthenticationException {
		defaultModelAttributes(model, request, locale);

		List<Project> projects = projectRepository.findAll(new Order(Order.Direction.ASC, "hidden", "name"),
		                                                   SecurityHelper.getRemoteUserId(request));

		log.info("Found " + projects.size() + " projects");
		model.addAttribute("projects", projects);

		return ThymeLeafTemplate.PROJECTS.pageName;
	}
}
