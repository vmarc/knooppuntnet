package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.common.MapBounds
import kpn.api.common.common.Ref
import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.diff.RouteData
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.custom.Relation
import kpn.api.custom.Subset
import kpn.core.analysis.RouteMember
import kpn.core.doc.RouteDetailDoc
import kpn.server.analyzer.engine.tiles.domain.RouteTileAnalysis
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo

case class RouteDetailAnalysis(
  relation: Relation,
  routeDetail: RouteDetailDoc,
  structure: RouteStructure = RouteStructure(),
  routeNodeAnalysis: OldRouteNodeAnalysis = OldRouteNodeAnalysis(),
  routeMembers: Seq[RouteMember] = Seq.empty,
  ways: Seq[Way] = Seq.empty,
  startNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  endNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  startTentacleNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  endTentacleNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  allWayNodes: Seq[Node] = Seq.empty,
  bounds: MapBounds = MapBounds(),
  geometryDigest: String = "",
  tileAnalysis: RouteTileAnalysis = RouteTileAnalysis("", "")
) {

  def id: Long = relation.id

  def name: String = routeDetail.summary.name

  def subset: Option[Subset] = routeDetail.summary.country.flatMap(c => Subset.of(c, routeDetail.summary.networkType))

  def toRef: Ref = Ref(id, name)

  def containsNode(nodeId: Long): Boolean = {
    routeNodeAnalysis.routeNodes.exists(_.id == nodeId)
  }

  def toRouteData: RouteData = {
    RouteData(
      routeDetail.summary.country,
      routeDetail.summary.networkType,
      routeDetail.summary.networkScope,
      relation.toRaw,
      routeDetail.summary.name,
      routeNodeAnalysis.routeNodes.map(_.node),
      allWayNodes,
      ways.map(_.toRaw),
      Seq[RawRelation](), // TODO CHANGE add unexpected relations
      routeDetail.facts
    )
  }

  def toRouteTileInfo: RouteTileInfo = {
    RouteTileInfo(
      _id = id,
      name = routeDetail.summary.name,
      proposed = routeDetail.proposed,
      lastSurvey = routeDetail.lastSurvey,
      tags = routeDetail.summary.tags,
      facts = routeDetail.facts,
      freePaths = routeDetail.analysis.map.freePaths,
      forwardPath = routeDetail.analysis.map.forwardPath,
      backwardPath = routeDetail.analysis.map.backwardPath,
      startTentaclePaths = routeDetail.analysis.map.startTentaclePaths,
      endTentaclePaths = routeDetail.analysis.map.endTentaclePaths,
      unusedSegments = routeDetail.analysis.map.unusedSegments,
    )
  }
}
