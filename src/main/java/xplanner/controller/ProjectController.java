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
import net.sf.xplanner.domain.TimeEntry;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xplanner.ThymeLeafTemplate;
import xplanner.domain.TimeEntryPerDay;
import xplanner.domain.TimeEntryType;
import xplanner.repository.ProjectRepository;
import xplanner.repository.TimeEntryRepository;
import xplanner.repository.TimeEntryTypeRepository;
import xplanner.service.PermissionService;
import xplanner.sql.Order;
import xplanner.ui.BreadCrumbBuilder;
import xplanner.util.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(BaseController.PROJECTS_URL)
public class ProjectController extends BaseController {
	public static final Logger log = Logger.getLogger(ProjectController.class);

	private @Autowired ProjectRepository projectRepository;
	private @Autowired TimeEntryRepository timeEntryRepository;
	private @Autowired TimeEntryTypeRepository timeEntryTypeRepository;

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

	@RequestMapping(value = PROJECT_TIMELOG_URL, method = GET)
	public String doViewProjectTimelog(@PathVariable Integer id,
	                                   @RequestParam(value = "m", required = false) Integer month,
	                                   @RequestParam(value = "y", required = false) Integer year,
	                                   final HttpServletRequest request,
	                                   Model model,
	                                   Locale locale) throws AuthenticationException, ParseException {
		ControllerData data = new ControllerData();
		defaultModelAttributes(request, model, locale, data);
		Project project = projectRepository.findById(id);
		if (project == null) {
			return null;
		}

		Date startTime = null, endTime = null;

		if (month != null && year != null) {
			startTime = DateUtils.firstDayOfTheMonth(month, year);
			endTime = DateUtils.lastDayOfTheMonth(month, year);
		} else {
			startTime = DateUtils.firstDayOfTheMonth();
			endTime = DateUtils.lastDayOfTheMonth();

			Calendar calendar = Calendar.getInstance();
			month = calendar.get(Calendar.MONTH);
			year = calendar.get(Calendar.YEAR);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Map<String, TimeEntryPerDay> timeEntryMap = new HashMap<>();
		Collection<String> keys = new LinkedHashSet<>();
		Map<Integer, TimeEntryType> timeEntryTypeMap = new HashMap<>();
		for (TimeEntry timeEntry : timeEntryRepository.findAllByProjectId(id, startTime, endTime)) {
			int timeEntryTypeId = timeEntry.getTimeEntryTypeId() == null? 0 : timeEntry.getTimeEntryTypeId();
			String key = timeEntryTypeId + "|" + sdf.format(timeEntry.getStartTime());
			keys.add(key);
			if (timeEntryMap.get(key) == null) {
				timeEntryMap.put(key, new TimeEntryPerDay(timeEntry.getStartTime()));
			}

			TimeEntryPerDay ted = timeEntryMap.get(key);
			ted.addHours(timeEntry.getDuration());

			if (timeEntryTypeId != 0) {
				TimeEntryType entryType = timeEntryTypeMap.get(timeEntryTypeId);

				if (entryType == null) {
					entryType = timeEntryTypeRepository.findById(timeEntry.getTimeEntryTypeId());
					timeEntryTypeMap.put(timeEntry.getTimeEntryTypeId(), entryType);
				}

				ted.setEntryTimeTypeName(entryType.getName());
			}
		}

		List<TimeEntryPerDay> timeEntries = new ArrayList<>();
		double totalHours = 0;
		for (String key : keys) {
			TimeEntryPerDay entryPerDay = timeEntryMap.get(key);
			timeEntries.add(entryPerDay);
			totalHours += entryPerDay.getHours();
		}

		model.addAttribute("monthFilter", month);
		model.addAttribute("yearFilter", year);
		model.addAttribute("project", project);
		model.addAttribute("timeEntries", timeEntries);
		model.addAttribute("totalHours",  totalHours);

		addBreadCrumbs(model, new BreadCrumbBuilder(messageSource, locale).toProject(project).build());

		return ThymeLeafTemplate.PROJECT_TIMELOG.pageName;
	}
}
