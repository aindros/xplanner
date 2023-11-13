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

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.PrincipalSpecificPermissionHelper;
import lombok.Getter;
import net.sf.xplanner.domain.Permission;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring3.SpringTemplateEngine;
import xplanner.service.AuthenticationService;
import xplanner.ui.BreadCrumbBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

public abstract class BaseController {
	public static final Logger log = Logger.getLogger(BaseController.class);

	private @Autowired AuthenticationService authenticationService;
	protected @Autowired MessageSource messageSource;
	protected @Autowired PrincipalSpecificPermissionHelper principalSpecificPermissionHelper;

	private static final Map<String, String> CONTEXT_URLS = new HashMap<>();
	public static final String
			PROJECTS_URL = "/projects",
			PROJECT_TIMELOG_URL  = "/{id}/timelog";

	public static final String
			PROJECTS_KEY = "projects",
			PROJECT_TIMELOG_KEY = "projectTimelog";

	static {
		CONTEXT_URLS.put(PROJECTS_KEY,   PROJECTS_URL);
		CONTEXT_URLS.put(PROJECT_TIMELOG_KEY, PROJECTS_URL + PROJECT_TIMELOG_URL);
	}

	public static String getContextUrl(String key, Map<String, String> parameters) {
		String url = CONTEXT_URLS.get(key) == null? "" : CONTEXT_URLS.get(key);
		if (url.isEmpty()) return url;

		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			String placeHolder = "{" + entry.getKey() + "}";
			url = url.replace(placeHolder, entry.getValue());
		}

		return url;
	}

	protected Map<Integer, List<Permission>> getPermissions(HttpServletRequest request) throws AuthenticationException {
		return principalSpecificPermissionHelper.getPermissionsForPrincipal(SecurityHelper.getRemoteUserId(request));
	}

	protected void addBreadCrumbs(Model model, List<BreadCrumbBuilder.Node> breadCrumbs) {
		model.addAttribute("breadCrumbs", breadCrumbs);
	}

	protected void defaultModelAttributes(HttpServletRequest request,
	                                      Model model,
	                                      Locale locale,
	                                      ControllerData data)
			throws AuthenticationException {
		String requestURL = request.getRequestURL().toString();
		String footerMessage = messageSource.getMessage("footer.message", new String[] {"/xplanner/do/systemInfo"}, "", locale);

		model.addAttribute("loggedUsername",    SecurityHelper.getUserPrincipal(request).getName());
		model.addAttribute("requestURL",        requestURL);
		model.addAttribute("authenticatedUser", authenticationService.retrieveAuthenticatedUser(request));
		model.addAttribute("footerMessage",     footerMessage);

		XPlannerProperties properties = new XPlannerProperties();
		String version = properties.getProperty(XPlannerProperties.XPLANNER_VERSION_KEY);
		String revision = properties.getProperty(XPlannerProperties.XPLANNER_BUILD_REVISION_KEY);
		String date = properties.getProperty(XPlannerProperties.XPLANNER_BUILD_DATE_KEY);

		String versionLabel =
				messageSource.getMessage("app.label.version", new String[] {version, date, revision}, "", locale);
		model.addAttribute("versionLabel", versionLabel);

		data.getPermissions().putAll(getPermissions(request));
	}

	protected static class ControllerData {
		private final @Getter Map<Integer, List<Permission>> permissions = new HashMap<>();
	}

	protected @Autowired SpringTemplateEngine templateEngine;
	private final String TMP = "/tmp";

	public File exportToPdfBox(Map<String, Object> variables, String templatePath, String out, Locale locale) {
		if (out == null || out.isEmpty()) {
			int retries = 3;
			do {
				out = TMP + "/" + new Date().getTime() + ".pdf";
			} while (new File(out).exists() && retries-- > 0);
			if (retries == 0) {
				throw null;
			}
		}

		try (OutputStream os = new FileOutputStream(out);) {
			PdfRendererBuilder builder = new PdfRendererBuilder();
			builder.withHtmlContent(getHtmlString(variables, templatePath, locale), "file:");
			builder.toStream(os);
			builder.run();
		} catch (Exception e) {
			log.error("Exception while generating pdf: {}", e);
		}

		return new File(out);
	}

	private String getHtmlString(Map<String, Object> variables, String templatePath, Locale locale) {
		try {
			final Context context = new Context();
			context.setVariables(variables);
			context.setLocale(locale);

			return templateEngine.process(templatePath, context);
		} catch (Exception e) {
			log.error("Exception while getting html string from template engine: {}", e);
		}

		return null;
	}
}
