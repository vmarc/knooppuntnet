package kpn.core.doc

import kpn.api.base.WithId
import kpn.api.common.Bounds
import kpn.api.common.Fact
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.RouteMemberInfo
import kpn.api.common.RouteSummary
import kpn.api.common.common.Ref
import kpn.api.common.route.BaseRouteSegment
import kpn.api.common.route.RouteEdge
import kpn.api.common.route.RouteInfoAnalysis
import kpn.api.common.route.RouteNodes
import kpn.api.custom.Day
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.context.ElementIds

case class BaseRouteDoc(
  _id: Long, // routeId
  active: Boolean,
  summary: RouteSummary,
  proposed: Boolean,
  version: Long,
  changeSetId: Long,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  facts: Seq[Fact],
  unexpectedNodeIds: Seq[Long],
  members: Seq[RouteMemberInfo],
  nameDerivedFromNodes: Boolean,
  nodes: RouteNodes,
  analysis: RouteInfoAnalysis,
  geometryDigest: String,
  locationAnalysis: RouteLocationAnalysis,
  nodeRefs: Seq[Long], // networkNodeIds
  elementIds: ElementIds,
  edges: Seq[RouteEdge],
  segments: Seq[BaseRouteSegment],
  segmentElements: Seq[BaseRouteSegmentElement],
  paths: Seq[BaseRoutePath],
  relation: Option[Relation],
  subRelationTree: Option[RouteRelation],
  bounds: Option[Bounds],
  subRouteIds: Seq[Long]
) extends WithId {

  def id: Long = summary.id

  def toRef: Ref = Ref(summary.id, summary.name)

  def deactivated: BaseRouteDoc = {
    copy(active = false)
  }
}
