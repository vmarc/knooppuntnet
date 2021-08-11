package kpn.core.analysis

import kpn.api.common.data.Element
import kpn.api.common.data.Member
import kpn.api.common.data.Node
import kpn.api.common.data.NodeMember
import kpn.api.common.data.RelationMember
import kpn.api.common.data.Tagable
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags

object TagInterpreter {

  def isRouteRelation(tags: Tags): Boolean = {
    tags.has("network:type", "node_network") &&
      tags.has("type", "route") && {
      tags("network") match {
        case Some(value) => ScopedNetworkType.all.map(_.key).contains(value)
        case None => false
      }
    }
  }

  def isNetworkRelation(tags: Tags): Boolean = {
    tags.has("network:type", "node_network") &&
      tags.has("type", "network") && {
      tags("network") match {
        case Some(value) => ScopedNetworkType.all.map(_.key).contains(value)
        case None => false
      }
    }
  }

  def isNetworkNode(tags: Tags): Boolean = {
    tags.has("network:type", "node_network") &&
      ScopedNetworkType.all.exists(scopedNetworkType => hasNodeTagKey(scopedNetworkType, tags))
  }

  def isNetworkNode(tags: Tags, networkType: NetworkType): Boolean = {
    tags.has("network:type", "node_network") &&
      networkType.scopedNetworkTypes.exists(scopedNetworkType => hasNodeTagKey(scopedNetworkType, tags))
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

  def isValidNetworkNode(node: RawNode): Boolean = {
    NetworkType.all.exists(networkType => isValidNetworkNode(networkType, node))
  }

  /*
     Returns true is given node matches the conditions to be considered a node network node. Use
     this method when checking a node that is a member of a network or route relation.
   */
  def isReferencedNetworkNode(scopedNetworkType: ScopedNetworkType, node: RawNode): Boolean = {
    hasNodeTagKey(scopedNetworkType, node.tags) && hasNetworkTypeNodeNetworkTag(node)
  }

  /*
     Returns true is given node matches the conditions to be considered a node network node. Use
     this method when checking a 'standalone' node (not member of a network or route relation).

     If the node is not part of a known network or route relation, than we want it to be a known
     node.
   */
  def isValidNetworkNode(networkType: NetworkType, node: RawNode): Boolean = {
    val hasAnyNodeTagKey = ScopedNetworkType.all.filter(_.networkType == networkType).exists { scopedNetworkType =>
      hasNodeTagKey(scopedNetworkType, node.tags)
    }
    hasAnyNodeTagKey && hasNetworkTypeNodeNetworkTag(node)
  }

  def isValidRouteRelation(relation: RawRelation): Boolean = {
    NetworkType.all.exists(networkType => isValidRouteRelation(networkType, relation))
  }

  def isValidRouteRelation(networkType: NetworkType, member: Member): Boolean = {
    member match {
      case relationMember: RelationMember => isValidRouteRelation(networkType, relationMember.relation.raw)
      case _ => false
    }
  }

  def isReferencedRouteRelation(relation: RawRelation): Boolean = {
    ScopedNetworkType.all.exists(networkType => isReferencedRouteRelation(networkType, relation))
  }

  def isReferencedRouteRelation(scopedNetworkType: ScopedNetworkType, member: Member): Boolean = {
    member match {
      case relationMember: RelationMember => isReferencedRouteRelation(scopedNetworkType, relationMember.relation.raw)
      case _ => false
    }
  }

  def isReferencedRouteRelation(scopedNetworkType: ScopedNetworkType, relation: RawRelation): Boolean = {
    hasScopedNetworkTag(scopedNetworkType, relation) &&
      relation.tags.has("type", "route") &&
      hasNetworkTypeNodeNetworkTag(relation)
  }

  def isValidRouteRelation(networkType: NetworkType, relation: RawRelation): Boolean = {
    hasNetworkTag(networkType, relation) &&
      relation.tags.has("type", "route") &&
      hasNetworkTypeNodeNetworkTag(relation)
  }

  def isValidNetworkMember(scopedNetworkType: ScopedNetworkType, member: Member): Boolean = {
    val isNodeMember: Boolean = member match {
      case nodeMember: NodeMember => isReferencedNetworkNode(scopedNetworkType, nodeMember.node.raw)
      case _ => false
    }
    member.isWay || isNodeMember
  }

  def isUnexpectedNode(scopedNetworkType: ScopedNetworkType, node: Node): Boolean = {
    !isReferencedNetworkNode(scopedNetworkType, node.raw) && !isMap(node)
  }

  private def isMap(node: Node): Boolean = {
    node.tags.has("tourism", "information") &&
      (node.tags.has("information", "map") || node.tags.has("information", "guidepost"))
  }

  private def hasNetworkTag(networkType: NetworkType, element: Tagable): Boolean = {
    ScopedNetworkType.all.filter(_.networkType == networkType).exists { scopedNetworkType =>
      element.tags.has("network", scopedNetworkType.key)
    }
  }

  private def hasScopedNetworkTag(scopedNetworkType: ScopedNetworkType, element: Tagable): Boolean = {
    element.tags.has("network", scopedNetworkType.key)
  }

  private def hasNetworkTypeNodeNetworkTag(element: Tagable): Boolean = {
    element.tags.has("network:type", "node_network")
  }

  private def isElementNetworkRelation(networkType: NetworkType, element: Element): Boolean = {
    element.tags.has("type", "network") &&
      hasNetworkTag(networkType, element) &&
      hasNetworkTypeNodeNetworkTag(element)
  }

  private def hasNodeTagKey(scopedNetworkType: ScopedNetworkType, tags: Tags): Boolean = {
    tags.has(scopedNetworkType.nodeRefTagKey) ||
      tags.has(scopedNetworkType.proposedNodeRefTagKey) ||
      tags.has(scopedNetworkType.nodeNameTagKey) ||
      tags.has(scopedNetworkType.proposedNodeNameTagKey)
  }

}
