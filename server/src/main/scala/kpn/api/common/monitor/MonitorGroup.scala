package kpn.api.common.monitor

import kpn.api.base.WithStringId

object MonitorGroup {
  def apply(_id: String, description: String): MonitorGroup = {
    MonitorGroup(_id, _id, description)
  }
}

case class MonitorGroup(
  _id: String, // name
  name: String,
  description: String
) extends WithStringId {

  def toMongo: MonitorGroup = {
    copy(_id = name)
  }
}
