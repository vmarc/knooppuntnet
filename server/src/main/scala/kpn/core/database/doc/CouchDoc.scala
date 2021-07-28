package kpn.core.database.doc

trait CouchDoc {
  def _id: String
  def _rev: Option[String]
  def withRev(_rev: Option[String]): CouchDoc
}
