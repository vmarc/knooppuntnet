package kpn.core.doc

import kpn.api.common.LatLon
import kpn.api.common.common.Ref
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.Timestamp

case class NetworkInfoNodeDetail(
  id: Long,
  name: String,
  longName: String,
  latitude: String,
  longitude: String,
  connection: Boolean,
  roleConnection: Boolean,
  definedInRelation: Boolean,
  proposed: Boolean,
  timestamp: Timestamp,
  lastSurvey: Option[Day],
  expectedRouteCount: Option[Long],
  facts: Seq[Fact]
) extends LatLon {

  def toRef: Ref = {
    Ref(id, name)
  }

  def isSameAs(other: NetworkInfoNodeDetail): Boolean = {
    name == other.name &&
      longName == other.longName &&
      latitude == other.latitude &&
      longitude == other.longitude &&
      connection == other.connection &&
      roleConnection == other.roleConnection &&
      definedInRelation == other.definedInRelation &&
      proposed == other.proposed &&
      lastSurvey == other.lastSurvey &&
      expectedRouteCount == other.expectedRouteCount &&
      facts == other.facts
  }
}
