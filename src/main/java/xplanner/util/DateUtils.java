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

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public static Date firstDayOfTheMonth() {
		Calendar calendar = Calendar.getInstance();
		return firstDayOfTheMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
	}

	public static Date lastDayOfTheMonth() {
		Calendar calendar = Calendar.getInstance();
		return lastDayOfTheMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
	}

	public static Calendar firstDayOfTheMonthCalendar(int month, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.HOUR, 0);

		return calendar;
	}

	public static Date firstDayOfTheMonth(int month, int year) {
		return firstDayOfTheMonthCalendar(month, year).getTime();
	}

	public static Date lastDayOfTheMonth(int month, int year) {
		Calendar calendar = firstDayOfTheMonthCalendar(month, year);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.HOUR, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		return calendar.getTime();
	}
}
