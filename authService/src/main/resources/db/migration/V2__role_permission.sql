CREATE TABLE permission (
    id BIGSERIAL PRIMARY KEY ,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE role_permission (
    role_id BIGINT NOT NULL ,
    permission_id BIGINT NOT NULL ,
    PRIMARY KEY (role_id,permission_id),
    CONSTRAINT fk_role_permission_role FOREIGN KEY (role_id) REFERENCES roles(id),
    CONSTRAINT fk_role_permission_permission FOREIGN KEY (permission_id) REFERENCES permission(id)
);

INSERT INTO roles(name) VALUES
                            ('ROLE ADMIN'),
                            ('ROLE CUSTOMER'),
                            ('ROLE BANKER');

INSERT INTO permission(name) VALUES
                                 ('CREATE_ACCOUNT'),
                                 ('VIEW_ACCOUNT'),
                                 ('TRANSFER_MONEY'),
                                 ('APPROVE_TRANSACTION');