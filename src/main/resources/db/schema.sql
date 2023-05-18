create table if not exists rating(
    id BIGINT not null,
    rating_mpa VARCHAR(5),
    CONSTRAINT rating_id PRIMARY KEY (id)
);

create table if not exists films(
    id BIGINT not null,
    name varchar not null,
    description varchar(200),
    release_date Date,
    duration BIGINT,
    rating_id BIGINT,
    CONSTRAINT film_id PRIMARY KEY (id),
    CONSTRAINT fk_rating_id
        FOREIGN KEY(rating_id)
            REFERENCES rating(id) on delete cascade
);

create table if not exists users(
    id BIGINT not null ,
    email varchar not null ,
    login varchar not null ,
    name varchar,
    birthday timestamp,
    CONSTRAINT user_id PRIMARY KEY (id)
);

create table if not exists categories(
    id BIGINT not null,
    name varchar,
    CONSTRAINT category_id PRIMARY KEY (id)
);

create table if not exists film_categories(
    film_id BIGINT not null,
    category_id BIGINT not null,
    primary key (film_id, category_id),
    CONSTRAINT fk_user_id
        FOREIGN KEY(film_id)
            REFERENCES users(id) on delete cascade,
    CONSTRAINT fk_category_id
        FOREIGN KEY(category_id)
            REFERENCES categories(id) on delete cascade
);

create table if not exists film_likes(
    film_id BIGINT not null,
    user_id BIGINT not null,
    primary key (film_id, user_id),
    CONSTRAINT fk_film_likes_id
        FOREIGN KEY(film_id)
            REFERENCES films(id) on delete cascade,
    CONSTRAINT fk_user_likes_id
        FOREIGN KEY(user_id)
            REFERENCES users(id) on delete cascade
);

create table if not exists user_friends(
    user_id BIGINT not null ,
    other_user_id BIGINT not null ,
    is_confirm boolean DEFAULT false,
    primary key (user_id, other_user_id),
    CONSTRAINT fk_fiends_user_id
        FOREIGN KEY(user_id)
            REFERENCES users(id) on delete cascade,
    CONSTRAINT fk_other_user_id
        FOREIGN KEY(other_user_id)
            REFERENCES users(id) on delete cascade
);