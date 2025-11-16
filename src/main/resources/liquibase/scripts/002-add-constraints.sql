--liquibase formatted sql

--changeset a.zhuravlev:4
UPDATE users SET image = '/images/avatars/default_avatar.jpg' WHERE image IS NULL OR image = '';
UPDATE ads SET imageurl = '/images/ads/default_ad.jpg' WHERE imageurl IS NULL OR imageurl = '';

--changeset a.zhuravlev:5
ALTER TABLE ads RENAME COLUMN imageurl TO image_url;

--changeset a.zhuravlev:6
ALTER TABLE users RENAME COLUMN firstname TO first_name;

--changeset a.zhuravlev:7
ALTER TABLE users RENAME COLUMN lastname TO last_name;

--changeset a.zhuravlev:8
ALTER TABLE comments RENAME COLUMN createdat TO created_at;

