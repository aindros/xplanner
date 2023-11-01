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

package net.sf.xplanner.dao.impl;

import net.sf.xplanner.dao.ProjectDao;
import net.sf.xplanner.domain.Project;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-text.xml"})
public class ProjectDaoImplTest {
	
	@Autowired
	private ProjectDao projectDao;

	private Project newProject(String name, String description) {
		Project project = new Project();
		project.setName(name);
		project.setDescription(description);

		return project;
	}
	
	@Test
	@Transactional
	public void testSaveAndGet(){
		Project project1 = newProject( "First project",  "Description of the first project"  );
		Project project5 = newProject( "Second project", "Description of the second project" );

		int id1 = projectDao.save(project1);
		int id2 = projectDao.save(project5);

		assertNotEquals(0,   id1);
		assertNotEquals(id1, id2);
		
		Project project = projectDao.getById(id1);
		assertEquals(project1, project);
		assertEquals(project1.getName(), project.getName());
		assertEquals(project1.getDescription(), project.getDescription());
	}
}