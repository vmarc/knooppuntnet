package kpn.core.database.doc

import kpn.api.common.monitor.LongdistanceRouteChange

case class LongdistanceRouteChangeDoc(_id: String, longdistanceRouteChange: LongdistanceRouteChange, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
