DROP TABLE IF EXISTS movie_genre;
DROP TABLE IF EXISTS genre;
DROP TABLE IF EXISTS movie;

CREATE TABLE genre
(
  id BIGINT PRIMARY KEY NOT NULL,
  name VARCHAR(30) NOT NULL
);
CREATE TABLE movie
(
  id BIGINT PRIMARY KEY NOT NULL,
  released BIGINT,
  title VARCHAR(255) NOT NULL,
  timestamp DATETIME,
  video INT,
  url VARCHAR(255) NOT NULL
);
CREATE TABLE movie_genre
(
  movies_id BIGINT NOT NULL,
  genres_id BIGINT DEFAULT 0 NOT NULL,
  PRIMARY KEY (movies_id, genres_id)
);

CREATE UNIQUE INDEX unique_id ON genre (id);
CREATE UNIQUE INDEX unique_id ON movie (id);
