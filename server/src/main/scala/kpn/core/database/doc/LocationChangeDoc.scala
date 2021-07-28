package kpn.core.database.doc

import kpn.api.common.changes.details.LocationChange

case class LocationChangeDoc(_id: String, locationChange: LocationChange, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
