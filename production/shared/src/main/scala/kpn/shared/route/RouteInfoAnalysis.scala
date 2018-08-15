package kpn.shared.route

case class RouteInfoAnalysis(
  startNodes: Seq[RouteNetworkNodeInfo],
  endNodes: Seq[RouteNetworkNodeInfo],
  startTentacleNodes: Seq[RouteNetworkNodeInfo],
  endTentacleNodes: Seq[RouteNetworkNodeInfo],
  unexpectedNodeIds: Seq[Long],
  members: Seq[RouteMemberInfo],
  expectedName: String,
  map: RouteMap,
  structureStrings: Seq[String]
)
