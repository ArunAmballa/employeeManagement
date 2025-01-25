ALTER TABLE employee
    ADD CONSTRAINT FK_UNIQUE FOREIGN KEY (department_id) REFERENCES department (id);