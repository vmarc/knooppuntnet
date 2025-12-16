package kpn.core.doc

import kpn.api.common.Bounds
import kpn.api.common.Fact
import kpn.api.common.Relation
import kpn.api.common.common.Ref
import kpn.api.common.route.BaseRouteSegment
import kpn.server.analyzer.engine.context.ElementIds

case class BaseRouteDoc(
  _id: Long, // routeId
  active: Boolean,
  base: RouteBaseData,
  facts: Seq[Fact],
  geometryDigest: String,
  elementIds: ElementIds,
  segments: Seq[BaseRouteSegment],
  segmentElements: Seq[BaseRouteSegmentElement],
  paths: Seq[BaseRoutePath],
  relation: Option[Relation],
  subRelationTree: Option[RouteRelation],
  subRouteIds: Seq[Long],
  bounds: Option[Bounds],
) extends WithId {

  def toRef: Ref = Ref(_id, base.summary.name)

  def deactivated: BaseRouteDoc = {
    copy(active = false)
  }
}
