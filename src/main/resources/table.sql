CREATE TABLE book (
                      book_id          BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）'
                          PRIMARY KEY,
                      book_name   VARCHAR(255)   NOT NULL COMMENT '书籍名称',
                      book_link   VARCHAR(500)   NOT NULL COMMENT '书籍链接',
                      create_time DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（自动填充）',
                      update_time DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（自动更新）',
                      update_user VARCHAR(100)   NOT NULL COMMENT '更新人',
                      is_collect  TINYINT        NOT NULL DEFAULT 0 COMMENT '是否收藏（1=收藏，0=未收藏）',
                      token       VARCHAR(255)   NULL COMMENT '阅读信息令牌',
                      is_deleted  TINYINT        NOT NULL DEFAULT 0 COMMENT '逻辑删除（1=已删除，0=未删除）',
                      INDEX idx_token (token) COMMENT '令牌索引（加速查询）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '书籍信息表';


CREATE TABLE user (
                      user_id          BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）'
                          PRIMARY KEY,
                      nick_name   VARCHAR(100)   NOT NULL COMMENT '用户昵称',
                      user_name   VARCHAR(100)   NOT NULL COMMENT '登录用户名',
                      password    VARCHAR(255)   NOT NULL COMMENT '用户密码（加密存储）',
                      sex         TINYINT        NOT NULL DEFAULT 0 COMMENT '性别（1=男，2=女，0=未知）',
                      email       VARCHAR(100)   NOT NULL COMMENT '用户邮箱',
                      phone       VARCHAR(20)    NOT NULL COMMENT '用户手机号',
                      level       INT            NOT NULL DEFAULT 1 COMMENT '用户等级（默认1级）',
                      money       INT            NOT NULL DEFAULT 0 COMMENT '账户余额（单位：分，避免浮点数精度问题）',
                      status      TINYINT        NOT NULL DEFAULT 1 COMMENT '账号状态（1=正常，0=禁用）',
                      is_deleted  TINYINT        NOT NULL DEFAULT 0 COMMENT '逻辑删除（1=已删除，0=未删除）',
                      create_time DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（自动填充）',
                      update_time DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（自动更新）',
    -- 唯一索引（保持原业务约束，命名规范调整为uk_字段名）
                      CONSTRAINT uk_user_name UNIQUE (user_name) COMMENT '用户名唯一，避免重复注册',
                      CONSTRAINT uk_email UNIQUE (email) COMMENT '邮箱唯一，避免重复绑定',
                      CONSTRAINT uk_phone UNIQUE (phone) COMMENT '手机号唯一，避免重复绑定',
    -- 普通索引（根据查询频率添加，如按用户名/手机号查询）
                      INDEX idx_user_name (user_name) COMMENT '用户名查询索引',
                      INDEX idx_phone (phone) COMMENT '手机号查询索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '用户信息表';


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

CREATE TABLE `purchased` (
                             `user_id` BIGINT NOT NULL COMMENT '用户ID（联合主键）',
                             `book_id` BIGINT NOT NULL COMMENT '书籍ID（联合主键）',
                             `book_link` VARCHAR(500) NOT NULL COMMENT '书籍链接',
                             `purchased_time` DATETIME NOT NULL COMMENT '购买时间（对应LocalDateTime）',
                             PRIMARY KEY (`user_id`, `book_id`),
                             KEY `idx_purchased_time` (`purchased_time`),
                             CONSTRAINT `fk_purchased_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
                             CONSTRAINT `fk_purchased_book` FOREIGN KEY (`book_id`) REFERENCES `book` (`book_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = '用户书籍购买记录表';

CREATE TABLE `hot` (
                       `book_name` VARCHAR(255) NOT NULL COMMENT '书籍名称（主键）',
                       `cnt` INT NOT NULL COMMENT '热度计数（如点击量、收藏量）',
                       PRIMARY KEY (`book_name`) COMMENT '主键：书籍名称',
                       KEY `idx_cnt` (`cnt`) COMMENT '热度计数索引，优化按热度排序查询效率'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='热门书籍统计表';
