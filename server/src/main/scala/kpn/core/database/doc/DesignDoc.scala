package kpn.core.database.doc

case class DesignDoc(
  _id: String,
  language: String,
  views: Map[String, ViewDoc] = Map.empty,
  autoupdate: Boolean = false,
  _rev: Option[String] = None
) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
