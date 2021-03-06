DROP TABLE IF EXISTS USER_ROLES;
DROP TABLE IF EXISTS USERS;
DROP SEQUENCE IF EXISTS GLOBAL_SEQ;

CREATE SEQUENCE GLOBAL_SEQ START 100000;

CREATE TABLE USERS
(
    ID               INTEGER PRIMARY KEY DEFAULT NEXTVAL('GLOBAL_SEQ'),
    NAME             VARCHAR(50)                       NOT NULL,
    EMAIL            VARCHAR(100)                      NOT NULL,
    PASSWORD         VARCHAR(100)                      NOT NULL,
    REGISTERED       TIMESTAMP           DEFAULT NOW() NOT NULL,
    CALORIES_PER_DAY INTEGER             DEFAULT 2000  NOT NULL,
    ENABLED          BOOL                DEFAULT TRUE  NOT NULL
);

CREATE UNIQUE INDEX UNIQUE_EMAIL ON USERS (EMAIL);

CREATE TABLE USER_ROLES
(
    USER_ID INTEGER      NOT NULL,
    ROLE    VARCHAR(100) NOT NULL,
    CONSTRAINT USER_ROLE_UNIQUE UNIQUE (USER_ID, ROLE),
    FOREIGN KEY (USER_ID) REFERENCES USERS (ID) ON DELETE CASCADE
);