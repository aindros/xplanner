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

package xplanner.ui.thymeleaf.dialect;

import org.springframework.context.MessageSource;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import xplanner.ui.thymeleaf.processor.TextProcessor;

import java.util.HashSet;
import java.util.Set;

public class XPlannerDialect extends AbstractProcessorDialect {
	private final MessageSource messageSource;
	public XPlannerDialect(MessageSource messageSource) {
		super("XPlanner Dialect",
		      "xplanner",
		      1000);

		this.messageSource = messageSource;
	}

	/*
	 * Initialize the dialect's processors.
	 *
	 * Note the dialect prefix is passed here because, although we set
	 * "xplanner" to be the dialect's prefix at the constructor, that
	 * only works as a default, and at engine configuration time the
	 * user might have chosen a different prefix to be used.
	 */
	@Override
	public Set<IProcessor> getProcessors(final String dialectPrefix) {
		final Set<IProcessor> processors = new HashSet<>();
		processors.add(new TextProcessor(dialectPrefix, messageSource));

		return processors;
	}
}
