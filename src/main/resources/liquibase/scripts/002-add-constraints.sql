--liquibase formatted sql

--changeset a.zhuravlev:1
ALTER TABLE ads
    ADD CONSTRAINT fk_ads_author
    FOREIGN KEY (author_id)
    REFERENCES users(id);

--changeset a.zhuravlev:2
ALTER TABLE comments
    ADD CONSTRAINT fk_comments_ad
    FOREIGN KEY (ad_id)
    REFERENCES ads(id);

--changeset a.zhuravlev:3
ALTER TABLE comments
    ADD CONSTRAINT fk_comments_author
    FOREIGN KEY (author_id)
    REFERENCES users(id);