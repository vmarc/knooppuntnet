package kpn.core.database.doc

import kpn.api.common.LocationChangeSetSummary

case class LocationChangeSetSummaryDoc(_id: String, locationChangeSetSummary: LocationChangeSetSummary, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
