-- 서버 데이터
INSERT INTO servers (id, name) VALUES
    (1000000000000001, 'General Workspace');

-- 채널 데이터
INSERT INTO channels (id, name, server_id) VALUES
                                               (2000000000000001, 'welcome', 1000000000000001),
                                               (2000000000000002, 'general-chat', 1000000000000001);

-- 메시지 데이터
INSERT INTO messages (server_id, channel_id, sender_id, content, timestamp) VALUES
                                                                                (1000000000000001, 2000000000000001, 1, '서버에 오신 것을 환영합니다!', NOW()),
                                                                                (1000000000000001, 2000000000000002, 1, '안녕하세요! general-chat 채널입니다.', NOW()),
                                                                                (1000000000000001, 2000000000000002, 2, '반갑습니다. 테스트 메시지입니다.', NOW());
