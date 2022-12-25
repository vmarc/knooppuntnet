package kpn.server.api.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.custom.Day

case class MonitorRoute(
  _id: ObjectId,
  groupId: ObjectId,
  name: String,
  description: String,
  comment: Option[String],
  relationId: Option[Long],
  user: String,

  // reference information
  referenceType: Option[String],
  referenceDay: Option[Day],
  referenceFilename: Option[String],
  referenceDistance: Long,

  // analysis results
  deviationDistance: Long,
  deviationCount: Long,
  osmWayCount: Long,
  osmDistance: Long,
  osmSegmentCount: Long,
  happy: Boolean,

  relation: Option[MonitorRouteRelation]
) extends WithObjectId
