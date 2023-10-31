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

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.PersonRole;
import net.sf.xplanner.domain.PersonRoleId;
import org.springframework.stereotype.Component;
import xplanner.repository.command.FindAllByCommand;
import xplanner.repository.command.FindByCommand;
import xplanner.repository.command.HQLCommand;
import xplanner.repository.command.SessionCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("personRoleRepository")
public class PersonRoleRepositoryImpl extends BaseRepository<PersonRole, PersonRoleId> implements PersonRoleRepository {
	protected PersonRoleRepositoryImpl() {
		super(PersonRole.class);
	}

	@Override
	public List<PersonRole> findAllByUserId(int userId) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("userId", userId);

		SessionCommand<List<PersonRole>> command =
				new HQLCommand<>("from " + getDomainName() + " where person_id = :userId", parameters);

		return execute(command);
	}
}
