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
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.TimeEntry;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xplanner.ThymeLeafTemplate;
import xplanner.domain.TimeEntryPerDay;
import xplanner.repository.*;
import xplanner.ui.BreadCrumbBuilder;
import xplanner.util.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(BaseController.PROJECT_TIMELOG_URL)
public class TimelogController extends BaseController {
	public static final Logger log = Logger.getLogger(TimelogController.class);

	private @Autowired ProjectRepository projectRepository;
	private @Autowired TimeEntryRepository timeEntryRepository;
	private @Autowired MessageSource messageSource;

	@RequestMapping(method = GET)
	public String doViewProject(@PathVariable Integer id,
	                            @RequestParam(value = "sd", required = false) Date startTime,
	                            @RequestParam(value = "ed", required = false) Date endTime,
	                            final HttpServletRequest request,
	                            Model model,
	                            Locale locale) throws AuthenticationException, ParseException {
		ControllerData data = new ControllerData();
		defaultModelAttributes(request, model, locale, data);
		Project project = projectRepository.findById(id);
		if (project == null) {
			return null;
		}

		if (startTime == null && endTime == null) {
			startTime = DateUtils.firstDayOfTheMonth();
			endTime = DateUtils.lastDayOfTheMonth();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Map<String, TimeEntryPerDay> timeEntryMap = new HashMap<>();
		Collection<String> keys = new LinkedHashSet<>();
		for (TimeEntry timeEntry : timeEntryRepository.findAllByProjectId(id, startTime, endTime)) {
			String key = sdf.format(timeEntry.getStartTime());
			keys.add(key);
			if (timeEntryMap.get(key) == null) {
				timeEntryMap.put(key, new TimeEntryPerDay(sdf.parse(key)));
			}

			timeEntryMap.get(key).addHours(timeEntry.getDuration());
		}

		List<TimeEntryPerDay> timeEntries = new ArrayList<>();
		double totalHours = 0;
		for (String key : keys) {
			TimeEntryPerDay entryPerDay = timeEntryMap.get(key);
			timeEntries.add(entryPerDay);
			totalHours += entryPerDay.getHours();
		}

		model.addAttribute("project", project);
		model.addAttribute("timeEntries", timeEntries);
		model.addAttribute("totalHours",  totalHours);

		addBreadCrumbs(model, new BreadCrumbBuilder(messageSource, locale).toProject(project).build());

		return ThymeLeafTemplate.PROJECT_TIMELOG.pageName;
	}
}