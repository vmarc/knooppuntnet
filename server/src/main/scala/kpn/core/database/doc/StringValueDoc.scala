package kpn.core.database.doc

case class StringValueDoc(_id: String, value: String, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
