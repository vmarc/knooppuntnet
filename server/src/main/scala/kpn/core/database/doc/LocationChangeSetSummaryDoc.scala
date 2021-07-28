package kpn.core.database.doc

import kpn.api.common.LocationChangeSetSummary

case class LocationChangeSetSummaryDoc(_id: String, locationChangeSetSummary: LocationChangeSetSummary, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
