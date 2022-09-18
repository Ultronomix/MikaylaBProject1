CREATE TABLE ers_user_roles(
    role_id varchar PRIMARY KEY DEFAULT gen_random_uuid(),
    role varchar UNIQUE NOT NULL
);

CREATE TABLE ers_users(
    user_id varchar PRIMARY KEY DEFAULT gen_random_uuid(),
    given_name varchar NOT NULL,
    surname varchar NOT NULL,
    email varchar(255) NOT NULL UNIQUE,
    username varchar(25) NOT NULL UNIQUE CHECK (length(username) >= 4),
    password varchar NOT NULL CHECK (length(password) >= 8),
    is_active boolean,
    role_id varchar NOT NULL,

    FOREIGN KEY (role_id) REFERENCES ers_user_roles (role_id)
);

CREATE TABLE ers_reimbursement_statuses (
    status_id varchar PRIMARY KEY DEFAULT gen_random_uuid(),
    status varchar(25) UNIQUE NOT NULL
);

CREATE TABLE ers_reimbursement_types (
    type_id varchar PRIMARY KEY DEFAULT gen_random_uuid(),
    type varchar(25) UNIQUE NOT NULL
);

CREATE TABLE ers_reimbursements (
    reimb_id varchar PRIMARY KEY DEFAULT gen_random_uuid(),
    amount numeric(6,2) not null,
    submitted timestamp not null,
    resolved timestamp,
    description varchar(255) not null,
    payment_id bytea,
    author_id varchar not null,
    resolver_id varchar,
    status_id varchar not null,
    type_id varchar not null,

    FOREIGN KEY(author_id) REFERENCES ers_users (user_id),
    FOREIGN KEY(resolver_id) REFERENCES ers_users (user_id),
    FOREIGN KEY(status_id) REFERENCES ers_reimbursement_statuses (status_id),
    FOREIGN KEY(type_id) REFERENCES ers_reimbursement_types (type_id)
);
