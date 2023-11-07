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

package xplanner.ui;

import lombok.*;
import net.sf.xplanner.domain.NamedObject;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.TimeEntry;
import org.springframework.context.MessageSource;
import xplanner.controller.BaseController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class BreadCrumbBuilder {
	private final MessageSource messageSource;
	private final Locale locale;
	private final List<Node> nodes = new ArrayList<>();

	public BreadCrumbBuilder(MessageSource messageSource, Locale locale) {
		this.messageSource = messageSource;
		this.locale = locale;
	}

	private BreadCrumbBuilder addNode(String label, String link) {
		nodes.add(new Node(label, link));

		return this;
	}

	public BreadCrumbBuilder toTop() {
		return addNode(messageSource.getMessage("navigation.top", null, "Home", locale), "/");
	}

	public BreadCrumbBuilder toProject(Project project) {
		return addNode(project.getName(), getUrl(project));
	}

	private String getUrl(NamedObject namedObject) {
		return BaseController.getContextUrl(namedObject.getClass()) + "/" + namedObject.getId();
	}

	public List<Node> build() {
		if (!nodes.isEmpty()) {
			nodes.get(nodes.size() - 1).setCurrentPage(true);
		}

		return new ArrayList<>(nodes);
	}

	public static class Node {
		private @Getter String label;
		private @Getter String link;
		private @Getter @Setter boolean currentPage = false;

		public Node(String label, String link) {
			this.label = label;
			this.link = link;
		}
	}
}
