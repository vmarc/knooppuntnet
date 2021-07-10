package kpn.server.analyzer.engine.context

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
import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.changes.data.AnalysisData

object AnalysisContext {
  val networkTypeTaggingStart: Timestamp = Timestamp(2019, 11, 1)
  val networkTypeTaggingStartSnapshotFilename: String = "/kpn/conf/snapshot.json"
}

class AnalysisContext(
  val snapshotKnownElements: Elements = Elements(), // nodes:routes/networks known on 2019-11-01
  val oldTagging: Boolean = false,
  var beforeNetworkTypeTaggingStart: Boolean = false
) {

  val data: AnalysisData = AnalysisData()
  var knownElements: Elements = Elements()

  def addKnownNode(nodeId: Long): Unit = {
    this.synchronized {
      knownElements = knownElements.copy(nodeIds = knownElements.nodeIds + nodeId)
    }
  }

  def addKnownRoute(routeId: Long): Unit = {
    this.synchronized {
      knownElements = knownElements.copy(routeIds = knownElements.routeIds + routeId)
    }
  }

  def addKnownNetwork(networkId: Long): Unit = {
    this.synchronized {
      knownElements = knownElements.copy(networkIds = knownElements.networkIds + networkId)
    }
  }

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
        hasNetworkTag(networkType, element)
    }
    else if (beforeNetworkTypeTaggingStart) {
      element.tags.has("type", "network") &&
        hasNetworkTag(networkType, element) &&
        (hasNetworkTypeTag(element) || isKnownNetwork(element))
    }
    else {
      element.tags.has("type", "network") &&
        hasNetworkTag(networkType, element) &&
        hasNetworkTypeTag(element)
    }
  }

  def isValidNetworkNode(node: RawNode): Boolean = {
    NetworkType.all.exists(networkType => isValidNetworkNode(networkType, node))
  }

  def isReferencedNetworkNode(node: RawNode): Boolean = {
    NetworkType.all.exists(networkType => isReferencedNetworkNode(networkType, node))
  }

  /*
     Returns true is given node matches the conditions to be considered a node network node. Use
     this method when checking a node that is a member of a network or route relation.
   */
  def isReferencedNetworkNode(networkType: NetworkType, node: RawNode): Boolean = {
    if (oldTagging || beforeNetworkTypeTaggingStart) {
      isNodeNetworkType(networkType, node)
    }
    else {
      isNodeNetworkType(networkType, node) && hasNetworkTypeTag(node)
    }
  }

  /*
     Returns true is given node matches the conditions to be considered a node network node. Use
     this method when checking a 'standalone' node (not member of a network or route relation).

     If the node is not part of a known network or route relation, than we want it to be a known
     node.
   */
  def isValidNetworkNode(networkType: NetworkType, node: RawNode): Boolean = {
    if (oldTagging) {
      isNodeNetworkType(networkType, node)
    }
    else if (beforeNetworkTypeTaggingStart) {
      isNodeNetworkType(networkType, node) && (hasNetworkTypeTag(node) || isKnownNode(node))
    }
    else {
      isNodeNetworkType(networkType, node) && hasNetworkTypeTag(node)
    }
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
    NetworkType.all.exists(networkType => isReferencedRouteRelation(networkType, relation))
  }

  def isReferencedRouteRelation(networkType: NetworkType, member: Member): Boolean = {
    member match {
      case relationMember: RelationMember => isReferencedRouteRelation(networkType, relationMember.relation.raw)
      case _ => false
    }
  }

  def isReferencedRouteRelation(networkType: NetworkType, relation: RawRelation): Boolean = {
    if (oldTagging || beforeNetworkTypeTaggingStart) {
      hasNetworkTag(networkType, relation) &&
        relation.tags.has("type", "route")
    }
    else {
      hasNetworkTag(networkType, relation) &&
        relation.tags.has("type", "route") &&
        hasNetworkTypeTag(relation)
    }
  }

  def isValidRouteRelation(networkType: NetworkType, relation: RawRelation): Boolean = {
    if (oldTagging) {
      hasNetworkTag(networkType, relation) &&
        relation.tags.has("type", "route")
    }
    else if (beforeNetworkTypeTaggingStart) {
      hasNetworkTag(networkType, relation) &&
        relation.tags.has("type", "route") &&
        (hasNetworkTypeTag(relation) || isKnownRoute(relation))
    }
    else {
      hasNetworkTag(networkType, relation) &&
        relation.tags.has("type", "route") &&
        hasNetworkTypeTag(relation)
    }
  }

  def isValidNetworkMember(networkType: NetworkType, member: Member): Boolean = { // TODO this method does not belong here???
    val isNodeMember = member match {
      case nodeMember: NodeMember => isReferencedNetworkNode(networkType, nodeMember.node.raw)
      case _ => false
    }
    member.isWay || isNodeMember
  }

  def isUnexpectedNode(networkType: NetworkType, node: Node): Boolean = {
    !isReferencedNetworkNode(networkType, node.raw) && !isMap(node)
  }

  private def isMap(node: Node): Boolean = {
    node.tags.has("tourism", "information") &&
      (node.tags.has("information", "map") || node.tags.has("information", "guidepost"))
  }

  private def isNodeNetworkType(networkType: NetworkType, node: RawNode): Boolean = {
    ScopedNetworkType.all.filter(_.networkType == networkType).exists { scopedNetworkType =>
      node.tags.has(scopedNetworkType.nodeTagKey) || node.tags.has(scopedNetworkType.proposedNodeTagKey)
    }
  }

  private def isKnownNode(node: RawNode): Boolean = {
    snapshotKnownElements.nodeIds.contains(node.id) || knownElements.nodeIds.contains(node.id)
  }

  private def isKnownNetwork(element: Element): Boolean = {
    snapshotKnownElements.networkIds.contains(element.id) || knownElements.networkIds.contains(element.id)
  }

  private def isKnownRoute(relation: RawRelation): Boolean = {
    snapshotKnownElements.routeIds.contains(relation.id) || knownElements.routeIds.contains(relation.id)
  }

  private def hasNetworkTag(networkType: NetworkType, element: Tagable): Boolean = {
    ScopedNetworkType.all.filter(_.networkType == networkType).exists { scopedNetworkType =>
      element.tags.has("network", scopedNetworkType.key)
    }
  }

  private def hasNetworkTypeTag(element: Tagable): Boolean = {
    element.tags.has("network:type", "node_network")
  }
}
