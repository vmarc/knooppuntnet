package kpn.core.planner

import kpn.shared.NodeInfo
import kpn.core.db.couch.Couch
import kpn.core.repository.NodeRepository
import kpn.core.repository.RouteRepository
import kpn.shared.NetworkType
import kpn.shared.route.RouteInfo

class PlanBuilder(nodeRepository: NodeRepository, routeRepository: RouteRepository) {

  def build(networkType: NetworkType, encodedPlan: EncodedPlan): Plan = {
    val nodeMap: Map[Long, NodeInfo] = nodeRepository.nodesWithIds(encodedPlan.allNodeIds, Couch.uiTimeout, stale = false).map(n => n.id -> n).toMap
    val routeMap: Map[Long, RouteInfo] = routeRepository.routesWithIds(encodedPlan.routeIds, Couch.uiTimeout).map(r => r.id -> r).toMap
    val distances: Seq[Int] = new DistanceCalculator(routeMap).calculateDistances(encodedPlan)

    val planItems: Seq[PlanItem] = encodedPlan.items.zip(distances).map { case (encodedPlanItem, meters) =>
      encodedPlanItem match {
        case nodeKey: EncodedNode =>
          nodeMap.get(nodeKey.nodeId) match {
            case Some(node) => PlanNode(node.id, node.name(networkType), meters)
            case None => PlanMessageItem(NodeNotFoundInDatabase(nodeKey.nodeId))
          }

        case nodeKey: EncodedIntermediateNode =>
          nodeMap.get(nodeKey.nodeId) match {
            case Some(node) => PlanIntermediateNode(node.id, node.name(networkType), meters)
            case None => PlanMessageItem(NodeNotFoundInDatabase(nodeKey.nodeId))
          }

        case routeKey: EncodedRoute =>
          routeMap.get(routeKey.routeId) match {
            case Some(route) =>
              PlanRoute(route.id, route.summary.name, meters /*segments*/)
            case None => PlanMessageItem(RouteNotFoundInDatabase(routeKey.routeId))
          }

        case encodedMessage: EncodedMessage =>
          PlanMessageItem(encodedMessage.message)
      }
    }

    val nodes = planItems.flatMap {
      case nodeItem: PlanNode => nodeMap.get(nodeItem.nodeId)
      case _ => None
    }

    val routes: Seq[RouteInfo] = planItems.flatMap {
      case routeItem: PlanRoute => routeMap.get(routeItem.routeId)
      case _ => None
    }

    val details = PlanJson.build(networkType, nodes, routes)

    Plan(planItems, details)
  }
}
