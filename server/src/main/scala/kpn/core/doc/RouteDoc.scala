package kpn.core.doc

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
import kpn.api.custom.Day
import kpn.api.custom.Timestamp
import org.bson.types.ObjectId

//_id: Long, // routeId
//active: Boolean,
//summary: RouteSummary,
//proposed: Boolean,
//version: Long,
//changeSetId: Long,
//lastUpdated: Timestamp,
//lastSurvey: Option[Day],
//facts: Seq[Fact],
//unexpectedNodeIds: Seq[Long],
//members: Seq[RouteMemberInfo],
//nameDerivedFromNodes: Boolean,
//nodes: RouteNodes,
//analysis: RouteInfoAnalysis,
//geometryDigest: String,
//locationAnalysis: RouteLocationAnalysis,
//networkNodeIds: Option[Seq[Long]],
//elementIds: ElementIds,
//edges: Seq[RouteEdge],
//segments: Seq[BaseRouteSegment],
//segmentElements: Seq[BaseRouteSegmentElement],
//paths: Seq[BaseRoutePath],
//relation: Option[Relation],
//subRelationTree: Option[RouteRelation],
//bounds: Option[Bounds],
//subRouteIds: Seq[Long]

case class RouteDoc(
  _id: Long, // routeId // from BaseRouteDoc
  active: Boolean, // from BaseRouteDoc
  labels: Seq[String],
  summary: RouteSummary, // from BaseRouteDoc
  proposed: Boolean, // from BaseRouteDoc
  version: Long, // from BaseRouteDoc
  changeSetId: Long, // from BaseRouteDoc
  lastUpdated: Timestamp, // from BaseRouteDoc
  lastSurvey: Option[Day], // from BaseRouteDoc
  facts: Seq[Fact], // from BaseRouteDoc + verder aangevuld in MainRouteAnalyzer?
  unexpectedNodeIds: Seq[Long], // from BaseRouteDoc
  unexpectedRelationIds: Seq[Long], // from BaseRouteDoc
  members: Seq[RouteMemberInfo], // from BaseRouteDoc
  nameDerivedFromNodes: Boolean, // from BaseRouteDoc
  nodes: RouteNodes, // from BaseRouteDoc
  analysis: RouteInfoAnalysis, // from BaseRouteDoc
  locationAnalysis: RouteLocationAnalysis, // from BaseRouteDoc
  segments: Seq[RouteSegment], // derived from information in BaseRouteDoc
  superDistance: Long,
  superSegments: Seq[SuperSegment],
  paths: Seq[RoutePath],
  networkNodeIds: Option[Seq[Long]],
  routeIds: Seq[Long], // routeId of this route plus all other routes in the entire tree
  bounds: Option[Bounds], // from BaseRouteDoc
  structureRows: Seq[RouteStructureRow],
  relationCount: Long,
  relationLevels: Long,
  parentRoutes: Seq[ParentRoute],
  networkReferences: Seq[Reference],
  edges: Seq[RouteEdge], // from BaseRouteDoc
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
