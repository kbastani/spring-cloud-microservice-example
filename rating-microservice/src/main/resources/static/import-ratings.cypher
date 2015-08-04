USING PERIODIC COMMIT 20000
LOAD CSV WITH HEADERS FROM "http://localhost:9004/ratings.csv" AS csvLine
MERGE (user:User:_User { id: toInt(csvLine.userId) })
ON CREATE SET user.__type__="User", user.className="data.domain.nodes.User"
MERGE (product:Product:_Product { id: toInt(csvLine.movieId) })
ON CREATE SET product.__type__="Product", product.className="data.domain.nodes.Product"
MERGE (user)-[r:Rating]->(product)
ON CREATE SET r.timestamp = toInt(csvLine.timestamp), r.rating = toInt(csvLine.rating), r.knownId = csvLine.userId + "_" + csvLine.movieId, r.__type__ = "Rating", r.className = "data.domain.rels.Rating"