# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table `company_user` (`company_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`first_name` VARCHAR(254) NOT NULL,`middle_name` VARCHAR(254),`last_name` VARCHAR(254) NOT NULL,`email` VARCHAR(254) NOT NULL,`status` INTEGER NOT NULL,`user_type` INTEGER NOT NULL,`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
create unique index `idx_company_user_on_email_unique` on `company_user` (`company_id`,`email`);
create table `company` (`company_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`name` VARCHAR(254) NOT NULL,`status` INTEGER NOT NULL,`address` VARCHAR(254) NOT NULL,`email` VARCHAR(254) NOT NULL,`website` VARCHAR(254),`telephone` VARCHAR(254) NOT NULL,`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
create unique index `idx_company_on_name_unique` on `company` (`name`);
create table `contact` (`user_id` BIGINT NOT NULL,`friend_id` BIGINT NOT NULL,`status` INTEGER DEFAULT 1 NOT NULL,`token` VARCHAR(254),`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
alter table `contact` add constraint `pk_on_contact_user_id_friend_id` primary key(`user_id`,`friend_id`);
create table `country` (`country_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`code` VARCHAR(254) NOT NULL,`name` VARCHAR(254) NOT NULL,`active` BOOLEAN NOT NULL);
create unique index `idx_country_on_code_unique` on `country` (`code`);
create table `document_folder` (`document_folder_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`name` VARCHAR(254) NOT NULL,`default` BOOLEAN NOT NULL,`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
create table `document_search` (`document_search_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`user_id` BIGINT NOT NULL,`name` VARCHAR(254) NOT NULL,`search_text` VARCHAR(254) NOT NULL,`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
create table `document_tag` (`user_id` BIGINT NOT NULL,`user_tag_id` BIGINT NOT NULL,`document_id` BIGINT NOT NULL,`user_document_id` BIGINT NOT NULL,`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
create table `document` (`document_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`document_folder_id` BIGINT NOT NULL,`name` VARCHAR(254) NOT NULL,`document_type` INTEGER NOT NULL,`file_type` INTEGER NOT NULL,`file_name` VARCHAR(254) NOT NULL,`physical_name` VARCHAR(254) NOT NULL,`description` VARCHAR(254) NOT NULL,`signature` VARCHAR(254) NOT NULL,`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
create table `domain` (`domain_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`industry_id` BIGINT NOT NULL,`name` VARCHAR(254) NOT NULL,`description` VARCHAR(254) NOT NULL,`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
create unique index `idx_unique_on_skill_name` on `domain` (`name`);
create table `follower` (`subject_id` BIGINT NOT NULL,`follower_id` BIGINT NOT NULL,`status` INTEGER DEFAULT 1 NOT NULL,`token` VARCHAR(254),`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
alter table `follower` add constraint `pk_on_follower_subject_id_follower_id` primary key(`subject_id`,`follower_id`);
create table `industry` (`industry_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`name` VARCHAR(254) NOT NULL,`description` VARCHAR(254) NOT NULL,`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
create unique index `idx_unique_on_industry_name` on `industry` (`name`);
create table `job_requirement_xtn` (`job_requirement_xtn_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`target_start_date` TIMESTAMP NULL,`target_end_date` TIMESTAMP NULL,`salary_type` INTEGER NOT NULL,`salary_min` DOUBLE,`salary_max` DOUBLE,`currency` INTEGER NOT NULL,`tax_term` INTEGER NOT NULL,`payment_term` INTEGER NOT NULL,`background_check` INTEGER NOT NULL,`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
create table `job_requirement` (`job_requirement_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`company_id` BIGINT NOT NULL,`code` VARCHAR(254) NOT NULL,`ref_number` VARCHAR(254),`title` VARCHAR(254) NOT NULL,`employment_type` INTEGER NOT NULL,`industry_id` BIGINT NOT NULL,`location` VARCHAR(254) NOT NULL,`description` TEXT NOT NULL,`status` INTEGER NOT NULL,`positions` INTEGER NOT NULL,`job_title_id` BIGINT NOT NULL,`job_requirement_xtn_id` BIGINT NOT NULL,`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
create unique index `idx_job_requirement_on_code_unique` on `job_requirement` (`company_id`,`code`);
create table `job_title` (`job_title_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`company_id` BIGINT NOT NULL,`name` VARCHAR(254) NOT NULL,`description` VARCHAR(254) NOT NULL,`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
create unique index `idx_job_title_on_name_unique` on `job_title` (`company_id`,`name`);
create table `message_box` (`message_box_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`user_id` BIGINT NOT NULL,`message_box_type` INTEGER NOT NULL,`name` VARCHAR(254) NOT NULL,`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
create table `message_recipient` (`user_id` BIGINT NOT NULL,`message_id` BIGINT NOT NULL,`read` BOOLEAN NOT NULL,`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
alter table `message_recipient` add constraint `pk_on_message_recipient` primary key(`user_id`,`message_id`);
create table `message` (`message_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`parent_message_id` BIGINT,`sender_user_id` BIGINT NOT NULL,`editable` BOOLEAN DEFAULT true NOT NULL,`subject` VARCHAR(254) NOT NULL,`body` TEXT,`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
create table `skill` (`skill_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`industry_id` BIGINT NOT NULL,`name` VARCHAR(254) NOT NULL,`description` VARCHAR(254) NOT NULL,`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
create unique index `idx_skill_on_name_unique` on `skill` (`name`);
create table `state` (`state_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`code` VARCHAR(254) NOT NULL,`name` VARCHAR(254) NOT NULL,`country_id` BIGINT NOT NULL);
create unique index `idx_state_on_code_unique` on `state` (`country_id`,`code`);
create table `user_document_folder` (`user_document_folder_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`document_folder_id` BIGINT NOT NULL,`user_id` BIGINT NOT NULL,`ownership_type` INTEGER NOT NULL,`can_copy` BOOLEAN DEFAULT true NOT NULL,`is_limited_share` BOOLEAN DEFAULT false NOT NULL,`share_until_eod` TIMESTAMP NULL,`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
create table `user_document` (`user_document_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`user_id` BIGINT NOT NULL,`document_id` BIGINT NOT NULL,`ownership_type` INTEGER NOT NULL,`can_copy` BOOLEAN DEFAULT false NOT NULL,`can_share` BOOLEAN DEFAULT true NOT NULL,`can_view` BOOLEAN DEFAULT true NOT NULL,`important` BOOLEAN DEFAULT false NOT NULL,`star` BOOLEAN DEFAULT false NOT NULL,`is_limited_share` BOOLEAN DEFAULT true NOT NULL,`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`share_until_eod` TIMESTAMP NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
create table `user_message` (`user_id` BIGINT NOT NULL,`message_id` BIGINT NOT NULL,`message_box_id` BIGINT NOT NULL,`read` BOOLEAN DEFAULT false NOT NULL,`replied` BOOLEAN DEFAULT false NOT NULL,`important` BOOLEAN DEFAULT false NOT NULL,`star` BOOLEAN DEFAULT false NOT NULL,`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
alter table `user_message` add constraint `pk_on_user_message` primary key(`user_id`,`message_id`,`message_box_id`);
create table `user_profile_personal` (`user_profile_personal_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`xray_terms` VARCHAR(254) NOT NULL,`aboutMe` VARCHAR(254),`picture_file` VARCHAR(254),`physical_file` VARCHAR(254),`mobile` VARCHAR(254),`alternate_email` VARCHAR(254),`gender` INTEGER,`marital_status` INTEGER,`birth_year` INTEGER,`birth_day` INTEGER,`birth_month` INTEGER,`created_at` TIMESTAMP NOT NULL,`updated_at` TIMESTAMP NULL);
create table `user_skill` (`user_id` BIGINT NOT NULL,`skill_id` BIGINT NOT NULL,`skill_level` INTEGER NOT NULL,`description_short` VARCHAR(254),`description_long` VARCHAR(254),`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
alter table `user_skill` add constraint `pk_on_user_skill_user_id_skill_id` primary key(`user_id`,`skill_id`);
create table `user_tag` (`user_tag_Id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`user_id` BIGINT NOT NULL,`name` VARCHAR(254) NOT NULL,`created_user_id` BIGINT NOT NULL,`created_at` TIMESTAMP NOT NULL,`updated_user_id` BIGINT,`updated_at` TIMESTAMP NULL);
create table `user` (`user_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`first_name` VARCHAR(254) NOT NULL,`last_name` VARCHAR(254) NOT NULL,`email` VARCHAR(254) NOT NULL,`password` VARCHAR(254) NOT NULL,`country_id` BIGINT NOT NULL,`activationToken` VARCHAR(254) NOT NULL,`verified` BOOLEAN NOT NULL,`last_logon` TIMESTAMP NOT NULL,`user_profile_personal_id` BIGINT,`otp` VARCHAR(254),`otp_expired_at` TIMESTAMP NULL,`created_at` TIMESTAMP NOT NULL,`updated_at` TIMESTAMP NULL);
create unique index `idx_user_on_email_unique` on `user` (`email`);
alter table `company_user` add constraint `fk_company_user_company_id` foreign key(`company_id`) references `company`(`company_id`) on update NO ACTION on delete NO ACTION;
alter table `company_user` add constraint `fk_company_user_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `company_user` add constraint `fk_company_user_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `company` add constraint `fk_company_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `company` add constraint `fk_company_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `contact` add constraint `fk_on_contact_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `contact` add constraint `fk_on_contact_friend_id` foreign key(`friend_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `contact` add constraint `fk_on_contact_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `document_folder` add constraint `fk_on_document_folder_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `document_folder` add constraint `fk_on_document_folder_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `document_search` add constraint `fk_on_document_search_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `document_search` add constraint `fk_on_document_search_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `document_search` add constraint `fk_on_document_search_user_id` foreign key(`user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `document_tag` add constraint `fk_on_document_tag_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `document_tag` add constraint `fk_on_document_tag_document_id` foreign key(`document_id`) references `document`(`document_id`) on update NO ACTION on delete NO ACTION;
alter table `document_tag` add constraint `fk_on_document_tag_tag_id` foreign key(`user_tag_id`) references `user_tag`(`user_tag_Id`) on update NO ACTION on delete NO ACTION;
alter table `document_tag` add constraint `fk_on_document_tag_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `document_tag` add constraint `fk_on_document_tag_user_document_id` foreign key(`user_document_id`) references `user_document`(`user_document_id`) on update NO ACTION on delete NO ACTION;
alter table `document_tag` add constraint `fk_on_document_tag_user_id` foreign key(`user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `document` add constraint `fk_document_on_document_folder_id` foreign key(`document_folder_id`) references `document_folder`(`document_folder_id`) on update NO ACTION on delete NO ACTION;
alter table `document` add constraint `fk_on_document_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `document` add constraint `fk_on_document_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `domain` add constraint `fk_on_domain_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `domain` add constraint `fk_on_domain_industry_id` foreign key(`industry_id`) references `industry`(`industry_id`) on update NO ACTION on delete NO ACTION;
alter table `domain` add constraint `fk_on_domain_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `follower` add constraint `fk_on_follower_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `follower` add constraint `fk_on_follower_follower_id` foreign key(`follower_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `follower` add constraint `fk_on_follower_subject_id` foreign key(`subject_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `follower` add constraint `fk_on_follower_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `industry` add constraint `fk_on_industry_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `industry` add constraint `fk_on_industry_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `job_requirement_xtn` add constraint `fk_job_requirement_xtn_on_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `job_requirement_xtn` add constraint `fk_job_requirement_xtn_on_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `job_requirement` add constraint `fk_job_requirement_jon_on_title_id` foreign key(`job_title_id`) references `job_title`(`job_title_id`) on update NO ACTION on delete NO ACTION;
alter table `job_requirement` add constraint `fk_job_requirement_on_company_id` foreign key(`company_id`) references `company`(`company_id`) on update NO ACTION on delete NO ACTION;
alter table `job_requirement` add constraint `fk_job_requirement_on_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `job_requirement` add constraint `fk_job_requirement_on_industry_id` foreign key(`industry_id`) references `industry`(`industry_id`) on update NO ACTION on delete NO ACTION;
alter table `job_requirement` add constraint `fk_job_requirement_on_job_requirement_xtn_id` foreign key(`job_requirement_xtn_id`) references `job_requirement_xtn`(`job_requirement_xtn_id`) on update NO ACTION on delete NO ACTION;
alter table `job_requirement` add constraint `fk_job_requirement_on_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `job_title` add constraint `fk_job_title_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `job_title` add constraint `fk_job_title_on_company_id` foreign key(`company_id`) references `company`(`company_id`) on update NO ACTION on delete NO ACTION;
alter table `job_title` add constraint `fk_job_title_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `message_box` add constraint `fk_on_message_box_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `message_box` add constraint `fk_on_message_box_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `message_box` add constraint `fk_on_message_box_user_id` foreign key(`user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `message_recipient` add constraint `fk_on_message_recipient_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `message_recipient` add constraint `fk_on_message_recipient_message_id` foreign key(`message_id`) references `message`(`message_id`) on update NO ACTION on delete NO ACTION;
alter table `message_recipient` add constraint `fk_on_message_recipient_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `message_recipient` add constraint `fk_on_message_recipient_user_id` foreign key(`user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `message` add constraint `fk_on_message_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `message` add constraint `fk_on_message_message_id` foreign key(`parent_message_id`) references `message`(`message_id`) on update NO ACTION on delete NO ACTION;
alter table `message` add constraint `fk_on_message_sender_user_id` foreign key(`sender_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `message` add constraint `fk_on_message_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `skill` add constraint `fk_on_skill_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `skill` add constraint `fk_on_skill_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `skill` add constraint `fk_skill_on_industry_id` foreign key(`industry_id`) references `industry`(`industry_id`) on update NO ACTION on delete NO ACTION;
alter table `state` add constraint `fk_on_states_country_id` foreign key(`country_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `user_document_folder` add constraint `fk_on_user_document_folder_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `user_document_folder` add constraint `fk_on_user_document_folder_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `user_document_folder` add constraint `fk_user_document_folder_on_document_folder_id` foreign key(`document_folder_id`) references `document_folder`(`document_folder_id`) on update NO ACTION on delete NO ACTION;
alter table `user_document_folder` add constraint `fk_user_document_folder_on_user_id` foreign key(`user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `user_document` add constraint `fk_on_user_document_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `user_document` add constraint `fk_on_user_document_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `user_document` add constraint `fk_user_document_on_document_id` foreign key(`document_id`) references `document`(`document_id`) on update NO ACTION on delete NO ACTION;
alter table `user_document` add constraint `fk_user_document_on_user_id` foreign key(`user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `user_message` add constraint `fk_on_user_message_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `user_message` add constraint `fk_on_user_message_message_box_id` foreign key(`message_box_id`) references `message_box`(`message_box_id`) on update NO ACTION on delete NO ACTION;
alter table `user_message` add constraint `fk_on_user_message_message_id` foreign key(`message_id`) references `message`(`message_id`) on update NO ACTION on delete NO ACTION;
alter table `user_message` add constraint `fk_on_user_message_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `user_message` add constraint `fk_on_user_message_user_id` foreign key(`user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `user_skill` add constraint `fk_on_user_skill_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `user_skill` add constraint `fk_on_user_skill_skill_id` foreign key(`skill_id`) references `skill`(`skill_id`) on update NO ACTION on delete NO ACTION;
alter table `user_skill` add constraint `fk_on_user_skill_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `user_skill` add constraint `fk_on_user_skill_user_id` foreign key(`user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `user_tag` add constraint `fk_on_user_tag_created_user_id` foreign key(`created_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `user_tag` add constraint `fk_on_user_tag_updated_user_id` foreign key(`updated_user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `user_tag` add constraint `fk_on_user_tag_user_id` foreign key(`user_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;
alter table `user` add constraint `fk_on_user_country_id` foreign key(`country_id`) references `country`(`country_id`) on update NO ACTION on delete NO ACTION;
alter table `user` add constraint `fk_on_user_user_profile_personal_id` foreign key(`user_profile_personal_id`) references `user_profile_personal`(`user_profile_personal_id`) on update NO ACTION on delete NO ACTION;

# --- !Downs

ALTER TABLE user DROP FOREIGN KEY fk_on_user_country_id;
ALTER TABLE user DROP FOREIGN KEY fk_on_user_user_profile_personal_id;
ALTER TABLE user_tag DROP FOREIGN KEY fk_on_user_tag_created_user_id;
ALTER TABLE user_tag DROP FOREIGN KEY fk_on_user_tag_updated_user_id;
ALTER TABLE user_tag DROP FOREIGN KEY fk_on_user_tag_user_id;
ALTER TABLE user_skill DROP FOREIGN KEY fk_on_user_skill_created_user_id;
ALTER TABLE user_skill DROP FOREIGN KEY fk_on_user_skill_skill_id;
ALTER TABLE user_skill DROP FOREIGN KEY fk_on_user_skill_updated_user_id;
ALTER TABLE user_skill DROP FOREIGN KEY fk_on_user_skill_user_id;
ALTER TABLE user_message DROP FOREIGN KEY fk_on_user_message_created_user_id;
ALTER TABLE user_message DROP FOREIGN KEY fk_on_user_message_message_box_id;
ALTER TABLE user_message DROP FOREIGN KEY fk_on_user_message_message_id;
ALTER TABLE user_message DROP FOREIGN KEY fk_on_user_message_updated_user_id;
ALTER TABLE user_message DROP FOREIGN KEY fk_on_user_message_user_id;
ALTER TABLE user_document DROP FOREIGN KEY fk_on_user_document_created_user_id;
ALTER TABLE user_document DROP FOREIGN KEY fk_on_user_document_updated_user_id;
ALTER TABLE user_document DROP FOREIGN KEY fk_user_document_on_document_id;
ALTER TABLE user_document DROP FOREIGN KEY fk_user_document_on_user_id;
ALTER TABLE user_document_folder DROP FOREIGN KEY fk_on_user_document_folder_created_user_id;
ALTER TABLE user_document_folder DROP FOREIGN KEY fk_on_user_document_folder_updated_user_id;
ALTER TABLE user_document_folder DROP FOREIGN KEY fk_user_document_folder_on_document_folder_id;
ALTER TABLE user_document_folder DROP FOREIGN KEY fk_user_document_folder_on_user_id;
ALTER TABLE state DROP FOREIGN KEY fk_on_states_country_id;
ALTER TABLE skill DROP FOREIGN KEY fk_on_skill_created_user_id;
ALTER TABLE skill DROP FOREIGN KEY fk_on_skill_updated_user_id;
ALTER TABLE skill DROP FOREIGN KEY fk_skill_on_industry_id;
ALTER TABLE message DROP FOREIGN KEY fk_on_message_created_user_id;
ALTER TABLE message DROP FOREIGN KEY fk_on_message_message_id;
ALTER TABLE message DROP FOREIGN KEY fk_on_message_sender_user_id;
ALTER TABLE message DROP FOREIGN KEY fk_on_message_updated_user_id;
ALTER TABLE message_recipient DROP FOREIGN KEY fk_on_message_recipient_created_user_id;
ALTER TABLE message_recipient DROP FOREIGN KEY fk_on_message_recipient_message_id;
ALTER TABLE message_recipient DROP FOREIGN KEY fk_on_message_recipient_updated_user_id;
ALTER TABLE message_recipient DROP FOREIGN KEY fk_on_message_recipient_user_id;
ALTER TABLE message_box DROP FOREIGN KEY fk_on_message_box_created_user_id;
ALTER TABLE message_box DROP FOREIGN KEY fk_on_message_box_updated_user_id;
ALTER TABLE message_box DROP FOREIGN KEY fk_on_message_box_user_id;
ALTER TABLE job_title DROP FOREIGN KEY fk_job_title_created_user_id;
ALTER TABLE job_title DROP FOREIGN KEY fk_job_title_on_company_id;
ALTER TABLE job_title DROP FOREIGN KEY fk_job_title_updated_user_id;
ALTER TABLE job_requirement DROP FOREIGN KEY fk_job_requirement_jon_on_title_id;
ALTER TABLE job_requirement DROP FOREIGN KEY fk_job_requirement_on_company_id;
ALTER TABLE job_requirement DROP FOREIGN KEY fk_job_requirement_on_created_user_id;
ALTER TABLE job_requirement DROP FOREIGN KEY fk_job_requirement_on_industry_id;
ALTER TABLE job_requirement DROP FOREIGN KEY fk_job_requirement_on_job_requirement_xtn_id;
ALTER TABLE job_requirement DROP FOREIGN KEY fk_job_requirement_on_updated_user_id;
ALTER TABLE job_requirement_xtn DROP FOREIGN KEY fk_job_requirement_xtn_on_created_user_id;
ALTER TABLE job_requirement_xtn DROP FOREIGN KEY fk_job_requirement_xtn_on_updated_user_id;
ALTER TABLE industry DROP FOREIGN KEY fk_on_industry_created_user_id;
ALTER TABLE industry DROP FOREIGN KEY fk_on_industry_updated_user_id;
ALTER TABLE follower DROP FOREIGN KEY fk_on_follower_created_user_id;
ALTER TABLE follower DROP FOREIGN KEY fk_on_follower_follower_id;
ALTER TABLE follower DROP FOREIGN KEY fk_on_follower_subject_id;
ALTER TABLE follower DROP FOREIGN KEY fk_on_follower_updated_user_id;
ALTER TABLE domain DROP FOREIGN KEY fk_on_domain_created_user_id;
ALTER TABLE domain DROP FOREIGN KEY fk_on_domain_industry_id;
ALTER TABLE domain DROP FOREIGN KEY fk_on_domain_updated_user_id;
ALTER TABLE document DROP FOREIGN KEY fk_document_on_document_folder_id;
ALTER TABLE document DROP FOREIGN KEY fk_on_document_created_user_id;
ALTER TABLE document DROP FOREIGN KEY fk_on_document_updated_user_id;
ALTER TABLE document_tag DROP FOREIGN KEY fk_on_document_tag_created_user_id;
ALTER TABLE document_tag DROP FOREIGN KEY fk_on_document_tag_document_id;
ALTER TABLE document_tag DROP FOREIGN KEY fk_on_document_tag_tag_id;
ALTER TABLE document_tag DROP FOREIGN KEY fk_on_document_tag_updated_user_id;
ALTER TABLE document_tag DROP FOREIGN KEY fk_on_document_tag_user_document_id;
ALTER TABLE document_tag DROP FOREIGN KEY fk_on_document_tag_user_id;
ALTER TABLE document_search DROP FOREIGN KEY fk_on_document_search_created_user_id;
ALTER TABLE document_search DROP FOREIGN KEY fk_on_document_search_updated_user_id;
ALTER TABLE document_search DROP FOREIGN KEY fk_on_document_search_user_id;
ALTER TABLE document_folder DROP FOREIGN KEY fk_on_document_folder_created_user_id;
ALTER TABLE document_folder DROP FOREIGN KEY fk_on_document_folder_updated_user_id;
ALTER TABLE contact DROP FOREIGN KEY fk_on_contact_created_user_id;
ALTER TABLE contact DROP FOREIGN KEY fk_on_contact_friend_id;
ALTER TABLE contact DROP FOREIGN KEY fk_on_contact_updated_user_id;
ALTER TABLE company DROP FOREIGN KEY fk_company_created_user_id;
ALTER TABLE company DROP FOREIGN KEY fk_company_updated_user_id;
ALTER TABLE company_user DROP FOREIGN KEY fk_company_user_company_id;
ALTER TABLE company_user DROP FOREIGN KEY fk_company_user_created_user_id;
ALTER TABLE company_user DROP FOREIGN KEY fk_company_user_updated_user_id;
drop table `user`;
drop table `user_tag`;
ALTER TABLE user_skill DROP PRIMARY KEY;
drop table `user_skill`;
drop table `user_profile_personal`;
ALTER TABLE user_message DROP PRIMARY KEY;
drop table `user_message`;
drop table `user_document`;
drop table `user_document_folder`;
drop table `state`;
drop table `skill`;
drop table `message`;
ALTER TABLE message_recipient DROP PRIMARY KEY;
drop table `message_recipient`;
drop table `message_box`;
drop table `job_title`;
drop table `job_requirement`;
drop table `job_requirement_xtn`;
drop table `industry`;
ALTER TABLE follower DROP PRIMARY KEY;
drop table `follower`;
drop table `domain`;
drop table `document`;
drop table `document_tag`;
drop table `document_search`;
drop table `document_folder`;
drop table `country`;
ALTER TABLE contact DROP PRIMARY KEY;
drop table `contact`;
drop table `company`;
drop table `company_user`;

