CREATE TABLE time_entry_type (
	id INT NOT NULL DEFAULT '0',
	last_update TIMESTAMP DEFAULT NULL,
	name varchar(255) DEFAULT NULL,
	description TEXT,
	PRIMARY KEY (id)
);

ALTER TABLE time_entry ADD COLUMN time_entry_type_id INT;
ALTER TABLE time_entry ADD CONSTRAINT fk_time_entry_time_entry_type FOREIGN KEY (time_entry_type_id) REFERENCES time_entry_type (id) ON DELETE NO ACTION ON UPDATE NO ACTION;
