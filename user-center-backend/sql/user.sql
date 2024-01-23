create table user
(
    id           bigint auto_increment
        primary key,
    username     varchar(255)                        null comment '用户昵称',
    userAccount  varchar(255)                        null comment '账号',
    avatarUrl    varchar(1024)                       null comment '头像',
    gender       tinyint                             null comment '性别 0-女 1-男',
    userPassword varchar(255)                        null comment '密码',
    phone        varchar(128)                        null comment '电话',
    email        varchar(255)                        null comment '邮件',
    userStatus   int       default 0                 not null comment '状态 0-正常',
    createTime   timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '创建时间',
    updateTime   timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint   default 0                 not null comment '逻辑删除 0-未删除 1-删除',
    userRole     int       default 0                 not null comment '用户角色 0-普通用户 1-管理员'
);