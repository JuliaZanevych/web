INSERT INTO event_types (id, name)
VALUES (1, 'Пограбування'),
       (2, 'Убивство'),
       (3, 'Згвалтування'),
       (4, 'Побиття'),
       (5, 'ДТП'),
       (6, 'Катастрофа'),
       (7, 'Булінг'),
       (8, 'Хуліганство'),
       (9, 'Втрата свідомості'),
       (10, 'Нетверезий стан'),
       (11, 'Розповсюдження наркоричних засобів'),
       (12, 'Хабарництво'),
       (13, 'Розшук');
SELECT setval('event_types_id_seq', 12);

INSERT INTO events (id, title, description, event_date, publication_date)
VALUES (1, 'На Львівщині п’яний водій з’їхав у кювет, троє потерпілих', 'Аварія сталася 21 травня, близько 21:55 год, на автодорозі “Гайок–Добротвір”, поблизу села Перекалки, у Червоноградському районі. Про це повідомляє відділ комунікації поліції Львівської області. 30-річний водій автомобіля «Mazda Premacy», мешканець одного з сіл Львівського району, їдучи з міста Кам’янка-Бузька в село Перекалки, не впорався з керуванням та з’їхав у кювет. Внаслідок аварії, водій та пасажири — 43-річний мешканець Львівського району і 38-річний мешканець міста Кам’янка-Бузька — отримали тілесні ушкодження та були госпіталізовані. Водій керував транспортним засобом, перебуваючи у стані алкогольного сп’яніння, у нього було виявлено 1,6 проміле алкоголю. За фактом слідчі відділення поліції № 1 Червоноградського районного відділу поліції відкрили кримінальне провадження за ч. 1 ст. 286-1 (порушення правил безпеки дорожнього руху або експлуатації транспорту особами, які керують транспортними засобами у стані сп’яніння) Кримінального кодексу України. Санкція статті передбачає покарання — позбавлення волі на термін до трьох років з позбавленням права керувати транспортними засобами на термін від трьох до п’яти років.', '2021-05-21 21:55:00', '2021-05-23 19:58:01'),
       (2, 'На Львівщині зіткнулися дві вантажівки та легковик', 'Сьогодні, 29 травня, близько 16:50 сталася аварія у с.Нагірне Львівської області. Про це пише Варта1. В автопригоді зіткнулися три автомобілі, два з яких вантажні і один легковик. Обставини аварії встановлюватимуть правоохоронці.', '2021-05-29 16:50:00', '2021-05-29 17:34:00'),
       (3, 'Ексфутболіста збірної України затримали п’яним за кермом Porsche 911', 'У п’ятницю ввечері, 28 травня, у центрі Києва, на бульварі Лесі Українки патрульна поліція зупинила Porsche 911, за кермом якого перебував ексзахисник “Шахтаря” і збірної України з футболу В’ячеслав Шевчук. Про це пише ua-football. У екс-футболіста були ознаки алкогольного сп’яніння, однак тест на алкоголь він проходити відмовився, до того ж обурювався і почав штовхати патрульних. Поліцейські одягнули на нього наручники і затримали. У футболіста, є своя версія того, що сталося – його нібито “злили” друзі з “Шахтаря”. Ймовірно, Шевчук їхав з НСК “Олімпійський”, де сьогодні відбулася презентація фотокниги до 85-річчя “Шахтаря” і прем’єрний показ документального фільму “Донецьк і його команда”.', '2021-05-28 11:20:00', '2021-05-29 09:40:00');
SELECT setval('events_id_seq', 3);

INSERT INTO event_type_references(event_id, event_type_id)
VALUES (1, 5),
       (1, 12),
       (2, 5),
       (3, 8),
       (3, 10);