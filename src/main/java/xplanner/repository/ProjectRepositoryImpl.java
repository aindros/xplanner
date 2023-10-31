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

import com.technoetic.xplanner.security.auth.Authorizer;
import com.technoetic.xplanner.security.auth.PrincipalSpecificPermissionHelper;
import net.sf.xplanner.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xplanner.repository.command.*;
import xplanner.sql.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("projectRepository")
public class ProjectRepositoryImpl extends BaseRepository<Project, Integer> implements ProjectRepository {
	private @Autowired PersonRoleRepository personRoleRepository;
	private @Autowired RoleRepository roleRepository2;
	protected @Autowired PrincipalSpecificPermissionHelper principalSpecificPermissionHelper;

	protected ProjectRepositoryImpl() {
		super(Project.class);
	}

	public List<Project> findProjectsByHidden(boolean hidden) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("hidden", hidden);

		return execute(new FindAllByCommand<>(parameters, null, Project.class, true));
	}

	@Override
	public List<Project> findAll(Order order, final int userId) {
		List<Integer> projectIds = new ArrayList<>();
		List<Integer> roleIds = new ArrayList<>();

		List<PersonRole> personRoles = personRoleRepository.findAllByUserId(userId);

		for (PersonRole personRole : personRoles) {
			projectIds.add(personRole.getProjectId());
			roleIds.add(personRole.getRoleId());
		}

		List<Role> roles = roleRepository2.findAllById(roleIds, null);
		for (Role role : roles) {
			if (role.getName().equals(Role.SYSADMIN)) {
				return super.findAll(order);
			}
		}

		return super.findAllById(projectIds, order);
	}
}
