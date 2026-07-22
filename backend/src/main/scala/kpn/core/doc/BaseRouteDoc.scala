package kpn.core.doc

import kpn.api.common.Bounds
import kpn.api.common.Fact
import kpn.api.common.common.Ref
import kpn.api.common.data.Tagable
import kpn.api.common.route.BaseRouteSegment
import kpn.api.custom.Tag
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
  subRelationTree: Option[RouteRelation],
  subRouteIds: Seq[Long],
  bounds: Option[Bounds],
) extends WithId with Tagable {

  def toRef: Ref = Ref(_id, base.name)

  def deactivated: BaseRouteDoc = {
    copy(active = false)
  }

  def tags: Seq[Tag] = {
    base.raw.tags
  }
}
