
INSERT  INTO `application_user`(`id`,`created_at`,`created_by`,`modified_at`,`modified_by`,`active`,`email`,`password`,`username`,`write_protected`) VALUES
(1,'2020-07-28 18:15:47',NULL,'2020-07-28 18:15:47',NULL,'true','admin@localhost','$2a$10$TxPQrRKSulPdP1ps.z1MsOh4C4OMUS9UssE89ZIJPObKs43aGYaNe','admin','true'),
(2,'2020-07-28 18:15:47',NULL,'2020-07-28 18:15:47',NULL,'true','user@localhost','$2a$10$TxPQrRKSulPdP1ps.z1MsOh4C4OMUS9UssE89ZIJPObKs43aGYaNe','user','false');

/*Data for the table `authority` */

INSERT  INTO `authority`(`id`,`authority`) VALUES
(1,'USER_READ'),
(2,'USER_MANAGER_ACCESS'),
(3,'USER_MANAGER_WRITE'),
(4,'USER_MANAGER_READ'),
(5,'ROLE_MANAGER_READ'),
(6,'USER_MANAGER_LOCK'),
(7,'USER_ACCESS'),
(8,'ROLE_MANAGER_ACCESS'),
(9,'ROLE_MANAGER_WRITE'),
(10,'USER_WRITE');

/*Data for the table `profile` */

/*Data for the table `refresh_token` */

/*Data for the table `role` */

INSERT  INTO `role`(`id`,`created_at`,`created_by`,`modified_at`,`modified_by`,`locked`,`role`) VALUES
(1,'2020-07-28 18:15:47',NULL,'2020-07-28 18:15:47',NULL,true,'ROLE_ADMIN'),
(2,'2020-07-28 18:15:47',NULL,'2020-07-28 18:15:47',NULL,false,'ROLE_USER'),
(3,'2020-07-28 18:15:47',NULL,'2020-07-28 18:15:47',NULL,false,'ROLE_INDEPENDENT');

/*Data for the table `role_authorities` */

INSERT  INTO `role_authorities`(`role_id`,`authority_id`) VALUES
(1,6),
(1,7),
(1,9),
(1,5),
(1,1),
(1,3),
(1,8),
(1,10),
(1,2),
(1,4),
(2,1),
(2,7),
(2,10);

/*Data for the table `user_roles` */

INSERT  INTO `user_roles`(`user_id`,`role_id`) VALUES
/*(1, 1), */ /* ADMIN is added on init stage in application.properties */
(2,2);
