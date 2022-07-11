DROP TABLE IF EXISTS MEALS;
DROP TABLE IF EXISTS USER_ROLES;
DROP TABLE IF EXISTS USERS;
DROP SEQUENCE IF EXISTS GLOBAL_SEQ;

CREATE SEQUENCE GLOBAL_SEQ AS INTEGER START WITH 100000;

CREATE TABLE USERS
(
    ID               INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    NAME             VARCHAR(50)             NOT NULL,
    EMAIL            VARCHAR(100)            NOT NULL,
    PASSWORD         VARCHAR(100)            NOT NULL,
    REGISTERED       TIMESTAMP DEFAULT NOW() NOT NULL,
    CALORIES_PER_DAY INTEGER   DEFAULT 2000  NOT NULL,
    ENABLED          BOOLEAN   DEFAULT TRUE  NOT NULL
);

CREATE UNIQUE INDEX UNIQUE_EMAIL ON USERS (EMAIL);

CREATE TABLE USER_ROLES
(
    USER_ID INTEGER      NOT NULL,
    ROLE    VARCHAR(100) NOT NULL,
    CONSTRAINT USER_ROLE_UNIQUE UNIQUE (USER_ID, ROLE),
    FOREIGN KEY (USER_ID) REFERENCES USERS (ID) ON DELETE CASCADE
);

CREATE TABLE MEALS
(
    ID          INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    DESCRIPTION VARCHAR(100) NOT NULL,
    CALORIES    INTEGER      NOT NULL,
    DATE_TIME   TIMESTAMP    NOT NULL,
    USER_ID     INTEGER      NOT NULL,
    FOREIGN KEY (USER_ID) REFERENCES USERS (ID) ON DELETE CASCADE
);

CREATE UNIQUE INDEX DATE_TIME_INDEX ON MEALS (DATE_TIME);