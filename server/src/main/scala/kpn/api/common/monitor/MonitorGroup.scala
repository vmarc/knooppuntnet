package kpn.api.common.monitor

import kpn.api.base.WithStringId

case class MonitorGroup(
  _id: String, // name
  description: String
) extends WithStringId {

  def name: String = _id

}
