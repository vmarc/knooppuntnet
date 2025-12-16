package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.Relation
import kpn.api.common.common.MapBounds
import kpn.api.common.common.Ref
import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.custom.Subset
import kpn.core.analysis.RouteMember
import kpn.core.doc.BaseRouteDoc
import kpn.server.analyzer.engine.tiles.domain.RouteTileAnalysis

case class RouteDetailAnalysis(
  relation: Relation,
  baseRoute: BaseRouteDoc,
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

  def name: String = baseRoute.base.summary.name

  def subsets: Seq[Subset] = {
    baseRoute.base.summary.countries.flatMap { country =>
      baseRoute.base.summary.routeTypes.flatMap { routeType =>
        Subset.of(country, routeType)
      }
    }
  }

  def toRef: Ref = Ref(id, name)
}
