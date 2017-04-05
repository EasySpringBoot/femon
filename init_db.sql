CREATE SCHEMA `femon`
  DEFAULT CHARACTER SET utf8;
ALTER TABLE `femon`.`fm_service`
  CHANGE COLUMN `response_body` `response_body` LONGTEXT NULL DEFAULT NULL;

ALTER TABLE `femon`.`fm_service_data`
  CHANGE COLUMN `response_body` `response_body` LONGTEXT NULL DEFAULT NULL;


UPDATE fm_service
SET today_fail_times = 0, today_success_times = 0, total_fail_times = 0, total_success_times = 0;
DELETE FROM fm_history;
