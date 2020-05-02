package kpn.core.database.doc

import kpn.core.gpx.GpxFile

case class GpxDoc(_id: String, file: GpxFile, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
