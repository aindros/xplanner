INSERT INTO person (id, last_update, name, email, initials, userId, password) VALUES
	(1, '2005-12-07', 'sysadmin', 'no@reply.com', 'SYS', 'sysadmin', '1tGWp1Bdm02Sw4bD7/o0N2ao405Tf8kjxGBW/A==');

INSERT INTO object_type (id, last_update, description, name) VALUES
	(1, '2010-06-30 09:00:00', 'User',      'User'      ),
	(2, '2010-06-30 09:00:00', 'project',   'Project'   ),
	(3, '2010-06-30 09:00:00', 'iteration', 'Iteration' ),
	(4, '2010-06-30 09:00:00', 'userstory', 'UserStory' ),
	(5, '2010-06-30 09:00:00', 'task',      'Task'      ),
	(6, '2010-06-30 09:00:00', 'note',      'Note'      ),
	(7, '2010-06-30 09:00:00', 'setting',   'Setting'   );

INSERT INTO patches (system_name, patch_level, patch_date, patch_in_progress) VALUES
	('xplanner', '14', '2011-07-15 15:39:29', 'F');

INSERT INTO permission (id, principal, name, resource_type, resource_id) VALUES
	(1,  5, '%',               '%',                        0),
	(2,  3, 'create.project',  'system.project',           0),
	(3,  3, 'create.person',   'system.person',            0),
	(4,  4, 'create.project',  'system.project',           0),
	(5,  4, 'admin%',          '%',                        0),
	(6,  3, 'create%',         '%',                        0),
	(7,  3, 'edit%',           '%',                        0),
	(8,  3, 'integrate%',      '%',                        0),
	(9,  3, 'delete%',         '%',                        0),
	(10, 2, 'read%',           '%',                        0),
	(11, 3, 'delete',          'system.project.iteration', 0);

INSERT INTO person_role (role_id, person_id, project_id) VALUES (5, 1, 0);

INSERT INTO roles (id, role, lft, rgt) VALUES
	(1, 'viewer',   1, 8),
	(2, 'editor',   2, 7),
	(3, 'admin',    3, 6),
	(4, 'sysadmin', 4, 5);
