package kpn.server.api.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId
import kpn.api.custom.Day

case class MonitorRoute(
  _id: ObjectId,
  groupId: ObjectId,
  name: String,
  description: String,
  comment: Option[String],
  relationId: Option[Long],
  referenceType: Option[String],
  referenceDay: Option[Day],
  referenceFilename: Option[String],
  referenceDistance: Long,
  deviationDistance: Long,
  deviationCount: Long,
  osmWayCount: Long,
  osmDistance: Long,
  osmSegmentCount: Long,
  happy: Boolean
) extends WithObjectId
