package kpn.server.analyzer.engine.analysis.route.domain

import kpn.api.common.data.Member

object WayMemberLink {

  def from(wayMembers: Seq[Member]): Seq[WayMemberLink] = {
    var next: Option[WayMemberLink] = None
    val links = wayMembers.reverse.map { wayMember =>
      val link = WayMemberLink(wayMember, next)
      next = Some(link)
      link
    }
    links.reverse
  }

  def apply(wayMember: Member, next: Option[WayMemberLink]): WayMemberLink = {
    val nodeIds: Seq[Long] = {
      if (wayMember.role.contains("backward")) {
        wayMember.wayNodes.reverse.map(_.id)
      }
      else {
        wayMember.wayNodes.map(_.id)
      }
    }
    WayMemberLink(wayMember, next, nodeIds)
  }
}

case class WayMemberLink(
  wayMember: Member,
  next: Option[WayMemberLink],
  nodeIds: Seq[Long]
) {

  def wayId: Long = {
    wayMember.memberId
  }

  def isClosedLoop: Boolean = {
    val wayNodes = wayMember.wayNodes
    wayNodes.sizeIs > 2 && wayNodes.head == wayNodes.last
  }

  def isRoundabout: Boolean = wayMember.hasTag("junction", "roundabout")

  def isUnidirectional: Boolean = {
    wayMember.role.contains("forward") || wayMember.role.contains("backward")
  }

  def connection(otherLink: WayMemberLink): Option[Long] = {
    forwardConnection(otherLink) match {
      case None => backwardConnection(otherLink)
      case Some(nodeId) => Some(nodeId)
    }
  }

  def forwardConnection(otherLink: WayMemberLink): Option[Long] = {
    forwardConnectableNodeIds.flatMap { nodeId1 =>
      otherLink.forwardConnectableNodeIds.find(nodeId2 => nodeId1 == nodeId2)
    }.headOption
  }

  def backwardConnection(otherLink: WayMemberLink): Option[Long] = {
    forwardConnectableNodeIds.flatMap { nodeId1 =>
      otherLink.backwardConnectableNodeIds.find(nodeId2 => nodeId1 == nodeId2)
    }.headOption
  }

  def canBackwardConnectTo(otherLinkOption: Option[WayMemberLink]): Boolean = {
    otherLinkOption match {
      case None => false
      case Some(otherLink) =>
        backwardConnectableNodeIds.flatMap { nodeId1 =>
          otherLink.backwardConnectableNodeIds.find(nodeId2 => nodeId1 == nodeId2)
        }.nonEmpty
    }
  }

  private def forwardConnectableNodeIds: Seq[Long] = {
    if (isClosedLoop) {
      nodeIds.dropRight(1)
    }
    else {
      if (isUnidirectional) {
        Seq(nodeIds.head)
      }
      else {
        // bidirectional fragment
        Seq(
          nodeIds.head,
          nodeIds.last
        )
      }
    }
  }

  private def backwardConnectableNodeIds: Seq[Long] = {
    if (isClosedLoop) {
      nodeIds.dropRight(1)
    }
    else {
      if (isUnidirectional) {
        Seq(nodeIds.last)
      }
      else {
        // bidirectional fragment
        Seq(
          nodeIds.head,
          nodeIds.last
        )
      }
    }
  }
}
