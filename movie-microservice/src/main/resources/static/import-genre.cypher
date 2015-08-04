LOAD CSV WITH HEADERS FROM "http://localhost:9005/movie-genres.csv" AS csvLine
MATCH (movie:Movie { id: csvLine.id })
WITH movie, split(csvLine.genres,";") as genres
FOREACH(g in genres |
    MERGE (genre:Genre:_Genre { name: g })
    MERGE (movie)-[:HAS_GENRE]->(genre))