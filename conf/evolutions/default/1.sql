# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table `adviser` (`user_id` BIGINT NOT NULL,`adviser_user_id` BIGINT NOT NULL,`status` INTEGER DEFAULT 0 NOT NULL,`token` VARCHAR(254));
alter table `adviser` add constraint `pk_on_adviser_user_id_adviser_user_id` primary key(`user_id`,`adviser_user_id`);
create table `config` (`name` VARCHAR(254) NOT NULL PRIMARY KEY,`value` VARCHAR(254) NOT NULL,`description` VARCHAR(254));
create table `contact` (`user_id` BIGINT NOT NULL,`contact_user_id` BIGINT NOT NULL,`status` INTEGER DEFAULT 0 NOT NULL,`token` VARCHAR(254));
alter table `contact` add constraint `pk_on_contact_user_id_contact_user_id` primary key(`user_id`,`contact_user_id`);
create table `document_tag` (`user_id` BIGINT NOT NULL,`user_tag_id` BIGINT NOT NULL,`document_id` BIGINT NOT NULL);
alter table `document_tag` add constraint `pk_on_document_tag_userid_user_tag_id_document_id` primary key(`user_id`,`user_tag_id`,`document_id`);
create table `document` (`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`user_id` BIGINT NOT NULL,`name` VARCHAR(254) NOT NULL,`document_type` INTEGER NOT NULL,`file_type` INTEGER NOT NULL,`file_name` VARCHAR(254) NOT NULL,`physical_name` VARCHAR(254) NOT NULL,`description` VARCHAR(254));
create table `domain` (`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`industry_id` BIGINT NOT NULL,`name` VARCHAR(254) NOT NULL,`code` VARCHAR(254) NOT NULL,`description` VARCHAR(254));
create unique index `idx_unique_on_skill_code` on `domain` (`code`);
create table `industry` (`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`code` VARCHAR(254) NOT NULL,`name` VARCHAR(254) NOT NULL,`description` VARCHAR(254));
create unique index `idx_unique_on_industry_code` on `industry` (`code`);
create table `membership` (`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`name` VARCHAR(254) NOT NULL,`description` VARCHAR(254));
create table `message_box` (`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`user_id` BIGINT NOT NULL,`message_box_type` INTEGER NOT NULL,`name` VARCHAR(254) NOT NULL);
create table `message_recipient` (`user_id` BIGINT NOT NULL,`message_id` BIGINT NOT NULL);
alter table `message_recipient` add constraint `pk_on_message_recipient` primary key(`user_id`,`message_id`);
create table `message` (`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`message_id` BIGINT,`sender_id` BIGINT,`subject` VARCHAR(254),`body` VARCHAR(254));
create table `skill` (`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`industry_id` BIGINT NOT NULL,`name` VARCHAR(254) NOT NULL,`code` VARCHAR(254) NOT NULL,`description` VARCHAR(254));
create unique index `idx_skill_on_code_unique` on `skill` (`code`);
create table `user_membership` (`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`user_id` BIGINT NOT NULL,`membership_id` BIGINT NOT NULL,`start_date` TIMESTAMP NOT NULL,`end_date` TIMESTAMP NULL,`active` BOOLEAN DEFAULT true NOT NULL,`description` VARCHAR(254));
create table `user_skill` (`user_id` BIGINT NOT NULL,`skill_id` BIGINT NOT NULL,`skill_level` INTEGER NOT NULL,`description_short` VARCHAR(254),`description_long` VARCHAR(254));
alter table `user_skill` add constraint `pk_on_user_skill_user_id_skill_id` primary key(`user_id`,`skill_id`);
create table `user_tag` (`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`user_id` BIGINT NOT NULL,`name` VARCHAR(254) NOT NULL);
alter table `user_tag` add constraint `pk_on_user_tag_id_user_id` primary key(`id`,`user_id`);
create table `user` (`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`first_name` VARCHAR(254) NOT NULL,`last_name` VARCHAR(254) NOT NULL,`email` VARCHAR(254) NOT NULL,`password` VARCHAR(254) NOT NULL);
create unique index `idx_user_on_email_unique` on `user` (`email`);
alter table `adviser` add constraint `fk_on_adviser_adviser_user_id` foreign key(`adviser_user_id`) references `user`(`id`) on update NO ACTION on delete NO ACTION;
alter table `contact` add constraint `fk_on_contact_contact_user_id` foreign key(`contact_user_id`) references `user`(`id`) on update NO ACTION on delete NO ACTION;
alter table `document` add constraint `fk_document_on_user_id` foreign key(`user_id`) references `user`(`id`) on update NO ACTION on delete NO ACTION;
alter table `domain` add constraint `fk_on_skill_industry_id` foreign key(`industry_id`) references `industry`(`id`) on update NO ACTION on delete NO ACTION;
alter table `message_box` add constraint `fk_on_message_box_user_id` foreign key(`user_id`) references `user`(`id`) on update NO ACTION on delete NO ACTION;
alter table `message_recipient` add constraint `fk_on_message_recipient_message_id` foreign key(`message_id`) references `message`(`id`) on update NO ACTION on delete NO ACTION;
alter table `message_recipient` add constraint `fk_on_message_recipient_user_id` foreign key(`user_id`) references `user`(`id`) on update NO ACTION on delete NO ACTION;
alter table `message` add constraint `fk_on_message_message_id` foreign key(`sender_id`) references `message`(`id`) on update NO ACTION on delete NO ACTION;
alter table `message` add constraint `fk_on_message_sender_id` foreign key(`sender_id`) references `user`(`id`) on update NO ACTION on delete NO ACTION;
alter table `skill` add constraint `fk_skill_on_industry_id` foreign key(`industry_id`) references `industry`(`id`) on update NO ACTION on delete NO ACTION;
alter table `user_membership` add constraint `fk_on_user_id` foreign key(`user_id`) references `user`(`id`) on update NO ACTION on delete NO ACTION;
alter table `user_membership` add constraint `fk_on_membership_id` foreign key(`membership_id`) references `membership`(`id`) on update NO ACTION on delete NO ACTION;
alter table `user_skill` add constraint `fk_on_user_skill_skill_id` foreign key(`skill_id`) references `skill`(`id`) on update NO ACTION on delete NO ACTION;
alter table `user_skill` add constraint `fk_on_user_skill_user_id` foreign key(`user_id`) references `user`(`id`) on update NO ACTION on delete NO ACTION;

