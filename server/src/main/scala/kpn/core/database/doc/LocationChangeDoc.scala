package kpn.core.database.doc

import kpn.api.common.changes.details.LocationChange

case class LocationChangeDoc(_id: String, locationChange: LocationChange, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
