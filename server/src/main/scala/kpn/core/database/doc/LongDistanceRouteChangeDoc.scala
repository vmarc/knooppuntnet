package kpn.core.database.doc

import kpn.api.common.longdistance.LongDistanceRouteChange

case class LongDistanceRouteChangeDoc(_id: String, longDistanceRouteChange: LongDistanceRouteChange, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
