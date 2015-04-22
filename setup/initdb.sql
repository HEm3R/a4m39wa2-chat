-- create table USER
create table "user" (
    id  bigserial not null,
    password_hash varchar(255) not null,
    username varchar(60) not null,
    primary key (id)
);

-- create table MESSAGE
create table message (
    id  bigserial not null,
    text varchar(500) not null,
    room_id int8 not null,
    user_id int8 not null,
    primary key (id)
);

-- create table ROLE
create table role (
    id  bigserial not null,
    role varchar(255) not null,
    primary key (id)
);

-- create table ROOM
create table room (
    id  bigserial not null,
    name varchar(30) not null,
    primary key (id)
);

-- create join table USER_ROLE
create table user_role (
    user_id int8 not null,
    role_id int8 not null
);

-- setup constraints
alter table "user" add constraint UK_user_username  unique (username);

alter table message add constraint FK_message_room foreign key (room_id) references room;

alter table message add constraint FK_message_user foreign key (user_id) references "user";

alter table user_role add constraint FK_user_role_role foreign key (role_id) references role;

alter table user_role add constraint FK_user_role_user foreign key (user_id) references "user"
