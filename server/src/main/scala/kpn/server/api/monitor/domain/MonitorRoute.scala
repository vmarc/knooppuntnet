package kpn.server.api.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId
import kpn.api.custom.Day

case class MonitorRoute(
  _id: ObjectId,
  groupId: ObjectId,
  name: String,
  description: String,
  relationId: Option[Long],
  referenceType: String,
  referenceDay: Option[Day],
  referenceDistance: Long,
  deviationDistance: Long,
  deviationCount: Long,
  osmSegmentCount: Long,
  happy: Boolean
) extends WithObjectId
