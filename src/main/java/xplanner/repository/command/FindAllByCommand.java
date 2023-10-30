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
import xplanner.sql.Order;
import xplanner.util.QueryUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FindAllByCommand<T, K> implements SessionCommand<List<T>> {
	private final Map<String, Object> queryParameters;
	private final Class<T> domainClass;
	private final boolean cachable;
	private final Order order;

	public FindAllByCommand(Map<String, Object> queryParameters,
	                        Order order,
	                        Class<T> domainClass,
	                        boolean cachable) {
		this.queryParameters = queryParameters;
		this.domainClass = domainClass;
		this.cachable = cachable;
		this.order = order;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> execute(Session session) {
		StringBuilder sb = new StringBuilder("from e in ")
				.append(domainClass);

		if (queryParameters != null) {
			sb.append(" where ");
			int i = 0;
			for (Map.Entry<String, Object> entry : queryParameters.entrySet()) {
				sb.append("e.").append(entry.getKey()).append(" = :").append(entry.getKey());
				if (i++ < queryParameters.size() - 1)
					sb.append(" and ");
			}
		}

		if (order != null) {
			sb.append(" ").append(order.toSql());
		}

		Query query = session.createQuery(sb.toString());

		if (queryParameters != null) {
			for (Map.Entry<String, Object> entry : queryParameters.entrySet()) {
				QueryUtils.setParameter(query, entry.getKey(), entry.getValue());
			}
		}

		query.setCacheable(cachable);

		List<T> list = query.list();
		if (list == null)
			return new ArrayList<>();

		return list;
	}
}
