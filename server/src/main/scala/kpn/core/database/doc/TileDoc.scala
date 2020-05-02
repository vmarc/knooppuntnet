package kpn.core.database.doc

case class TileDoc(_id: String, title: String = "tile", _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
