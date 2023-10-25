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

package xplanner.config;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.security.Authenticator;
import com.technoetic.xplanner.security.AuthenticatorImpl;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class XPlannerConfiguration {
	private final XPlannerProperties properties = new XPlannerProperties();

	@Bean
	public Authenticator authenticator() {
		return new AuthenticatorImpl();
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

		messageSource.setBasename("classpath:messages/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	/**
	 * @see StartupApplicationListener
	 * @return Flyway bean
	 */
	@Bean
	public Flyway flyway() {
		String conncetionUrl = properties.getProperty("hibernate.connection.url");
		String username = properties.getProperty("hibernate.connection.username");
		String password = properties.getProperty("hibernate.connection.password");

		return Flyway.configure().dataSource(conncetionUrl, username, password).load();
	}
}
