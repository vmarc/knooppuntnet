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
  referenceType: String,
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
) extends WithObjectId {

  def isSuperRoute: Boolean = relation match {
    case None => throw new RuntimeException("could not determine wether super route yet")
    case Some(monitorRouteRelation) => monitorRouteRelation.relations.nonEmpty
  }
}
