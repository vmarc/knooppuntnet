package kpn.core.engine.analysis

import kpn.shared.NetworkType
import kpn.shared.data.Element
import kpn.shared.data.Member
import kpn.shared.data.Node
import kpn.shared.data.NodeMember
import kpn.shared.data.RelationMember
import kpn.shared.data.raw.RawNode
import kpn.shared.data.raw.RawRelation

class Interpreter(val networkType: NetworkType) {

  def isNetworkRelation(relation: RawRelation): Boolean = {
    isElementNetworkRelation(relation)
  }

  def isNetworkRelation(member: Member): Boolean = {
    member match {
      case relationMember: RelationMember => isNetworkRelation(relationMember.relation.raw)
      case _ => false
    }
  }

  private def isElementNetworkRelation(element: Element): Boolean = {
    element.tags.has("type", "network") &&
      element.tags.has("network", networkType.networkTagValue) &&
      element.tags.has("network:type", "node_network")
  }

  def isNetworkNode(member: Member): Boolean = {
    member match {
      case nodeMember: NodeMember => isNetworkNode(nodeMember.node.raw)
      case _ => false
    }
  }

  def isNetworkNode(node: RawNode): Boolean = {
    node.tags.has(networkType.nodeTagKey) &&
      node.tags.has("network:type", "node_network")
  }

  def isRouteRelation(member: Member): Boolean = {
    member match {
      case relationMember: RelationMember => isRouteRelation(relationMember.relation.raw)
      case _ => false
    }
  }

  def isRouteRelation(relation: RawRelation): Boolean = {
    relation.tags.has("network", networkType.name) &&
      relation.tags.has("type", "route") &&
      relation.tags.has("network:type", "node_network")
  }

  def isValidNetworkMember(member: Member): Boolean = {
    member.isWay || isNetworkNode(member)
  }

  def isUnexpectedNode(node: Node): Boolean = {
    !isNetworkNode(node.raw) && !isMap(node)
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
