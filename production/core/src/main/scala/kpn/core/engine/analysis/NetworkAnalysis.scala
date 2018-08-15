package kpn.core.engine.analysis

import kpn.core.analysis.NetworkMemberRoute
import kpn.core.analysis.NetworkNode
import kpn.shared.NetworkExtraMemberNode
import kpn.shared.NetworkExtraMemberRelation
import kpn.shared.NetworkExtraMemberWay
import kpn.shared.network.NetworkShape

case class NetworkAnalysis(
  allNodes: Map[Long, NetworkNode] = Map(),
  networkExtraMemberWay: Seq[NetworkExtraMemberWay] = Seq(),
  networkExtraMemberNode: Seq[NetworkExtraMemberNode] = Seq(),
  networkExtraMemberRelation: Seq[NetworkExtraMemberRelation] = Seq(),
  routes: Seq[NetworkMemberRoute] = Seq(),
  networkNodesInRelation: Set[NetworkNode] = Set(),
  networkNodesInRouteWays: Set[NetworkNode] = Set(),
  networkNodesInRouteRelations: Set[NetworkNode] = Set(),
  allNodesInNetwork: Set[NetworkNode] = Set(),
  shape: Option[NetworkShape] = None
)
