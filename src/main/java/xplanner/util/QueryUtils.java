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

import org.hibernate.Query;

import java.util.Collection;
import java.util.Date;

public class QueryUtils {
	public static void setParameter(Query query, String fieldName, Object fieldValue) {
		if (fieldValue instanceof String) {
			query.setString(fieldName, (String) fieldValue);
		} else if (fieldValue instanceof Boolean) {
			query.setBoolean(fieldName, (Boolean) fieldValue);
		} else if (fieldValue instanceof Date) {
			query.setDate(fieldName, (Date) fieldValue);
		} else if (fieldValue instanceof Integer) {
			query.setInteger(fieldName, (Integer) fieldValue);
		} else if (fieldValue instanceof Collection) {
			query.setParameterList(fieldName, (Collection) fieldValue);
		} else {
			query.setParameter(fieldName, fieldValue);
		}
	}

	public static void setParameter(Query query, Object fieldValue, int index) {
		if (fieldValue instanceof String) {
			query.setString(index, (String) fieldValue);
		} else if (fieldValue instanceof Boolean) {
			query.setBoolean(index, (Boolean) fieldValue);
		} else if (fieldValue instanceof Date) {
			query.setDate(index, (Date) fieldValue);
		} else if (fieldValue instanceof Integer) {
			query.setInteger(index, (Integer) fieldValue);
		} else {
			query.setParameter(index, fieldValue);
		}

	}
}
