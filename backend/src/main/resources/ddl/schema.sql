
/* Drop Tables */

DROP TABLE ADDRESS IF EXISTS ;
DROP TABLE EMAIL IF EXISTS ;
DROP TABLE MEMBERSHIP IF EXISTS ;
DROP TABLE GRP IF EXISTS ;
DROP TABLE USR IF EXISTS ;




/* Create Tables */

-- 住所
CREATE TABLE ADDRESS
(
    -- ユーザID
    USER_ID bigint NOT NULL,
    -- 郵便番号
    ZIP_CODE varchar(8),
    -- 住所
    ADDRESS varchar(512),
    -- バージョン
    VER int,
    -- 最終更新日時
    LAST_UPDATED_AT timestamp,
    PRIMARY KEY (USER_ID)
);


-- メール
CREATE TABLE EMAIL
(
    -- ユーザID
    USER_ID bigint NOT NULL,
    -- メール番号
    EMAIL_NO bigint NOT NULL,
    -- メールアドレス
    EMAIL varchar(256),
    -- バージョン
    VER int,
    -- 最終更新日時
    LAST_UPDATED_AT timestamp,
    PRIMARY KEY (USER_ID, EMAIL_NO)
);


-- グループ
CREATE TABLE GRP
(
    -- グループID
    GROUP_ID bigint NOT NULL,
    -- グループ名
    GROUP_NAME varchar(512),
    -- バージョン
    VER int,
    -- 最終更新日時
    LAST_UPDATED_AT timestamp,
    PRIMARY KEY (GROUP_ID)
);


-- 所属
CREATE TABLE MEMBERSHIP
(
    -- ユーザID
    USER_ID bigint NOT NULL,
    -- グループID
    GROUP_ID bigint NOT NULL,
    -- バージョン
    VER int,
    -- 最終更新日時
    LAST_UPDATED_AT timestamp,
    PRIMARY KEY (USER_ID, GROUP_ID)
);


-- ユーザ
CREATE TABLE USR
(
    -- ユーザID
    USER_ID bigint NOT NULL,
    -- 名前
    FIRST_NAME varchar(512),
    -- 苗字
    FAMILY_NAME varchar(512),
    -- ログインID
    LOGIN_ID varchar(32) UNIQUE,
    -- ログイン中
    IS_LOGIN boolean,
    -- バージョン
    VER int,
    -- 最終更新日時
    LAST_UPDATED_AT timestamp,
    PRIMARY KEY (USER_ID)
);



/* Create Foreign Keys */

ALTER TABLE MEMBERSHIP
    ADD FOREIGN KEY (GROUP_ID)
        REFERENCES GRP (GROUP_ID)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
;


ALTER TABLE ADDRESS
    ADD FOREIGN KEY (USER_ID)
        REFERENCES USR (USER_ID)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
;


ALTER TABLE EMAIL
    ADD FOREIGN KEY (USER_ID)
        REFERENCES USR (USER_ID)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
;


ALTER TABLE MEMBERSHIP
    ADD FOREIGN KEY (USER_ID)
        REFERENCES USR (USER_ID)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
;


