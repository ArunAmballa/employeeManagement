CREATE TABLE employee
(
    id     BIGINT AUTO_INCREMENT NOT NULL,
    name   VARCHAR(255)          NULL,
    email  VARCHAR(255)          NULL,
    age    INT                   NOT NULL,
    salary DOUBLE                NULL,
    CONSTRAINT pk_employee PRIMARY KEY (id)
);