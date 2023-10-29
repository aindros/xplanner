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

package xplanner.controller;

import com.technoetic.xplanner.security.AuthenticationException;
import net.sf.xplanner.domain.Person;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import xplanner.ThymeLeafTemplate;
import xplanner.service.AuthenticationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/login")
public class LoginController {
	private final Logger log = Logger.getLogger(getClass());

	private @Autowired AuthenticationService authenticationService;
	private @Autowired MessageSource messageSource;

	@RequestMapping(method = GET)
	public String doViewLogin(ModelMap model) {
		model.addAttribute("user", new Person());

		return ThymeLeafTemplate.LOGIN.pageName;
	}

	@RequestMapping(method = POST)
	public String doPostLogin(@ModelAttribute("user") Person user,
	                          Model model,
	                          HttpServletRequest servletRequest,
	                          HttpServletResponse servletResponse,
	                          Locale locale) {
		try {
			authenticationService.authenticate(user, servletRequest, servletResponse);
		} catch (AuthenticationException ex) {
			/* Using message since text will be formatted slightly differently than the normal "error". */
			log.warn(ex.getMessage() + ": " + ex.getCause());

			Collection<String> errors = new ArrayList<>();
			errors.add(messageSource.getMessage("login.failed", null, "login.failed", locale));

			Map<?, ?> errorMap = ex.getErrorsByModule();
			for (Object key : errorMap.keySet()) {
				String moduleName = (String) key;
				String message = (String) errorMap.get(moduleName);
				errors.add(moduleName + " &mdash; " + messageSource.getMessage(message, new Object[] { user.getUserId() }, message, locale));
			}

			model.addAttribute("errorMessages", errors);

			return ThymeLeafTemplate.LOGIN.pageName;
		}

		return ThymeLeafTemplate.ROOT.redirectUrl;
	}
}
