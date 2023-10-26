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

import java.util.List;

public class FindByCommand<T, K> implements SessionCommand<T> {
	private final K id;
	private final Class<T> domainClass;
	private final String fieldName;
	private final boolean cachable;

	public FindByCommand(String fieldName, K id, Class<T> domainClass, boolean cachable) {
		this.id = id;
		this.domainClass = domainClass;
		this.fieldName = fieldName;
		this.cachable = cachable;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T execute(Session session) {
		Query query = session.createQuery("from e in " + domainClass + " where e." + fieldName + " = :" + fieldName);
		if (id instanceof String)
			query.setString(fieldName, (String) id);
		query.setCacheable(cachable);

		List<?> list = query.list();

		if (list != null && !list.isEmpty())
			return (T) list.get(0);

		return null;
	}
}
