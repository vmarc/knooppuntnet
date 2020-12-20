package kpn.core.database.doc

import kpn.api.common.monitor.MonitorGroup

case class MonitorGroupDoc(_id: String, monitorGroup: MonitorGroup, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
