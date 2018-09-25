package kpn.core.engine.analysis

import kpn.core.analysis.NetworkMemberRoute
import kpn.core.analysis.NetworkNode
import kpn.shared.NetworkExtraMemberNode
import kpn.shared.NetworkExtraMemberRelation
import kpn.shared.NetworkExtraMemberWay
import kpn.shared.network.NetworkShape

case class NetworkAnalysis(
  allNodes: Map[Long, NetworkNode] = Map.empty,
  networkExtraMemberWay: Seq[NetworkExtraMemberWay] = Seq.empty,
  networkExtraMemberNode: Seq[NetworkExtraMemberNode] = Seq.empty,
  networkExtraMemberRelation: Seq[NetworkExtraMemberRelation] = Seq.empty,
  routes: Seq[NetworkMemberRoute] = Seq.empty,
  networkNodesInRelation: Set[NetworkNode] = Set.empty,
  networkNodesInRouteWays: Set[NetworkNode] = Set.empty,
  networkNodesInRouteRelations: Set[NetworkNode] = Set.empty,
  allNodesInNetwork: Set[NetworkNode] = Set.empty,
  shape: Option[NetworkShape] = None
)
