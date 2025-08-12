package kpn.server.monitor.domain

import kpn.api.common.monitor.MonitorGroupProperties
import kpn.core.doc.WithObjectId
import org.bson.types.ObjectId

object MonitorGroup {

  def from(properties: MonitorGroupProperties): MonitorGroup = {
    MonitorGroup(ObjectId.get(), properties.name, properties.description)
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
