create table if not exists films(
    id BIGINT not null,
    name varchar not null,
    description varchar,
    release_date Date,
    duration integer,
    rating varchar(5),
    CONSTRAINT film_id PRIMARY KEY (id)
);

create table if not exists users(
    id integer not null ,
    email varchar not null ,
    login varchar not null ,
    name varchar,
    birthday Date,
    CONSTRAINT user_id PRIMARY KEY (id)
);

create table if not exists categories(
    id integer not null ,
    name varchar,
    CONSTRAINT category_id PRIMARY KEY (id)
);

create table if not exists film_categories(
    user_id BIGINT not null,
    category_id integer,
    CONSTRAINT fk_user_id
        FOREIGN KEY(user_id)
            REFERENCES users(id) on delete cascade,
    CONSTRAINT fk_category_id
        FOREIGN KEY(category_id)
            REFERENCES categories(id) on delete cascade
);

create table if not exists film_likes(
    film_id BIGINT,
    user_id BIGINT,
    CONSTRAINT fk_film_likes_id
        FOREIGN KEY(film_id)
            REFERENCES films(id) on delete cascade,
    CONSTRAINT fk_user_likes_id
        FOREIGN KEY(user_id)
            REFERENCES users(id) on delete cascade
);

create table if not exists user_friends(
    user_id BIGINT,
    other_user_id BIGINT,
    is_confirmation boolean,
    CONSTRAINT fk_fiends_user_id
        FOREIGN KEY(user_id)
            REFERENCES users(id) on delete cascade,
    CONSTRAINT fk_other_user_id
        FOREIGN KEY(other_user_id)
            REFERENCES users(id) on delete cascade
);