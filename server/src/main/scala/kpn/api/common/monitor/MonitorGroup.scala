package kpn.api.common.monitor

import kpn.api.base.MongoId
import kpn.api.base.WithMongoId

case class MonitorGroup(
  _id: MongoId,
  name: String,
  description: String
) extends WithMongoId
