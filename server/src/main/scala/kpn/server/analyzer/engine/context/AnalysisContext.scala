package kpn.server.analyzer.engine.context

import kpn.server.analyzer.engine.changes.data.AnalysisData
import kpn.shared.NetworkType
import kpn.shared.ScopedNetworkType
import kpn.shared.Timestamp
import kpn.shared.data.Element
import kpn.shared.data.Member
import kpn.shared.data.Node
import kpn.shared.data.NodeMember
import kpn.shared.data.RelationMember
import kpn.shared.data.Tagable
import kpn.shared.data.raw.RawNode
import kpn.shared.data.raw.RawRelation

object AnalysisContext {
  val networkTypeTaggingStart: Timestamp = Timestamp(2019, 11, 1)
  val networkTypeTaggingStartSnapshotFilename: String = "/kpn/conf/snapshot.json"
}

class AnalysisContext(oldTagging: Boolean = false, beforeNetworkTypeTaggingStart: Boolean = false) {

  val data: AnalysisData = AnalysisData()
  val snapshotKnownElements: Elements = Elements()
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
      isNodeNetworkType(networkType, node)
    }
    else if (beforeNetworkTypeTaggingStart) {
      isNodeNetworkType(networkType, node) && (hasNetworkTypeTag(node) || isKnownNode(node))
    }
    else {
      isNodeNetworkType(networkType, node) && hasNetworkTypeTag(node)
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

  private def isNodeNetworkType(networkType: NetworkType, node: RawNode): Boolean = {
    ScopedNetworkType.all.filter(_.networkType == networkType).exists { scopedNetworkType =>
      node.tags.has(scopedNetworkType.nodeTagKey)
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
