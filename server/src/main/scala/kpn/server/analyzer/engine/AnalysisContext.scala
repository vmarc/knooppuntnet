package kpn.server.analyzer.engine

import kpn.server.analyzer.engine.changes.data.AnalysisData
import kpn.shared.NetworkType
import kpn.shared.data.Element
import kpn.shared.data.Member
import kpn.shared.data.Node
import kpn.shared.data.NodeMember
import kpn.shared.data.RelationMember
import kpn.shared.data.raw.RawNode
import kpn.shared.data.raw.RawRelation

class AnalysisContext(oldTagging: Boolean = false) {

  val data: AnalysisData = AnalysisData()

  def isNetworkRelation(relation: RawRelation): Boolean = {
    NetworkType.all.exists(networkType => isNetworkRelation(networkType, relation))
  }

  def isNetworkRelation(networkType: NetworkType, relation: RawRelation): Boolean = {
    isElementNetworkRelation(networkType, relation)
  }

  def isNetworkRelation(networkType: NetworkType, member: Member): Boolean = {
    member match {
      case relationMember: RelationMember => isNetworkRelation(networkType, relationMember.relation.raw)
      case _ => false
    }
  }

  private def isElementNetworkRelation(networkType: NetworkType, element: Element): Boolean = {
    if (oldTagging) {
      element.tags.has("type", "network") &&
        element.tags.has("network", networkType.networkTagValue)
    }
    else {
      element.tags.has("type", "network") &&
        element.tags.has("network", networkType.networkTagValue) &&
        element.tags.has("network:type", "node_network")
    }
  }

  def isNetworkNode(node: RawNode): Boolean = {
    NetworkType.all.exists(networkType => isNetworkNode(networkType, node))
  }

  def isNetworkNode(networkType: NetworkType, member: Member): Boolean = {
    member match {
      case nodeMember: NodeMember => isNetworkNode(networkType, nodeMember.node.raw)
      case _ => false
    }
  }

  def isNetworkNode(networkType: NetworkType, node: RawNode): Boolean = {
    if (oldTagging) {
      node.tags.has(networkType.nodeTagKey)
    }
    else {
      node.tags.has(networkType.nodeTagKey) &&
        node.tags.has("network:type", "node_network")
    }
  }

  def isRouteRelation(relation: RawRelation): Boolean = {
    NetworkType.all.exists(networkType => isRouteRelation(networkType, relation))
  }

  def isRouteRelation(networkType: NetworkType, member: Member): Boolean = {
    member match {
      case relationMember: RelationMember => isRouteRelation(networkType, relationMember.relation.raw)
      case _ => false
    }
  }

  def isRouteRelation(networkType: NetworkType, relation: RawRelation): Boolean = {
    if (oldTagging) {
      relation.tags.has("network", networkType.name) &&
        relation.tags.has("type", "route")
    }
    else {
      relation.tags.has("network", networkType.name) &&
        relation.tags.has("type", "route") &&
        relation.tags.has("network:type", "node_network")
    }
  }

  def isValidNetworkMember(networkType: NetworkType, member: Member): Boolean = { // TODO this method does not belong here???
    member.isWay || isNetworkNode(networkType, member)
  }

  def isUnexpectedNode(networkType: NetworkType, node: Node): Boolean = { // TODO this method does not belong here???
    !isNetworkNode(networkType, node.raw) && !isMap(node)
  }

  private def isMap(member: Member): Boolean = {
    member match {
      case nodeMember: NodeMember => isMap(nodeMember.node)
      case _ => false
    }
  }

  private def isMap(node: Node): Boolean = {
    node.tags.has("tourism", "information") &&
      (node.tags.has("information", "map") || node.tags.has("information", "guidepost"))
  }

}
