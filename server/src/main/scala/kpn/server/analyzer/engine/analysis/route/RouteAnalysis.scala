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
import kpn.core.doc.RouteDoc
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo

case class RouteAnalysis(
  relation: Relation,
  route: RouteDoc,
  structure: RouteStructure = RouteStructure(),
  routeNodeAnalysis: RouteNodeAnalysis = RouteNodeAnalysis(),
  routeMembers: Seq[RouteMember] = Seq.empty,
  ways: Seq[Way] = Seq.empty,
  startNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  endNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  startTentacleNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  endTentacleNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  allWayNodes: Seq[Node] = Seq.empty,
  bounds: MapBounds = MapBounds(),
  geometryDigest: String = ""
) {

  def id: Long = relation.id

  def name: String = route.summary.name

  def subset: Option[Subset] = route.summary.country.flatMap(c => Subset.of(c, route.summary.networkType))

  def toRef: Ref = Ref(id, name)

  def containsNode(nodeId: Long): Boolean = {
    routeNodeAnalysis.routeNodes.exists(_.id == nodeId)
  }

  def toRouteData: RouteData = {
    RouteData(
      route.summary.country,
      route.summary.networkType,
      route.summary.networkScope,
      relation.raw,
      route.summary.name,
      routeNodeAnalysis.routeNodes.map(_.node.raw),
      allWayNodes.map(_.raw),
      ways.map(_.raw),
      Seq[RawRelation](), // TODO CHANGE add unexpected relations
      route.facts
    )
  }

  def toRouteTileInfo: RouteTileInfo = {
    RouteTileInfo(
      _id = id,
      name = route.summary.name,
      proposed = route.proposed,
      lastSurvey = route.lastSurvey,
      tags = route.tags,
      facts = route.facts,
      freePaths = route.analysis.map.freePaths,
      forwardPath = route.analysis.map.forwardPath,
      backwardPath = route.analysis.map.backwardPath,
      startTentaclePaths = route.analysis.map.startTentaclePaths,
      endTentaclePaths = route.analysis.map.endTentaclePaths
    )
  }
}
