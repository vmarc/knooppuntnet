package kpn.core.analysis

import kpn.api.common.NetworkType
import kpn.api.common.data.Element
import kpn.api.common.data.Member
import kpn.api.common.data.Node
import kpn.api.common.data.NodeMember
import kpn.api.common.data.RelationMember
import kpn.api.common.data.Tagable
import kpn.api.custom.Relation
import kpn.api.custom.ScopedNetworkType

object TagInterpreter {

  def isRouteRelation(tagable: Tagable): Boolean = {
    tagable.hasTag("network:type", "node_network") &&
      tagable.hasTag("type", "route") && {
      tagable.tagValue("network") match {
        case Some(value) => ScopedNetworkType.all.map(_.key).contains(value)
        case None => false
      }
    }
  }

  def isNetworkRelation(tagable: Tagable): Boolean = {
    tagable.hasTag("network:type", "node_network") &&
      tagable.hasTag("type", "network") && {
      tagable.tagValue("network") match {
        case Some(value) => ScopedNetworkType.all.map(_.key).contains(value)
        case None => false
      }
    }
  }

  def isNetworkNode(tagable: Tagable): Boolean = {
    tagable.hasTag("network:type", "node_network") &&
      ScopedNetworkType.all.exists(scopedNetworkType => hasNodeTagKey(scopedNetworkType, tagable))
  }

  def isNetworkNode(tagable: Tagable, networkType: NetworkType): Boolean = {
    tagable.hasTag("network:type", "node_network") &&
      ScopedNetworkType.all.filter(_.networkType == networkType).exists(scopedNetworkType => hasNodeTagKey(scopedNetworkType, tagable))
  }

  def isNetworkRelation(networkType: NetworkType, relation: Relation): Boolean = {
    isElementNetworkRelation(networkType, relation)
  }

  def isNetworkRelation(networkType: NetworkType, member: Member): Boolean = {
    member match {
      case relationMember: RelationMember => isNetworkRelation(networkType, relationMember.relation)
      case _ => false
    }
  }

  def isValidNetworkNode(node: Node): Boolean = {
    NetworkType.values.exists(networkType => isValidNetworkNode(networkType, node))
  }

  /*
     Returns true is given node matches the conditions to be considered a node network node. Use
     this method when checking a node that is a member of a network or route relation.
   */
  def isReferencedNetworkNode(scopedNetworkType: ScopedNetworkType, node: Node): Boolean = {
    hasNodeTagKey(scopedNetworkType, node) && hasNetworkTypeNodeNetworkTag(node)
  }

  /*
     Returns true is given node matches the conditions to be considered a node network node. Use
     this method when checking a 'standalone' node (not member of a network or route relation).

     If the node is not part of a known network or route relation, than we want it to be a known
     node.
   */
  def isValidNetworkNode(networkType: NetworkType, node: Node): Boolean = {
    val hasAnyNodeTagKey = ScopedNetworkType.all.filter(_.networkType == networkType).exists { scopedNetworkType =>
      hasNodeTagKey(scopedNetworkType, node)
    }
    hasAnyNodeTagKey && hasNetworkTypeNodeNetworkTag(node)
  }

  def isReferencedRouteRelation(relation: Relation): Boolean = {
    ScopedNetworkType.all.exists(networkType => isReferencedRouteRelation(networkType, relation))
  }

  def isReferencedRouteRelation(scopedNetworkType: ScopedNetworkType, member: Member): Boolean = {
    member match {
      case relationMember: RelationMember => isReferencedRouteRelation(scopedNetworkType, relationMember.relation)
      case _ => false
    }
  }

  def isReferencedRouteRelation(scopedNetworkType: ScopedNetworkType, relation: Relation): Boolean = {
    hasScopedNetworkTag(scopedNetworkType, relation) &&
      relation.hasTag("type", "route") &&
      hasNetworkTypeNodeNetworkTag(relation)
  }

  def isValidNetworkMember(scopedNetworkType: ScopedNetworkType, member: Member): Boolean = {
    val isNodeMember: Boolean = member match {
      case nodeMember: NodeMember => isReferencedNetworkNode(scopedNetworkType, nodeMember.node)
      case _ => false
    }
    member.isWay || isNodeMember
  }

  def isUnexpectedNode(scopedNetworkType: ScopedNetworkType, node: Node): Boolean = {
    !isReferencedNetworkNode(scopedNetworkType, node) && !isMap(node)
  }

  def expectedRouteRelationCount(scopedNetworkType: ScopedNetworkType, tagable: Tagable): Option[Long] = {
    tagable.tagValue(scopedNetworkType.expectedRouteRelationsTag) match {
      case None => None
      case Some(value) =>
        if (!value.forall(_.isDigit)) {
          Some(0)
        }
        else {
          Some(value.toLong)
        }
    }
  }

  def isProposedNode(scopedNetworkType: ScopedNetworkType, tagable: Tagable): Boolean = {
    tagable.hasTag("state", "proposed") ||
      tagable.hasTag(scopedNetworkType.proposedNodeRefTagKey) ||
      tagable.hasTag(scopedNetworkType.proposedNodeNameTagKey)
  }

  private def isMap(node: Node): Boolean = {
    node.hasTag("tourism", "information") &&
      (node.hasTag("information", "map") || node.hasTag("information", "guidepost", "board", "route_marker"))
  }

  private def hasNetworkTag(networkType: NetworkType, element: Tagable): Boolean = {
    ScopedNetworkType.all.filter(_.networkType == networkType).exists { scopedNetworkType =>
      element.hasTag("network", scopedNetworkType.key)
    }
  }

  private def hasScopedNetworkTag(scopedNetworkType: ScopedNetworkType, element: Tagable): Boolean = {
    element.hasTag("network", scopedNetworkType.key)
  }

  private def hasNetworkTypeNodeNetworkTag(element: Tagable): Boolean = {
    element.hasTag("network:type", "node_network")
  }

  private def isElementNetworkRelation(networkType: NetworkType, element: Element): Boolean = {
    element.hasTag("type", "network") &&
      hasNetworkTag(networkType, element) &&
      hasNetworkTypeNodeNetworkTag(element)
  }

  private def hasNodeTagKey(scopedNetworkType: ScopedNetworkType, tagable: Tagable): Boolean = {
    tagable.hasTag(scopedNetworkType.nodeRefTagKey) ||
      tagable.hasTag(scopedNetworkType.proposedNodeRefTagKey) ||
      tagable.hasTag(scopedNetworkType.nodeNameTagKey) ||
      tagable.hasTag(scopedNetworkType.proposedNodeNameTagKey)
  }
}
