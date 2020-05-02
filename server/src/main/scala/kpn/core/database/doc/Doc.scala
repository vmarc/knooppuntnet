package kpn.core.database.doc

trait Doc {
  def _id: String
  def _rev: Option[String]
  def withRev(_rev: Option[String]): Doc
}
