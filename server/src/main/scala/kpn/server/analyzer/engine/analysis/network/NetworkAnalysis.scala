package kpn.server.analyzer.engine.analysis.network

import kpn.api.common.NetworkExtraMemberNode
import kpn.api.common.NetworkExtraMemberRelation
import kpn.api.common.NetworkExtraMemberWay
import kpn.api.common.network.NetworkShape
import kpn.core.analysis.NetworkMemberRoute
import kpn.core.analysis.NetworkNode

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
