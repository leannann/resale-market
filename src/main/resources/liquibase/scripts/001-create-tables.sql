--liquibase formated sql

--changeset a.zhuravlev:1
CREATE TABLE users (
     id SERIAL PRIMARY KEY,
     email VARCHAR(30) NOT NULL UNIQUE,
     first_name VARCHAR(30) NOT NULL,
     last_name VARCHAR(30) NOT NULL,
     phone VARCHAR(20) NOT NULL,
     role VARCHAR(10),
     image VARCHAR(255),
     password VARCHAR(30)
);

--changeset a.zhuravlev:2
CREATE TABLE ads (
    id SERIAL PRIMARY KEY,
    author_id INTEGER NOT NULL,
    title VARCHAR(90) NOT NULL,
    price INTEGER NOT NULL,
    description VARCHAR(255) NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (author_id) REFERENCES users(id)
);

--changeset a.zhuravlev:3
CREATE TABLE comments (
    id SERIAL PRIMARY KEY,
    ad_id INTEGER NOT NULL,
    author_id INTEGER NOT NULL,
    text VARCHAR(255) NOT NULL,
    created_at BIGINT NOT NULL,
    FOREIGN KEY (ad_id) REFERENCES ads(id),
    FOREIGN KEY (author_id) REFERENCES users(id)
);