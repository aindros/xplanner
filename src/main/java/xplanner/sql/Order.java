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

package xplanner.sql;

import lombok.Getter;

public class Order {
	private @Getter final String[] fields;
	private @Getter final Direction direction;

	public Order(Direction direction, String...fields) {
		this.direction = direction;
		this.fields = fields;
	}

	public enum Direction {
		ASC, DESC
	}

	public String toSql() {
		if (fields == null || direction == null) return "";

		StringBuilder sb = new StringBuilder("order by ");
		for (int i = 0; i < fields.length; i++) {
			sb.append(fields[i]);
			if (i < fields.length - 1)
				sb.append(",");
		}
		sb.append(" ").append(direction.name());

		return sb.toString();
	}
}
