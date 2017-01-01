#建表的sql语句尽量写在之类,方便统一管理
CREATE TABLE  IF NOT EXISTS `chinese_book` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `book_name` varchar(128) NOT NULL DEFAULT '',
  `book_type` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `book_params` blob,
  `book_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `book_language` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='中文书目表';

CREATE TABLE  IF NOT EXISTS `foreign_book` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `book_name` varchar(128) NOT NULL DEFAULT '',
  `book_type` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `book_params` blob,
  `book_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `book_language` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='英文数目表';

CREATE TABLE IF NOT EXISTS order_0 (
	  id bigint(20) NOT NULL AUTO_INCREMENT,
	  book_id bigint(20) NOT NULL,
	  uid bigint(20) NOT NULL,
	  PRIMARY KEY (id),
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='订单表0';

CREATE TABLE IF NOT EXISTS order_1 (
	  id bigint(20) NOT NULL AUTO_INCREMENT,
	  book_id bigint(20) NOT NULL,
	  uid bigint(20) NOT NULL,
	  PRIMARY KEY (id),
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='订单表1';

CREATE TABLE IF NOT EXISTS order_2 (
	  id bigint(20) NOT NULL AUTO_INCREMENT,
	  book_id bigint(20) NOT NULL,
	  uid bigint(20) NOT NULL,
	  PRIMARY KEY (id),
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='订单表2';

CREATE TABLE IF NOT EXISTS order_3 (
	  id bigint(20) NOT NULL AUTO_INCREMENT,
	  book_id bigint(20) NOT NULL,
	  uid bigint(20) NOT NULL,
	  PRIMARY KEY (id),
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='订单表3';

CREATE TABLE IF NOT EXISTS order_4 (
	  id bigint(20) NOT NULL AUTO_INCREMENT,
	  book_id bigint(20) NOT NULL,
	  uid bigint(20) NOT NULL,
	  PRIMARY KEY (id),
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='订单表4';

CREATE TABLE IF NOT EXISTS order_5 (
	  id bigint(20) NOT NULL AUTO_INCREMENT,
	  book_id bigint(20) NOT NULL,
	  uid bigint(20) NOT NULL,
	  PRIMARY KEY (id),
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='订单表5';

CREATE TABLE IF NOT EXISTS order_6 (
	  id bigint(20) NOT NULL AUTO_INCREMENT,
	  book_id bigint(20) NOT NULL,
	  uid bigint(20) NOT NULL,
	  PRIMARY KEY (id),
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='订单表6';

CREATE TABLE IF NOT EXISTS order_7 (
	  id bigint(20) NOT NULL AUTO_INCREMENT,
	  book_id bigint(20) NOT NULL,
	  uid bigint(20) NOT NULL,
	  PRIMARY KEY (id),
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='订单表7';

CREATE TABLE IF NOT EXISTS finished_order_201612 (
	  id bigint(20) NOT NULL AUTO_INCREMENT,
	  book_id bigint(20) NOT NULL,
	  uid bigint(20) NOT NULL,
	  PRIMARY KEY (id),
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='已完成订单表';