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

package xplanner.service;

import com.technoetic.xplanner.domain.Identifiable;
import net.sf.xplanner.domain.Permission;
import net.sf.xplanner.domain.Project;
import org.springframework.stereotype.Service;
import xplanner.util.DomainUtils;

import java.util.List;
import java.util.Map;

@Service
public class PermissionService extends BaseService {
	public static boolean permissionMatches(String permission,
	                                        Identifiable resource,
	                                        Map<Integer, List<Permission>> permissions) {
		return Permission.permissionMatches(permission,
		                                    DomainUtils.getTypeOfResource(resource),
		                                    resource.getId(),
		                                    permissions.get(resource.getId()))
				|| Permission.permissionMatches(permission,
				                                DomainUtils.getTypeOfResource(resource),
				                                Project.ANY_PROJECT,
				                                permissions.get(Project.ANY_PROJECT));
	}
}
