package kpn.core.db.couch

case class CouchConfig(
  host: String,
  port: Int,
  user: String,
  password: String,
  dbname: String,
  changeDbname: String,
  userDbname: String,
  reviewDbname: String,
  taskDbname: String
)
