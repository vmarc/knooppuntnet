package kpn.core.database.doc

case class StringValueDoc(_id: String, value: String, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
