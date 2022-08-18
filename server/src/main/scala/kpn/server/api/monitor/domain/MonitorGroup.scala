package kpn.server.api.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId
import kpn.api.common.monitor.MonitorGroupProperties

object MonitorGroup {

  def from(properties: MonitorGroupProperties): MonitorGroup = {
    MonitorGroup(ObjectId(), properties.name, properties.description)
  }

  def from(id: ObjectId, properties: MonitorGroupProperties): MonitorGroup = {
    MonitorGroup(id, properties.name, properties.description)
  }
}

case class MonitorGroup(
  _id: ObjectId,
  name: String,
  description: String
) extends WithObjectId
