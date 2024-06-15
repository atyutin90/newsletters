--устанавливаем актуальное значение request_destination_id_seq в соответствии с данными в таблице request_destination
SELECT setval('request_destination_id_seq', (SELECT MAX(id) FROM request_destination));

--устанавливаем актуальное значение question_id_seq в соответствии с данными в таблице question
SELECT setval('question_id_seq', (SELECT MAX(id) FROM question));

INSERT INTO request_destination (created_at, updated_at, name)
VALUES (LOCALTIMESTAMP, NULL, 'ГИБДД');