# --- !Downs

ALTER TABLE adviser DROP FOREIGN KEY fk_on_adviser_adviser_user_id;
ALTER TABLE contact DROP FOREIGN KEY fk_on_contact_contact_user_id;
ALTER TABLE document DROP FOREIGN KEY fk_document_on_user_id;
ALTER TABLE domain DROP FOREIGN KEY fk_on_skill_industry_id;
ALTER TABLE message_box DROP FOREIGN KEY fk_on_message_box_user_id;
ALTER TABLE message_recipient DROP FOREIGN KEY fk_on_message_recipient_message_id;
ALTER TABLE message_recipient DROP FOREIGN KEY fk_on_message_recipient_user_id;
ALTER TABLE message DROP FOREIGN KEY fk_on_message_message_id;
ALTER TABLE message DROP FOREIGN KEY fk_on_message_sender_id;
ALTER TABLE skill DROP FOREIGN KEY fk_skill_on_industry_id;
ALTER TABLE user_membership DROP FOREIGN KEY fk_on_user_id;
ALTER TABLE user_membership DROP FOREIGN KEY fk_on_membership_id;
ALTER TABLE user_skill DROP FOREIGN KEY fk_on_user_skill_skill_id;
ALTER TABLE user_skill DROP FOREIGN KEY fk_on_user_skill_user_id;
ALTER TABLE adviser DROP PRIMARY KEY;
drop table `adviser`;
drop table `config`;
ALTER TABLE contact DROP PRIMARY KEY;
drop table `contact`;
ALTER TABLE document_tag DROP PRIMARY KEY;
drop table `document_tag`;
drop table `document`;
drop table `domain`;
drop table `industry`;
drop table `membership`;
drop table `message_box`;
ALTER TABLE message_recipient DROP PRIMARY KEY;
drop table `message_recipient`;
drop table `message`;
drop table `skill`;
drop table `user_membership`;
ALTER TABLE user_skill DROP PRIMARY KEY;
drop table `user_skill`;
ALTER TABLE user_tag DROP PRIMARY KEY;
drop table `user_tag`;
drop table `user`;

