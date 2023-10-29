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

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import org.hibernate.Query;
import org.hibernate.Session;
import xplanner.util.QueryUtils;

import java.util.List;
import java.util.Map;

public class QueryCommand<T, K> implements SessionCommand<T> {
	private final String queryName;
	private final Object[] parameters;
	private final Class<T> domainClass;

	public QueryCommand(String queryName,
	                    Object[] parameters,
	                    Class<T> domainClass) {
		this.queryName = queryName;
		this.parameters = parameters;
		this.domainClass = domainClass;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T execute(Session session) {
		Query query = session.getNamedQuery(queryName);
		for (int i = 0; i < parameters.length; i++) {
			QueryUtils.setParameter(query, parameters[i], i);
		}

		List<?> list = query.list();

		if (list != null && !list.isEmpty())
			return (T) list.get(0);

		return null;
	}
}
