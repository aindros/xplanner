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
import net.sf.xplanner.domain.Project;
import org.springframework.stereotype.Component;
import xplanner.repository.command.FindAllByCommand;
import xplanner.repository.command.FindByCommand;

import java.util.List;

@Component("projectRepository")
public class ProjectRepositoryImpl extends BaseRepository<Project, Integer> implements ProjectRepository {
	protected ProjectRepositoryImpl() {
		super(Project.class);
	}

	public List<Project> findProjectsByHidden(boolean hidden) {
		return execute(new FindAllByCommand<>("hidden", hidden, Project.class, true));
	}
}
