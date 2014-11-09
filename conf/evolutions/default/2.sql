
# --- !Ups

create table `state` (`state_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`code` VARCHAR(254) NOT NULL,`name` VARCHAR(254) NOT NULL,`country_id` BIGINT NOT NULL);
create unique index `idx_state_on_code_unique` on `state` (`country_id`,`code`);
create table `user_personal_profile` (`user_personal_profile_id` BIGINT AUTO_INCREMENT PRIMARY KEY,`aboutMe` VARCHAR(254),`picture` VARCHAR(254),`mobile` VARCHAR(254),`alternative_email` VARCHAR(254),`gender` INTEGER,`marital_status` INTEGER,`birth_year` INTEGER,`birth_day` INTEGER,`birth_month` INTEGER,`xray_terms` VARCHAR(254),`created_at` TIMESTAMP NOT NULL,`updated_at` TIMESTAMP NULL);
alter table `state` add constraint `fk_on_states_country_id` foreign key(`country_id`) references `user`(`user_id`) on update NO ACTION on delete NO ACTION;

# --- !Downs

ALTER TABLE state DROP FOREIGN KEY fk_on_states_country_id;
drop table `user_personal_profile`;
drop table `state`;
