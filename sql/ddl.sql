USE BOOKER;

CREATE TABLE `LOGIN` (
                         `login_uid` VARCHAR(255) NOT NULL,
                         `pw` VARCHAR(255) NOT NULL,
                         `email` VARCHAR(255) DEFAULT NULL,
                         `birth` DATE DEFAULT NULL,
                         PRIMARY KEY (`login_uid`)
);

CREATE TABLE `PROFILE` (
                           `profile_uid` INT UNSIGNED NOT NULL AUTO_INCREMENT,
                           `nickname` VARCHAR(255) NOT NULL,
                           `userimage_url` VARCHAR(255) DEFAULT NULL,
                           `userimage_name` VARCHAR(255) DEFAULT NULL,
                           `usermessage` VARCHAR(255) DEFAULT NULL,
                           `login_uid` VARCHAR(255) NOT NULL,
                           `count_followers` INT UNSIGNED NOT NULL,
                           `count_followings` INT UNSIGNED NOT NULL,
                           FOREIGN KEY (`login_uid`) REFERENCES `LOGIN`(`login_uid`),
                           PRIMARY KEY (`profile_uid`)
);

CREATE TABLE `BOOK_DETAILS` (
                                `isbn` VARCHAR(255) NOT NULL,
                                `book_title` VARCHAR(255) DEFAULT NULL,
                                `author` VARCHAR(255) DEFAULT NULL,
                                `publisher` VARCHAR(255) DEFAULT NULL,
                                `cover_image_url` VARCHAR(255) DEFAULT NULL,
                                PRIMARY KEY (`isbn`)
);

CREATE TABLE `USER_BOOKS` (
                              `book_uid` INT UNSIGNED NOT NULL AUTO_INCREMENT,
                              `isbn` VARCHAR(255) NOT NULL,
                              `profile_uid` INT UNSIGNED NOT NULL,
                              `read_status` TINYINT UNSIGNED DEFAULT NULL,
                              `sale_status` TINYINT UNSIGNED DEFAULT NULL,
                              FOREIGN KEY (`isbn`) REFERENCES `BOOK_DETAILS`(`isbn`),
                              FOREIGN KEY (`profile_uid`) REFERENCES `PROFILE`(`profile_uid`),
                              PRIMARY KEY (`book_uid`)
);


CREATE TABLE `DIRECTMESSAGE` (
                                 `message_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
                                 `mcheck` TINYINT DEFAULT NULL,
                                 `mtitle` LONGTEXT DEFAULT NULL,
                                 `mcontents` LONGTEXT DEFAULT NULL,
                                 `mdate` DATETIME DEFAULT NULL,
                                 `sender_uid` BIGINT NOT NULL,
                                 `recipient_uid` BIGINT NOT NULL,
                                 PRIMARY KEY (`message_id`)
);

CREATE TABLE `FOLLOW` (
                          `follow_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
                          `from_user_id` INT UNSIGNED NOT NULL,
                          `to_user_id` INT UNSIGNED NOT NULL,
                          FOREIGN KEY (`from_user_id`) REFERENCES `PROFILE`(`profile_uid`),
                          PRIMARY KEY (`follow_id`),
                          CONSTRAINT `follow_uk` UNIQUE (`from_user_id`, `to_user_id`)
);

CREATE TABLE `INTERESTS` (
                             `interest_uid` INT UNSIGNED NOT NULL AUTO_INCREMENT,
                             `interest_name` VARCHAR(255) NOT NULL,
                             `profile_uid` INT UNSIGNED NOT NULL,
                             FOREIGN KEY (`profile_uid`) REFERENCES `PROFILE`(`profile_uid`),
                             PRIMARY KEY (`interest_uid`)
);

CREATE TABLE `JOURNALS` (
                            `journal_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
                            `jtitle` LONGTEXT DEFAULT NULL,
                            `jcontents` LONGTEXT DEFAULT NULL,
                            `jdatetime` DATETIME DEFAULT NULL,
                            `jimage_url` VARCHAR(255) DEFAULT NULL,
                            `jimage_name` VARCHAR(255) DEFAULT NULL,
                            `book_uid` INT UNSIGNED DEFAULT NULL,
                            FOREIGN KEY (`book_uid`) REFERENCES `USER_BOOKS`(`book_uid`),
                            PRIMARY KEY (`journal_id`)
);

CREATE TABLE `LIBRARY_LIST` (
                                `lib_code` VARCHAR(255) NOT NULL,
                                `lib_name` VARCHAR(255) DEFAULT NULL,
                                PRIMARY KEY (`lib_code`)
);