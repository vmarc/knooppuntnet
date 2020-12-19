package kpn.core.database.doc

import kpn.api.common.monitor.LongdistanceRoute

case class LongdistanceRouteDoc(_id: String, longdistanceRoute: LongdistanceRoute, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
