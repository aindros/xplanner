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

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import xplanner.service.AuthenticationService;
import xplanner.ui.PageLayout;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public abstract class BaseController {
	private @Autowired AuthenticationService authenticationService;
	protected @Autowired MessageSource messageSource;

	protected void defaultModelAttributes(Model model,
	                                      HttpServletRequest request,
	                                      Locale locale)
			throws AuthenticationException {
		String requestURL = request.getRequestURL().toString();
		String footerMessage = messageSource.getMessage("footer.message", new String[] {"/xplanner/do/systemInfo"}, "", locale);

		model.addAttribute("loggedUsername",    SecurityHelper.getUserPrincipal(request).getName());
		model.addAttribute("requestURL",        requestURL);
		model.addAttribute("authenticatedUser", authenticationService.retrieveAuthenticatedUser(request));
		model.addAttribute("pageLayout",        new PageLayout());
		model.addAttribute("footerMessage",     footerMessage);

		XPlannerProperties properties = new XPlannerProperties();
		String version = properties.getProperty(XPlannerProperties.XPLANNER_VERSION_KEY);
		String revision = properties.getProperty(XPlannerProperties.XPLANNER_BUILD_REVISION_KEY);
		String date = properties.getProperty(XPlannerProperties.XPLANNER_BUILD_DATE_KEY);

		String versionLabel =
				messageSource.getMessage("app.label.version", new String[] {version, date, revision}, "", locale);
		model.addAttribute("versionLabel", versionLabel);
	}
}
