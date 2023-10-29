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

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import xplanner.ThymeLeafTemplate;
import xplanner.repository.IterationRepository;
import xplanner.repository.ProjectRepository;

import java.util.List;

@Controller
@RequestMapping("/")
public class RootController {
	private @Autowired ProjectRepository projectRepository;
	private @Autowired IterationRepository iterationRepository;

	@RequestMapping(method = RequestMethod.GET)
	public String doViewRoot() {
		List<Project> projects = projectRepository.findProjectsByHidden(false);

		if (projects.size() != 1)
			return ThymeLeafTemplate.PROJECTS.redirectUrl;

		Iteration iteration = iterationRepository.findByProjectId(projects.iterator().next().getId());
		if (iteration == null)
			return ThymeLeafTemplate.PROJECTS.redirectUrl;

		return "redirect:/do/view/iteration?oid=" + iteration.getId();
	}
}
