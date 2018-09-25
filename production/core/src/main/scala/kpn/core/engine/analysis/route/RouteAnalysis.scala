package kpn.core.engine.analysis.route

import kpn.core.analysis.RouteMember
import kpn.shared.Subset
import kpn.shared.common.MapBounds
import kpn.shared.common.Ref
import kpn.shared.data.Node
import kpn.shared.data.Relation
import kpn.shared.data.Way
import kpn.shared.data.raw.RawRelation
import kpn.shared.diff.RouteData
import kpn.shared.route.RouteInfo
import kpn.shared.route.RouteNetworkNodeInfo

case class RouteAnalysis(
  relation: Relation,
  route: RouteInfo = null,
  structure: RouteStructure = RouteStructure(),
  routeNodes: RouteNodeAnalysis = new RouteNodeAnalysis(),
  routeMembers: Seq[RouteMember] = Seq.empty,
  ways: Seq[Way] = Seq.empty,
  startNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  endNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  startTentacleNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  endTentacleNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  allWayNodes: Seq[Node] = Seq.empty,
  bounds: MapBounds = MapBounds()
) {

  def id: Long = relation.id

  def name: String = route.summary.name

  def subset: Option[Subset] = route.summary.country.flatMap(c => Subset.of(c, route.summary.networkType))

  def toRef: Ref = Ref(id, name)

  def containsNode(nodeId: Long): Boolean = {
    routeNodes.routeNodes.exists(_.id == nodeId)
  }

  def toRouteData: RouteData = {
    RouteData(
      route.summary.country,
      route.summary.networkType,
      relation.raw,
      route.summary.name,
      routeNodes.routeNodes.map(_.node.raw),
      allWayNodes.map(_.raw),
      ways.map(_.raw),
      Seq[RawRelation](), // TODO CHANGE add unexpected relations
      route.facts
    )
  }
}
