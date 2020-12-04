package kpn.core.database.doc

import kpn.api.common.longdistance.LongDistanceRoute

case class LongDistanceRouteDoc(_id: String, route: LongDistanceRoute, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
