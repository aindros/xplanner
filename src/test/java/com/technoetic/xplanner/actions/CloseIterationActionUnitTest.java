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

package com.technoetic.xplanner.actions;

import com.technoetic.xplanner.charts.DataSampler;
import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.forms.IterationStatusEditorForm;
import com.technoetic.xplanner.history.HistorySupport;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.tx.CheckedExceptionHandlingTransactionTemplateMock;
import com.technoetic.xplanner.util.TimeGenerator;
import net.sf.xplanner.dao.impl.CommonDao;
import net.sf.xplanner.domain.Iteration;
import org.apache.struts.action.ActionForm;
import org.junit.Before;
import org.junit.Test;
import xplanner.env.UnitTestEnvironment;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CloseIterationActionUnitTest extends UnitTestEnvironment {
	private static final int ITERATION_ID = 1;

	private CloseIterationAction action;
	private CommonDao<Iteration> commonDao;

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() throws AuthenticationException {
		commonDao = mock(CommonDao.class);
		TimeGenerator timeGenerator = mock(TimeGenerator.class);

		action = spy(new CloseIterationAction());
		action.setTransactionTemplate(new CheckedExceptionHandlingTransactionTemplateMock());
		action.setCommonDao(commonDao);
		action.setDataSampler(mock(DataSampler.class));
		action.setHistorySupport(mock(HistorySupport.class));
		action.setTimeGenerator(timeGenerator);

		doReturn(0).when(action).getRemoteUserId(any(HttpServletRequest.class));
	}

	@Override
	protected ActionForm getSupportFrom() {
		return new IterationStatusEditorForm(String.valueOf(ITERATION_ID));
	}

	@Override
	protected List<Forward> getForwards() {
		return Arrays.asList(
				new Forward(AbstractAction.TYPE_KEY, Iteration.class.getName()),
				new Forward("view/projects",         "projects.jsp"),
				new Forward("onclose",               "/do/continue/unfinished/stories")
		);
	}

	@Test
	public void onActiveIteration_testExecute_thenGetInactiveIteration() throws Exception {
		Iteration iteration = new Iteration();
		iteration.setId(ITERATION_ID);
		iteration.setIterationStatus(IterationStatus.ACTIVE);

		doReturn(iteration).when(commonDao).getById(Iteration.class, iteration.getId());

		support.executeAction(action);

		assertEquals("wrong iteration status", IterationStatus.INACTIVE_KEY, iteration.getStatusKey());
	}
}