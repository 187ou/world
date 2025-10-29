-- book表，purchased表，user表都有修改，甚至大改

-- auto-generated definition
-- auto-generated definition
create table book
(
    book_id     bigint auto_increment comment '主键ID（自增）'
        primary key,
    book_name   varchar(255)                       not null comment '书籍名称',
    book_link   varchar(500)                       not null comment '书籍链接',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间（自动填充）',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间（自动更新）',
    token       varchar(255)                       null comment '阅读信息令牌',
    is_deleted  tinyint  default 0                 not null comment '逻辑删除（1=已删除，0=未删除）',
    link_user   varchar(1024)                      null comment '小说关联用户 - Json形式'
)
    comment '书籍信息表' collate = utf8mb4_unicode_ci;

create index idx_token
    on book (token)
    comment '令牌索引（加速查询）';

-- auto-generated definition
create table user
(
    user_id     bigint auto_increment comment '主键ID（自增）'
        primary key,
    nick_name   varchar(100)                       null comment '用户昵称',
    user_name   varchar(100)                       not null comment '登录用户名',
    password    varchar(255)                       not null comment '用户密码（加密存储）',
    sex         tinyint  default 0                 not null comment '性别（1=男，2=女，0=未知）',
    email       varchar(100)                       not null comment '用户邮箱',
    phone       varchar(20)                        null comment '用户手机号',
    level       int      default 1                 not null comment '用户等级（默认1级）',
    money       int      default 0                 not null comment '账户余额（单位：分，避免浮点数精度问题）',
    status      tinyint  default 1                 not null comment '账号状态（1=正常，0=禁用）',
    is_deleted  tinyint  default 0                 not null comment '逻辑删除（1=已删除，0=未删除）',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间（自动填充）',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间（自动更新）',
    constraint uk_email
        unique (email) comment '邮箱唯一，避免重复绑定',
    constraint uk_phone
        unique (phone) comment '手机号唯一，避免重复绑定',
    constraint uk_user_name
        unique (user_name) comment '用户名唯一，避免重复注册'
)
    comment '用户信息表' collate = utf8mb4_unicode_ci;

create index idx_phone
    on user (phone)
    comment '手机号查询索引';

create index idx_user_name
    on user (user_name)
    comment '用户名查询索引';




CREATE TABLE `read` (
                        `token` VARCHAR(255) NOT NULL COMMENT '阅读标识令牌（主键）',
                        `book_name` VARCHAR(255) NOT NULL COMMENT '书籍名称',
                        `book_link` VARCHAR(500) NOT NULL COMMENT '书籍链接',
                        `chapter_link` VARCHAR(500) NOT NULL COMMENT '章节链接',
                        `number` INT NOT NULL COMMENT '章节序号/阅读进度',
                        PRIMARY KEY (`token`) COMMENT '主键：阅读标识令牌',
                        KEY `idx_book_name` (`book_name`) COMMENT '书籍名称索引，优化查询效率'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户阅读记录表';


CREATE TABLE `level` (
                         `level` TINYINT NOT NULL COMMENT '设置ID/关联等级（主键）',
                         `print` TINYINT NOT NULL COMMENT '打印设置（1=启用，0=禁用）',
                         `reduce` TINYINT NOT NULL COMMENT '缩小比例（如5=缩小5%）',
                         `enlarge` TINYINT NOT NULL COMMENT '放大比例（如10=放大10%）',
                         `font` TINYINT NOT NULL COMMENT '字体类型（枚举值：1=宋体，2=黑体等）',
                         `font_size` TINYINT NOT NULL COMMENT '字体大小（如12=12号字）',
                         `bold` TINYINT NOT NULL COMMENT '是否加粗（1=是，0=否）',
                         `slash` TINYINT NOT NULL COMMENT '是否斜体（1=是，0=否）',
                         `underline` TINYINT NOT NULL COMMENT '是否下划线（1=是，0=否）',
                         `strikethrough` TINYINT NOT NULL COMMENT '是否删除线（1=是，0=否）',
                         `text_color` TINYINT NOT NULL COMMENT '文字颜色（色值编码，如0x000000=黑色）',
                         `background_color` TINYINT NOT NULL COMMENT '背景色（色值编码，如0xFFFFFF=白色）',
                         `style` TINYINT NOT NULL COMMENT '样式（枚举值：1=默认，2=夜间模式等）',
                         `line_spacing` TINYINT NOT NULL COMMENT '行间距（如1=单倍，2=双倍）',
                         PRIMARY KEY (`level`) COMMENT '主键：设置ID/关联等级'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户阅读样式设置表';

-- auto-generated definition
create table purchased
(
    user_id        bigint                   not null comment '用户ID（主键）',
    purchased_book VARCHAR(1024)            not null comment '用户已购小说id - Json形式',
    purchased_time datetime default CURRENT_TIMESTAMP not null comment '购买时间（对应LocalDateTime）',
    primary key (user_id),
    constraint fk_purchased_user
        foreign key (user_id) references user (user_id)
            on delete cascade
)
    comment '用户书籍购买记录表' collate = utf8mb4_unicode_ci;

create index idx_purchased_time
    on purchased (purchased_time);


CREATE TABLE `hot` (
                       `book_name` VARCHAR(255) NOT NULL COMMENT '书籍名称（主键）',
                       `cnt` INT NOT NULL COMMENT '热度计数（如点击量、收藏量）',
                       PRIMARY KEY (`book_name`) COMMENT '主键：书籍名称',
                       KEY `idx_cnt` (`cnt`) COMMENT '热度计数索引，优化按热度排序查询效率'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='热门书籍统计表';