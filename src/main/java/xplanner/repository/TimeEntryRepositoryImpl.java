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

package xplanner.repository;

import com.technoetic.xplanner.security.auth.PrincipalSpecificPermissionHelper;
import net.sf.xplanner.domain.TimeEntry;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xplanner.repository.command.FindAllByCommand;
import xplanner.repository.command.HQLCommand;
import xplanner.sql.Order;

import java.text.SimpleDateFormat;
import java.util.*;

@Component("timeEntryRepository")
public class TimeEntryRepositoryImpl extends BaseRepository<TimeEntry, Integer> implements TimeEntryRepository {
	protected TimeEntryRepositoryImpl() {
		super(TimeEntry.class);
	}

	@Override
	public List<TimeEntry> findAllByProjectId(int projectId, Date startTime, Date endTime) {
		String hql = "select te from " + getDomainName() + " te " +
				"join te.task t " +
				"join t.userStory us " +
				"join us.iteration i " +
				"join i.project p " +
				"where p.id = :projectId ";

		StringBuilder sb = new StringBuilder(hql);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("projectId", projectId);

		if (startTime != null) {
			sb.append("and te.startTime >= :startTime ");
			parameters.put("startTime", startTime);
		}

		if (endTime != null) {
			sb.append("and te.endTime <= :endTime ");
			parameters.put("endTime", endTime);
		}

		sb.append("order by te.startTime asc");

		HQLCommand<TimeEntry> command = new HQLCommand<>(sb.toString(), parameters);

		return execute(command);
	}


//	private @Autowired IterationRepository iterationRepository;
//	private @Autowired PersonRoleRepository personRoleRepository;
//	private @Autowired RoleRepository roleRepository2;
//	protected @Autowired PrincipalSpecificPermissionHelper principalSpecificPermissionHelper;
//
//	protected TimeEntryRepositoryImpl() {
//		super(Project.class);
//	}
//
//	public List<Project> findProjectsByHidden(boolean hidden) {
//		Map<String, Object> parameters = new HashMap<>();
//		parameters.put("hidden", hidden);
//
//		return execute(new FindAllByCommand<>(parameters, null, Project.class, true));
//	}
//
//	@Override
//	public List<Project> findAll(Order order, final int userId) {
//		List<Integer> projectIds = new ArrayList<>();
//		List<Integer> roleIds = new ArrayList<>();
//
//		List<PersonRole> personRoles = personRoleRepository.findAllByUserId(userId);
//
//		for (PersonRole personRole : personRoles) {
//			projectIds.add(personRole.getProjectId());
//			roleIds.add(personRole.getRoleId());
//		}
//
//		List<Role> roles = roleRepository2.findAllById(roleIds, null);
//		boolean sysadmin = false;
//		for (Role role : roles) {
//			if (role.getName().equals(Role.SYSADMIN)) {
//				sysadmin = true;
//				break;
//			}
//		}
//
//		List<Project> projects;
//		if (sysadmin) {
//			projects = super.findAll(order);
//		} else {
//			projects = super.findAllById(projectIds, order);
//			CollectionUtils.filter(projects, new Predicate() {
//				@Override
//				public boolean evaluate(Object object) {
//					return !((Project) object).isHidden();
//				}
//			});
//		}
//
//		for (Project project : projects) {
//			project.setCurrentIteration(iterationRepository.findByProjectId(project.getId()));
//		}
//
//		return projects;
//	}
}
