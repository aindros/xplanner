package net.sf.xplanner.events;

import org.apache.struts.action.ActionForm;
import org.springframework.context.ApplicationEvent;

/**
*    XplannerPlus, agile planning software
*    @author Maksym. 
*    Copyright (C) 2009  Maksym Chyrkov
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>
* 	 
*/
@XplannerEvent
public class ObjectUpdated extends ApplicationEvent {
	private static final long serialVersionUID = -3793971624943472208L;
	private ActionForm newValue;

	public ActionForm getNewValue() {
		return newValue;
	}

	public ObjectUpdated(ActionForm newValue, EventSource source) {
		super(source);
		this.newValue = newValue;
	}

}
