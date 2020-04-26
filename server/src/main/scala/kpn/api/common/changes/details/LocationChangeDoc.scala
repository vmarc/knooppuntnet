package kpn.api.common.changes.details

import kpn.core.database.doc.Doc

case class LocationChangeDoc(_id: String, locationChange: LocationChange, _rev: Option[String] = None) extends Doc
