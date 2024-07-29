CREATE TABLE `comment` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `description` VARCHAR(255) NOT NULL,
    `likes` INTEGER NOT NULL,
    `parent_comment_id` BIGINT,
    CONSTRAINT `comment_fk` FOREIGN KEY(`parent_comment_id`) REFERENCES `comment`(`id`)
);