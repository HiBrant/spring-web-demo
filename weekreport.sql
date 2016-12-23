drop table if exists wr_user;
create table wr_user (
	id bigint not null auto_increment comment '主键',
	email varchar(128) not null comment '公司邮箱',
	fullname varchar(128) not null comment '姓名',
	createtime timestamp not null default '2000-1-1 0:0:0' comment '记录创建时间',
	updatetime timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '记录更新时间',
	status tinyint not null default 0 comment '状态，0：正常，-1：删除',
	role tinyint not null default 0 comment '角色，0：组员，1：组长，10：主管',
	superiorId bigint not null default -1 comment '直接汇报人userId，-1：未指定',
	primary key (id),
	unique key uk_email (email),
	index idx_superiorId (superiorId)
) engine=InnoDB default charset=utf8 collate utf8_general_ci comment '用户表';

drop table if exists wr_report;
create table wr_report (
	id bigint not null auto_increment comment '主键',
	reporterId bigint not null comment '报告人userId',
	superiorId bigint not null comment '直接汇报人userId',
	startDate date not null comment '开始日期',
	endDate date not null comment '结束日期',
	createtime timestamp not null default '2000-1-1 0:0:0' comment '记录创建时间',
	updatetime timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '记录更新时间',
	status tinyint not null default 0 comment '状态，0：草稿，1：已提交',
	primary key (id),
	index idx_reporterId (reporterId),
	index idx_superiorId (superiorId)
) engine=InnoDB default charset=utf8 collate utf8_general_ci comment '周报主表';

drop table if exists wr_reportDoneTask;
create table wr_reportDoneTask (
	id bigint not null auto_increment comment '主键',
	reportId bigint not null comment '周报主表ID',
	content text not null comment '工作内容',
	rate int not null default 100 comment '完成率',
	delay tinyint not null default 0 comment '是否延迟，1：是，0：否',
	startDate date not null comment '开始日期',
	endDate date not null comment '结束日期',
	owner varchar(128) not null comment '工作承担人',
	createtime timestamp not null default '2000-1-1 0:0:0' comment '记录创建时间',
	updatetime timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '记录更新时间',
	primary key (id),
	index idx_reportId (reportId)
) engine=InnoDB default charset=utf8 collate utf8_general_ci comment '周报已完成工作表';

drop table if exists wr_reportTodoTask;
create table wr_reportTodoTask (
	id bigint not null auto_increment comment '主键',
	reportId bigint not null comment '周报主表ID',
	content text not null comment '工作内容',
	startDate date not null comment '开始日期',
	endDate date not null comment '结束日期',
	owner varchar(128) not null comment '工作承担人',
	createtime timestamp not null default '2000-1-1 0:0:0' comment '记录创建时间',
	updatetime timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '记录更新时间',
	primary key (id),
	index idx_reportId (reportId)
) engine=InnoDB default charset=utf8 collate utf8_general_ci comment '周报计划工作表';

drop table if exists wr_reportOthers;
create table wr_reportOthers (
	id bigint not null auto_increment comment '主键',
	reportId bigint not null comment '周报主表ID',
	risk text comment '风险',
	suggestion text comment '建议',
	createtime timestamp not null default '2000-1-1 0:0:0' comment '记录创建时间',
	updatetime timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '记录更新时间',
	primary key (id),
	unique key uk_reportId (reportId)
) engine=InnoDB default charset=utf8 collate utf8_general_ci comment '周报其它信息表';

#请保证插入user表的第一条记录与配置文件中管理员的邮箱一致
insert into `wr_user`(email, fullname, createtime) values ('hzliujian3@corp.netease.com', '刘剑', now());