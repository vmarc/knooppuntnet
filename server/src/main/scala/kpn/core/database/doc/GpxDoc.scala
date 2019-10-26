package kpn.core.database.doc

import kpn.core.gpx.GpxFile

case class GpxDoc(_id: String, file: GpxFile, _rev: Option[String] = None) extends Doc
