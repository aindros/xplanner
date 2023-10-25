CREATE TABLE attribute (
  targetId INT NOT NULL,
  name varchar(255) NOT NULL,
  value varchar(255) DEFAULT NULL,
  PRIMARY KEY (targetId,name)
);

CREATE TABLE datasample (
  sampleTime TIMESTAMP NOT NULL,
  referenceId INT NOT NULL,
  aspect varchar(255) NOT NULL,
  value DECIMAL(15, 2) DEFAULT NULL,
  PRIMARY KEY (sampleTime,referenceId,aspect)
);

CREATE TABLE history (
  id INT NOT NULL,
  when_happened TIMESTAMP DEFAULT NULL,
  container_id INT DEFAULT NULL,
  target_id INT DEFAULT NULL,
  object_type varchar(255) DEFAULT NULL,
  action varchar(255) DEFAULT NULL,
  description varchar(255) DEFAULT NULL,
  person_id INT DEFAULT NULL,
  notified BOOLEAN DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE identifier (
  nextId INT NOT NULL DEFAULT '1'
);

CREATE TABLE integration (
  id INT NOT NULL DEFAULT '0',
  project_id INT DEFAULT NULL,
  last_update TIMESTAMP DEFAULT NULL,
  person_id INT DEFAULT NULL,
  when_started TIMESTAMP DEFAULT NULL,
  when_requested TIMESTAMP DEFAULT NULL,
  when_complete TIMESTAMP DEFAULT NULL,
  state char(1) DEFAULT NULL,
  comments varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE iteration (
  id INT NOT NULL DEFAULT '0',
  last_update TIMESTAMP DEFAULT NULL,
  project_id INT DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  description TEXT,
  start_date date DEFAULT NULL,
  end_date date DEFAULT NULL,
  status smallINT DEFAULT NULL,
  days_worked DECIMAL(15, 2) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE note (
  id INT NOT NULL DEFAULT '0',
  attachedTo_id INT DEFAULT NULL,
  author_id INT DEFAULT NULL,
  subject varchar(255) DEFAULT NULL,
  body TEXT,
  submission_time TIMESTAMP DEFAULT NULL,
  last_update TIMESTAMP DEFAULT NULL,
  attachment_id INT DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE notification_receivers (
  project_id INT NOT NULL,
  person_id INT NOT NULL,
  PRIMARY KEY (project_id,person_id)
);

CREATE TABLE object_type (
  id INT NOT NULL,
  last_update TIMESTAMP DEFAULT NULL,
  description varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE patches (
  system_name varchar(30) NOT NULL,
  patch_level INT NOT NULL,
  patch_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  patch_in_progress char(1) NOT NULL DEFAULT 'F',
  PRIMARY KEY (system_name)
);

CREATE TABLE permission (
  id INT NOT NULL,
  principal INT DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  resource_type varchar(255) DEFAULT NULL,
  resource_id INT DEFAULT NULL,
  positive BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (id)
);

CREATE TABLE person (
  id INT NOT NULL,
  last_update TIMESTAMP DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  email varchar(255) DEFAULT NULL,
  phone varchar(255) DEFAULT NULL,
  initials varchar(255) DEFAULT NULL,
  userId varchar(255) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  is_hidden BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (id)
);

CREATE TABLE person_role (
  role_id INT NOT NULL,
  person_id INT NOT NULL,
  project_id INT NOT NULL,
  PRIMARY KEY (role_id,person_id,project_id)
);

CREATE TABLE project (
  id INT NOT NULL DEFAULT '0',
  last_update TIMESTAMP DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  description TEXT,
  is_hidden BOOLEAN DEFAULT NULL,
  backlog_id INT DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE roles (
  id INT NOT NULL,
  role varchar(255) DEFAULT NULL,
  lft INT DEFAULT NULL,
  rgt INT DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE setting (
  id INT NOT NULL,
  last_update TIMESTAMP DEFAULT NULL,
  description varchar(4096) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  category varchar(255) DEFAULT NULL,
  defaultValue varchar(255) DEFAULT NULL,
  setting_scope INT DEFAULT NULL,
  objectType_id INT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE story (
  id INT NOT NULL DEFAULT '0',
  last_update TIMESTAMP DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  description TEXT,
  iteration_id INT DEFAULT NULL,
  tracker_id INT DEFAULT NULL,
  estimated_hours DECIMAL(15, 2) DEFAULT NULL,
  priority INT DEFAULT NULL,
  customer_id INT DEFAULT NULL,
  status char(1) NOT NULL DEFAULT 'd',
  original_estimated_hours DECIMAL(15, 2) DEFAULT NULL,
  disposition char(1) NOT NULL DEFAULT 'p',
  postponed_hours DECIMAL(15, 2) DEFAULT '0',
  it_start_estimated_hours DECIMAL(15, 2) DEFAULT NULL,
  orderNo INT DEFAULT '0',
  PRIMARY KEY (id)
);

CREATE TABLE task (
  id INT NOT NULL DEFAULT '0',
  last_update TIMESTAMP DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  type varchar(255) DEFAULT NULL,
  description TEXT,
  acceptor_id INT DEFAULT NULL,
  created_date date DEFAULT NULL,
  estimated_hours DECIMAL(15, 2) DEFAULT NULL,
  original_estimate DECIMAL(15, 2) DEFAULT NULL,
  is_complete BOOLEAN DEFAULT NULL,
  story_id INT NOT NULL DEFAULT '0',
  disposition char(1) NOT NULL DEFAULT 'p',
  PRIMARY KEY (id)
);

CREATE TABLE time_entry (
  id INT NOT NULL DEFAULT '0',
  last_update TIMESTAMP DEFAULT NULL,
  start_time TIMESTAMP DEFAULT NULL,
  end_time TIMESTAMP DEFAULT NULL,
  duration DECIMAL(15, 2) DEFAULT NULL,
  person1_id INT DEFAULT NULL,
  person2_id INT DEFAULT NULL,
  task_id INT DEFAULT NULL,
  report_date TIMESTAMP DEFAULT NULL,
  description TEXT,
  PRIMARY KEY (id)
);

CREATE TABLE xdir (
  id INT NOT NULL,
  name varchar(255) DEFAULT NULL,
  parent_id INT DEFAULT NULL,
  last_update TIMESTAMP DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE xfile (
  id INT NOT NULL,
  name varchar(255) DEFAULT NULL,
  content_type varchar(255) DEFAULT NULL,
  data bytea,
  file_size bigINT DEFAULT NULL,
  dir_id INT DEFAULT NULL,
  last_update TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

CREATE UNIQUE INDEX uk_person_user_id ON person (userId);
CREATE UNIQUE INDEX uk_roles_role ON roles (role);

ALTER TABLE iteration ADD CONSTRAINT fk_iteration_project FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE note ADD CONSTRAINT fk_note_xfile FOREIGN KEY (attachment_id) REFERENCES xfile (id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE notification_receivers ADD CONSTRAINT fk_notification_receivers_person FOREIGN KEY (person_id) REFERENCES person (id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE notification_receivers ADD CONSTRAINT fk_notification_receivers_project FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE story ADD CONSTRAINT fk_story_person FOREIGN KEY (customer_id) REFERENCES person (id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE story ADD CONSTRAINT fk_story_iteration FOREIGN KEY (iteration_id) REFERENCES iteration (id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE task ADD CONSTRAINT fk_task_story FOREIGN KEY (story_id) REFERENCES story (id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE time_entry ADD CONSTRAINT fk_time_entry_task FOREIGN KEY (task_id) REFERENCES task (id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE xdir ADD CONSTRAINT fk_xdir_xdir FOREIGN KEY (parent_id) REFERENCES xdir (id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE xfile ADD CONSTRAINT fk_xfile_xdir FOREIGN KEY (dir_id) REFERENCES xdir (id) ON DELETE NO ACTION ON UPDATE NO ACTION;
