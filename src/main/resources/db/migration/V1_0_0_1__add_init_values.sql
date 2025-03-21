INSERT INTO request_destination (id, created_at, updated_at, name)
VALUES (1, LOCALTIMESTAMP, NULL, 'Альфа Банк'),
       (2, LOCALTIMESTAMP, NULL, 'ГИМС'),
       (3, LOCALTIMESTAMP, NULL, 'ГОСТЕХНАДЗОР'),
       (4, LOCALTIMESTAMP, NULL, 'Должник'),
       (5, LOCALTIMESTAMP, NULL, 'Росреестр'),
       (6, LOCALTIMESTAMP, NULL, 'РОСТЕХНАДЗОР'),
       (7, LOCALTIMESTAMP, NULL, 'ФНС'),
       (8, LOCALTIMESTAMP, NULL, 'ФСИП');

INSERT INTO question (id, created_at, updated_at, value, position)
VALUES (1, LOCALTIMESTAMP, NULL, 'Отчет временного управляющего о проведении процедуры наблюдения.', 1),
       (2, LOCALTIMESTAMP, NULL, 'Определение следующей процедуры банкротства в отношении Должника и об обращении в Арбитражный суд с соответствующим ходатайством.', 2),
       (3, LOCALTIMESTAMP, NULL, 'Определение кандидатуры арбитражного управляющего или саморегулируемой организации, из числа членов которой должен быть утверждён арбитражный управляющий.', 3),
       (4, LOCALTIMESTAMP, NULL, 'Определение дополнительных требований к кандидатуре арбитражного управляющего.', 4),
       (5, LOCALTIMESTAMP, NULL, 'Об образовании комитета кредиторов, определении количественного состава и полномочий комитета кредиторов, избрании членов комитета кредиторов.', 5),
       (6, LOCALTIMESTAMP, NULL, 'Выборы представителя собрания кредиторов.', 6),
       (7, LOCALTIMESTAMP, NULL, 'О выборе реестродержателя.', 7);
