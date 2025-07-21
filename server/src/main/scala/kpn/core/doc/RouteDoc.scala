package kpn.core.doc

import kpn.api.base.ObjectId
import kpn.api.base.WithId
import kpn.api.common.Bounds
import kpn.api.common.Fact
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.RouteMemberInfo
import kpn.api.common.RouteSummary
import kpn.api.common.common.Ref
import kpn.api.common.common.Reference
import kpn.api.common.route.ParentRoute
import kpn.api.common.route.RouteEdge
import kpn.api.common.route.RouteInfoAnalysis
import kpn.api.common.route.RouteNodes
import kpn.api.common.route.RoutePath
import kpn.api.common.route.RouteSegment
import kpn.api.common.route.RouteStructureRow
import kpn.api.common.route.SuperSegment
import kpn.api.custom.Day
import kpn.api.custom.Timestamp

case class RouteDoc(
  _id: Long, // routeId
  active: Boolean,
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
  locationAnalysis: RouteLocationAnalysis,
  segments: Seq[RouteSegment],
  superDistance: Long,
  superSegments: Seq[SuperSegment],
  paths: Seq[RoutePath],
  routeIds: Seq[Long], // routeId of this route plus all other routes in the entire tree
  bounds: Option[Bounds],
  structureRows: Seq[RouteStructureRow],
  parentRoutes: Seq[ParentRoute],
  networkReferences: Seq[Reference],
  edges: Seq[RouteEdge],
  stamp: Option[ObjectId],
) extends WithId {

  def id: Long = summary.id

  def toRef: Ref = Ref(summary.id, summary.name)

  def deactivated: RouteDoc = {
    copy(
      active = false,
      labels = labels.filterNot(_.startsWith("fact"))
    )
  }
}
