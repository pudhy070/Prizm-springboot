-- 기존 테이블이 있다면 삭제하여 깨끗한 상태에서 시작합니다.
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS channels;
DROP TABLE IF EXISTS servers;
DROP TABLE IF EXISTS users;

-- 서버 테이블
CREATE TABLE servers (
                         id BIGINT NOT NULL,
                         name VARCHAR(255) NOT NULL,
                         icon_url VARCHAR(255),
                         PRIMARY KEY (id)
);

-- 채널 테이블
CREATE TABLE channels (
                          id BIGINT NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          server_id BIGINT NOT NULL, -- snake_case로 변경
                          PRIMARY KEY (id),
                          FOREIGN KEY (server_id) REFERENCES servers (id) ON DELETE CASCADE
);


-- 메시지 테이블
CREATE TABLE messages (
    id BIGINT AUTO_INCREMENT NOT NULL,
    server_id BIGINT NOT NULL,
    channel_id BIGINT NOT NULL,
    thread_id VARCHAR(255),
    sender_id BIGINT NOT NULL,
    content TEXT,
    attachment_file_name VARCHAR(255),
    attachment_original_file_name VARCHAR(255),
    timestamp DATETIME(6),
    PRIMARY KEY (id)
);

-- 사용자 테이블
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT NOT NULL,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       PRIMARY KEY (id)
);