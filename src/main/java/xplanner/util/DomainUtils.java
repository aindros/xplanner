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

package xplanner.util;

import com.technoetic.xplanner.domain.Feature;
import com.technoetic.xplanner.domain.Integration;
import com.technoetic.xplanner.forms.*;
import net.sf.xplanner.domain.*;

import java.util.HashMap;
import java.util.Map;

public class DomainUtils {
	private static final Map<Class<?>,String> RESOURCE_TYPES = new HashMap<>();

	static {
		RESOURCE_TYPES.put(Project.class, "system.project");
		RESOURCE_TYPES.put(Iteration.class, "system.project.iteration");
		RESOURCE_TYPES.put(UserStory.class, "system.project.iteration.story");
		RESOURCE_TYPES.put(Task.class, "system.project.iteration.story.task");
		RESOURCE_TYPES.put(Feature.class, "system.project.iteration.story.feature");
		RESOURCE_TYPES.put(TimeEntry.class, "system.project.iteration.story.task.time_entry");
		RESOURCE_TYPES.put(Integration.class, "system.project.integration");
		RESOURCE_TYPES.put(Person.class, "system.person");
		RESOURCE_TYPES.put(Note.class, "system.note");
		RESOURCE_TYPES.put(ProjectEditorForm.class, "system.project");
		RESOURCE_TYPES.put(IterationEditorForm.class, "system.project.iteration");
		RESOURCE_TYPES.put(UserStoryEditorForm.class, "system.project.iteration.story");
		RESOURCE_TYPES.put(TaskEditorForm.class, "system.project.iteration.story.task");
		RESOURCE_TYPES.put(FeatureEditorForm.class, "system.project.iteration.story.feature");
		RESOURCE_TYPES.put(PersonEditorForm.class, "system.person");
		RESOURCE_TYPES.put(Setting.class, "system.setting");
	}

	public static String getTypeOfResource(Object resource) {
		String resourceClassName = resource.getClass().getName();

		for (Object clazz : RESOURCE_TYPES.keySet()) {
			String calssName = ((Class<?>) clazz).getName();
			if (resourceClassName.startsWith(calssName)) {
				return RESOURCE_TYPES.get(clazz);
			}
		}

		return null;
	}
}
