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

package xplanner.service;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.Authenticator;
import com.technoetic.xplanner.security.CredentialCookie;
import com.technoetic.xplanner.security.SecurityHelper;
import net.sf.xplanner.domain.Person;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AuthenticationService {
	private final Logger log = Logger.getLogger(getClass());

	private @Autowired Authenticator authenticator;

	public void authenticate(Person user,
	                         HttpServletRequest httpServletRequest,
	                         HttpServletResponse httpServletResponse) throws AuthenticationException {
		String userId = user.getUserId();
		String password = user.getPassword();

		authenticator.authenticate(httpServletRequest, userId, password);

		if (StringUtils.equals(httpServletRequest.getParameter("remember"), "Y")) {
			CredentialCookie credentials = new CredentialCookie(httpServletRequest, httpServletResponse);
			credentials.set(userId, password);
		}
	}

	public void logout(HttpServletRequest httpServletRequest,
	                   HttpServletResponse httpServletResponse) throws AuthenticationException {
		authenticator.logout(httpServletRequest, SecurityHelper.getRemoteUserId(httpServletRequest));
		new CredentialCookie(httpServletRequest, httpServletResponse).remove();
	}
}
