package kpn.api.common.monitor

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId

case class MonitorGroup(
  _id: ObjectId,
  name: String,
  description: String
) extends WithObjectId
