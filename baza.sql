--mariadb 11.5

drop schema if exists checkers;

create schema checkers;

use checkers;
create user if not exists 'javaman'@'localhost' identified by 'verystrong';

create table gameinfo(
  id int unsigned not null auto_increment primary key,
  num_players int unsigned not null,
  winning_player int unsigned,
  game_mode int unsigned not null
);

create table moves(
  id int unsigned not null auto_increment primary key,
  move_number int unsigned not null,
  x1 int not null,
  y1 int not null,
  x2 int not null,
  y2 int not null,
  player int not null,
  game_id int unsigned not null,
  foreign key (game_id) references gameinfo(id)
);

create table initialstate(
  id int unsigned not null auto_increment,
  game_id int unsigned not null,
  x int not null,
  y int not null,
  piece int not null,
  primary key (id),
  foreign key (game_id) references gameinfo(id)
);

grant select, insert, update on checkers.* to 'javaman'@'localhost';
flush privileges;