package kpn.core.db.couch

case class CouchConfig(
  host: String,
  port: Int,
  user: String,
  password: String,
  analysisDatabaseName: String,
  changeDatabaseName: String,
  changesetDatabaseName: String,
  poiDatabaseName: String,
  taskDatabaseName: String
)
