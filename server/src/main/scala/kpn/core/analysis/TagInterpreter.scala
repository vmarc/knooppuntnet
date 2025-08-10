package kpn.core.analysis

import kpn.api.common.RouteType
import kpn.api.common.data.Element
import kpn.api.common.data.Member
import kpn.api.common.data.Node
import kpn.api.common.data.Tagable
import kpn.api.custom.Relation
import kpn.api.custom.ScopedRouteType

object TagInterpreter {

  def isRouteRelation(tagable: Tagable): Boolean = {
    tagable.hasTag("network:type", "node_network") &&
      tagable.hasTag("type", "route") && {
      tagable.tagValue("network") match {
        case Some(value) => ScopedRouteType.all.map(_.key).contains(value)
        case None => false
      }
    }
  }

  def isNetworkRelation(tagable: Tagable): Boolean = {
    tagable.hasTag("network:type", "node_network") &&
      tagable.hasTag("type", "network") && {
      tagable.tagValue("network") match {
        case Some(value) => ScopedRouteType.all.map(_.key).contains(value)
        case None => false
      }
    }
  }

  def isNetworkNode(tagable: Tagable): Boolean = {
    tagable.hasTag("network:type", "node_network") &&
      ScopedRouteType.all.exists(scopedRouteType => hasNodeTagKey(scopedRouteType, tagable))
  }

  def isNetworkNode(tagable: Tagable, routeType: RouteType): Boolean = {
    tagable.hasTag("network:type", "node_network") &&
      ScopedRouteType.all.filter(_.routeType == routeType).exists(scopedRouteType => hasNodeTagKey(scopedRouteType, tagable))
  }

  def isNetworkRelation(routeType: RouteType, relation: Relation): Boolean = {
    isElementNetworkRelation(routeType, relation)
  }

  def isNetworkRelation(routeType: RouteType, member: Member): Boolean = {
    member.relation match {
      case Some(relation) => isNetworkRelation(routeType, relation)
      case _ => false
    }
  }

  def isValidNetworkNode(node: Tagable): Boolean = {
    RouteType.values.exists(routeType => isValidNetworkNode(routeType, node))
  }

  /*
     Returns true is given node matches the conditions to be considered a node network node. Use
     this method when checking a node that is a member of a network or route relation.
   */
  def isReferencedNetworkNode(scopedRouteType: ScopedRouteType, node: Node): Boolean = {
    hasNodeTagKey(scopedRouteType, node) && hasRouteTypeNodeNetworkTag(node)
  }

  /*
     Returns true is given node matches the conditions to be considered a node network node. Use
     this method when checking a 'standalone' node (not member of a network or route relation).

     If the node is not part of a known network or route relation, than we want it to be a known
     node.
   */
  def isValidNetworkNode(routeType: RouteType, node: Tagable): Boolean = {
    val hasAnyNodeTagKey = ScopedRouteType.all.filter(_.routeType == routeType).exists { scopedRouteType =>
      hasNodeTagKey(scopedRouteType, node)
    }
    hasAnyNodeTagKey && hasRouteTypeNodeNetworkTag(node)
  }

  def isReferencedRouteRelation(relation: Relation): Boolean = {
    ScopedRouteType.all.exists(routeType => isReferencedRouteRelation(routeType, relation))
  }

  def isReferencedRouteRelation(scopedRouteType: ScopedRouteType, member: Member): Boolean = {
    member.relation match {
      case Some(relation) => isReferencedRouteRelation(scopedRouteType, relation)
      case _ => false
    }
  }

  def isReferencedRouteRelation(scopedRouteType: ScopedRouteType, relation: Relation): Boolean = {
    hasScopedNetworkTag(scopedRouteType, relation) &&
      relation.hasTag("type", "route") &&
      hasRouteTypeNodeNetworkTag(relation)
  }

  def isValidNetworkMember(scopedRouteType: ScopedRouteType, member: Member): Boolean = {
    val isNodeMember: Boolean = member.node match {
      case Some(node) => isReferencedNetworkNode(scopedRouteType, node)
      case _ => false
    }
    member.isWay || isNodeMember
  }

  def isUnexpectedNode(scopedRouteType: ScopedRouteType, node: Node): Boolean = {
    !isReferencedNetworkNode(scopedRouteType, node) && !isMap(node)
  }

  def expectedRouteRelationCount(scopedRouteType: ScopedRouteType, tagable: Tagable): Option[Long] = {
    tagable.tagValue(scopedRouteType.expectedRouteRelationsTag).map { value =>
      if (!value.forall(_.isDigit)) {
        0
      }
      else {
        value.toLong
      }
    }
  }

  def isProposedNode(scopedRouteType: ScopedRouteType, tagable: Tagable): Boolean = {
    tagable.hasTag("state", "proposed") ||
      tagable.hasTag(scopedRouteType.proposedNodeRefTagKey) ||
      tagable.hasTag(scopedRouteType.proposedNodeNameTagKey)
  }

  private def isMap(node: Node): Boolean = {
    node.hasTag("tourism", "information") &&
      (node.hasTag("information", "map") || node.hasTag("information", "guidepost", "board", "route_marker"))
  }

  private def hasNetworkTag(routeType: RouteType, element: Tagable): Boolean = {
    ScopedRouteType.all.filter(_.routeType == routeType).exists { scopedRouteType =>
      element.hasTag("network", scopedRouteType.key)
    }
  }

  private def hasScopedNetworkTag(scopedRouteType: ScopedRouteType, element: Tagable): Boolean = {
    element.hasTag("network", scopedRouteType.key)
  }

  private def hasRouteTypeNodeNetworkTag(element: Tagable): Boolean = {
    element.hasTag("network:type", "node_network")
  }

  private def isElementNetworkRelation(routeType: RouteType, element: Element): Boolean = {
    element.hasTag("type", "network") &&
      hasNetworkTag(routeType, element) &&
      hasRouteTypeNodeNetworkTag(element)
  }

  private def hasNodeTagKey(scopedRouteType: ScopedRouteType, tagable: Tagable): Boolean = {
    tagable.hasTag(scopedRouteType.nodeRefTagKey) ||
      tagable.hasTag(scopedRouteType.proposedNodeRefTagKey) ||
      tagable.hasTag(scopedRouteType.nodeNameTagKey) ||
      tagable.hasTag(scopedRouteType.proposedNodeNameTagKey)
  }
}
