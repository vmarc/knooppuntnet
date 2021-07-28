package kpn.core.database.doc

import kpn.core.gpx.GpxFile

case class GpxDoc(_id: String, file: GpxFile, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
