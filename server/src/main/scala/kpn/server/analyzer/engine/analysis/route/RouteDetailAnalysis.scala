package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.common.MapBounds
import kpn.api.common.common.Ref
import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.custom.Relation
import kpn.api.custom.Subset
import kpn.core.analysis.RouteMember
import kpn.core.doc.RouteDetailDoc
import kpn.server.analyzer.engine.tiles.domain.RouteTileAnalysis

case class RouteDetailAnalysis(
  relation: Relation,
  routeDetail: RouteDetailDoc,
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

  def subsets: Seq[Subset] = {
    routeDetail.summary.countries.flatMap { country =>
      routeDetail.summary.networkTypes.flatMap { networkType =>
        Subset.of(country, networkType)
      }
    }
  }

  def toRef: Ref = Ref(id, name)
}
