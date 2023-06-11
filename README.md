# java-filmorate
[//]: # (Template repository for Filmorate project.)
##### ER-диаграмма, описывающая связь таблиц в БД прлиложения *Filmorate*
###### Таблица *"FILMS"* имеет две соединительные таблицы: *"FILM-GENRES"* и *"FILM-LIKES"*. В рамках реализации связи 
###### "многие-ко-многим" таблица *"FILMS"* имеет вид связи "один-ко-многи" к вышеуказанными тоблицами. Также данная 
###### таблица имеет вид связи "один-к-одному" к таблице *"MPA"*, которая имеет видя свяи "один-ко-многим" к таблице 
###### *"FILM-GENRES"*.
###### Таблица *"USERS"* также имеет две соединительные таблицы: *"USER-FRIENDS"* и *"FILM-LIKES"*. В рамках реализации
###### связи "многие-ко-многим" таблица *"USERS"* имеет вид связи "один-ко-многи" к вышеуказанными тоблицами.
<img src="src/main/resources/static/ER.png">

###### Примеры CRUD-запросов.
```postgresql
-- create
INSERT INTO users(user_id, email, login, name, birthday)
VALUES(?, ?, ?, ?, ?);

-- read
SELECT *
FROM users AS u
WHERE u.user_id = ?;

-- update
UPDATE users set user_id = ?, email = ? , login = ?, name = ?, birthday = ? 
WHERE user_id = ?;

-- delete
DELETE FROM users 
WHERE user_id = ?
```