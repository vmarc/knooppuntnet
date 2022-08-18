package kpn.server.api.monitor.domain

import kpn.api.base.MongoId
import kpn.api.base.WithMongoId

case class MonitorRoute(
  _id: MongoId,
  groupId: MongoId,
  name: String,
  description: String,
  relationId: Option[Long]
) extends WithMongoId
