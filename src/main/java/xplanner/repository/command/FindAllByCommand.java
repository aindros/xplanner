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

package xplanner.repository.command;

import org.hibernate.Query;
import org.hibernate.Session;
import xplanner.util.QueryUtils;

import java.util.ArrayList;
import java.util.List;

public class FindAllByCommand<T, K> implements SessionCommand<List<T>> {
	private final K fieldValue;
	private final Class<T> domainClass;
	private final String fieldName;
	private final boolean cachable;

	public FindAllByCommand(String fieldName, K fieldValue, Class<T> domainClass, boolean cachable) {
		this.fieldValue = fieldValue;
		this.domainClass = domainClass;
		this.fieldName = fieldName;
		this.cachable = cachable;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> execute(Session session) {
		Query query = session.createQuery("from e in " + domainClass + " where e." + fieldName + " = :" + fieldName);
		QueryUtils.setParameter(query, fieldName, fieldValue);
		query.setCacheable(cachable);

		List<T> list = query.list();
		if (list == null)
			return new ArrayList<>();

		return list;
	}
}
