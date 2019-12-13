CREATE TABLE IF NOT EXISTS user (
	user_id int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
	login_id varchar(32) COMMENT '登录id',
	name varchar(64) COMMENT '名称',
	email varchar(200) NOT NULL COMMENT '邮箱',
	password varchar(64) NOT NULL COMMENT '密码',
	mobile_phone varchar(16) NOT NULL COMMENT '手机',
	type int(1) NOT NULL COMMENT '用户类型',
	PRIMARY KEY (user_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS organization (
	city_code varchar(16) NOT NULL COMMENT '城市编码',
	name varchar(32) NOT NULL COMMENT '组织名称',
	type int(1) NOT NULL COMMENT '组织类型',
  phone varchar(16) NOT NULL COMMENT '手机',
	PRIMARY KEY (city_code, name)
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS user_login_record (
	user_id varchar(16) NOT NULL COMMENT '用户id',
	login_time timestamp NOT NULL COMMENT '组织名称'
) ENGINE=InnoDB;