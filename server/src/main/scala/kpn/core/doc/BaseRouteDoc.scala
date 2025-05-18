package kpn.core.doc

import kpn.api.base.WithId
import kpn.api.common.Bounds
import kpn.api.common.Fact
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.RouteMemberInfo
import kpn.api.common.RouteSummary
import kpn.api.common.common.Ref
import kpn.api.common.route.RouteEdge
import kpn.api.common.route.RouteInfoAnalysis
import kpn.api.common.route.RouteNodes
import kpn.api.custom.Day
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.context.ElementIds

case class BaseRouteDoc(
  _id: Long, // routeId
  labels: Seq[String],
  summary: RouteSummary,
  proposed: Boolean,
  version: Long,
  changeSetId: Long,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  facts: Seq[Fact],
  unexpectedNodeIds: Seq[Long],
  unexpectedRelationIds: Seq[Long],
  members: Seq[RouteMemberInfo],
  nameDerivedFromNodes: Boolean,
  nodes: RouteNodes,
  analysis: RouteInfoAnalysis,
  geometryDigest: String,
  locationAnalysis: RouteLocationAnalysis,
  tiles: Seq[String],
  nodeRefs: Seq[Long], // networkNodeIds
  elementIds: ElementIds,
  edges: Seq[RouteEdge],
  segments: Seq[BaseRouteSegment],
  segmentElements: Seq[BaseRouteSegmentElement],
  paths: Seq[BaseRoutePath],
  relation: Option[Relation],
  hierarchy: Option[RouteRelation],
  bounds: Option[Bounds],
  subRouteIds: Seq[Long]
) extends WithId {

  def id: Long = summary.id

  def toRef: Ref = Ref(summary.id, summary.name)

  def deactivated: BaseRouteDoc = {
    copy(
      labels = labels.filterNot(label =>
        label == Label.active || label.startsWith("fact")
      )
    )
  }

  def isActive: Boolean = labels.contains(Label.active)
}
