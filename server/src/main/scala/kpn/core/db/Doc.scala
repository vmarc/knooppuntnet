package kpn.core.db

trait Doc {
  def _id: String
  def _rev: Option[String]
}
