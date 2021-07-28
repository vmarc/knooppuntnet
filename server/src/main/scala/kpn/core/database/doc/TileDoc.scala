package kpn.core.database.doc

case class TileDoc(_id: String, title: String = "tile", _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
