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

import xplanner.repository.command.*;
import xplanner.sql.Order;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseRepository<T, K> extends SessionCommandExecutor implements Repository<T, K> {
	protected final Class<T> domainClass;

	protected BaseRepository(Class<T> domainClass) {
		this.domainClass = domainClass;
	}

	@Override
	public T findById(final K id) {
		return findById(id, false);
	}

	@Override
	public T findById(K id, boolean cachable) {
		return execute(new FindByCommand<>("id", id, domainClass, cachable));
	}

	@Override
	public List<T> findAll(Order order) {
		return execute(new FindAllByCommand<>(null, order, domainClass, false));
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAllById(Collection<K> ids, Order order) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("id", ids);

		String hql = "from " + getDomainName() + " where id in (:id)";

		if (order != null)
			hql += " " + order.toSql();

		return (List<T>) execute(new HQLCommand<>(hql, parameters));
	}

	protected String getDomainName() {
		return domainClass.getName();
	}
}
