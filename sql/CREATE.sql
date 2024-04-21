CREATE DATABASE IF NOT EXISTS `demo`;

USE `demo`;

create table users
(
    id           bigint auto_increment comment '用户唯一标识'
        primary key,
    username     varchar(255)             not null comment '用户名',
    password     varchar(255)             not null comment '密码',
    role         tinyint  default 0       not null comment '用户权限 0 - 普通用户 1- 管理员',
    avatar       varchar(255)             null comment '用户头像',
    email        varchar(255)             null comment '用户邮箱',
    phone        varchar(255)             null comment '用户电话号码',
    created_time datetime default (now()) not null comment '创建时间',
    updated_time datetime default (now()) null comment '更新时间',
    is_delete    int      default 0       not null comment '是否删除 0-未删除 1-已删除',
    constraint users_id_uindex
        unique (id)
);

create index users_role_index
    on users (role);