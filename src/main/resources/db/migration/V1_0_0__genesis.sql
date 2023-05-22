-- Description: Create player table
create table if not exists player (
    id serial primary key,
    name varchar(100) not null,
    created_at timestamp not null
);
-- Description: Create trump_game table
create table if not exists trump_game (
    id serial primary key,
    created_at timestamp not null,
    game_state text
);
comment on column trump_game.game_state is 'JSON representation of the game state';
-- Description: Create game_player table with indexes
create table if not exists game_player (
    id serial primary key,
    game_id integer not null,
    player_id integer not null,
    CONSTRAINT fk_game FOREIGN KEY(game_id) REFERENCES trump_game(id),
    CONSTRAINT fk_player FOREIGN KEY(player_id) REFERENCES player(id)
);
create index if not exists game_player_game_id_idx on game_player(game_id);
create index if not exists game_player_player_id_idx on game_player(player_id);