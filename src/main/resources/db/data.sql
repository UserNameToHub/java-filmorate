-- delete from FILM_LIKES;
-- delete from USERS;
-- delete from USER_FRIENDS;
-- delete from genres;
-- delete from FILM_GENRES;
-- delete from FILMS;
-- delete from RATING;
--
insert into GENRES(id, name)
values (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик');

insert into rating(id, rating_mpa)
values (1, 'G'),
       (2, 'PG'),
       (3, 'PG-13'),
       (4, 'R'),
       (5, 'NC-17');