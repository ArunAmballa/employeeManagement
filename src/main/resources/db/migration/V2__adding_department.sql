CREATE TABLE department
(
    id    BIGINT AUTO_INCREMENT NOT NULL,
    title VARCHAR(255)          NULL,
    CONSTRAINT pk_department PRIMARY KEY (id)
);
ALTER TABLE employee
    ADD department_id BIGINT NULL;
