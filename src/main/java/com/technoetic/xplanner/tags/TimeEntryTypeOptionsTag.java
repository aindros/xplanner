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

package com.technoetic.xplanner.tags;

import com.technoetic.xplanner.security.AuthenticationException;
import org.hibernate.HibernateException;
import xplanner.domain.TimeEntryType;

import java.util.List;

public class TimeEntryTypeOptionsTag extends OptionsTag {
	public static final String ALL_TIME_ENTRY_TYPES_QUERY = "from " + TimeEntryType.class.getName() + " as t";

	@Override
	public void release() {
		super.release();
	}

	@Override
	protected List<?> getOptions() throws HibernateException, AuthenticationException {
		return fetchAllTypes();
	}

	@SuppressWarnings("unchecked")
	private List<TimeEntryType> fetchAllTypes() throws HibernateException {
		return getSession().createQuery(ALL_TIME_ENTRY_TYPES_QUERY).list();
	}
}
