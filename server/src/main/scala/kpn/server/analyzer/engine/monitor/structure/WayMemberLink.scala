package kpn.server.analyzer.engine.monitor.structure

import kpn.api.common.data.Node
import kpn.api.common.data.WayMember

object WayMemberLink {

  def from(wayMembers: Seq[WayMember]): Seq[WayMemberLink] = {
    var next: Option[WayMemberLink] = None
    val links = wayMembers.reverse.map { wayMember =>
      val link = WayMemberLink(wayMember, next)
      next = Some(link)
      link
    }
    links.reverse
  }

  def apply(wayMember: WayMember, next: Option[WayMemberLink]): WayMemberLink = {
    val nodeIds: Seq[Long] = {
      if (wayMember.role.contains("backward")) {
        wayMember.way.nodes.reverse.map(_.id)
      }
      else {
        wayMember.way.nodes.map(_.id)
      }
    }
    WayMemberLink(wayMember, next, nodeIds)
  }
}

case class WayMemberLink(
  wayMember: WayMember,
  next: Option[WayMemberLink],
  nodeIds: Seq[Long]
) {

  def wayId: Long = {
    wayMember.way.id
  }

  def isClosedLoop: Boolean = {
    val way = wayMember.way
    way.nodes.size > 2 && way.nodes.head == way.nodes.last
  }

  def connectableNodeIds: Seq[Long] = {
    if (isClosedLoop) {
      wayMember.way.nodes.map(_.id).dropRight(1)
    }
    else {
      if (hasRoleForward) {
        Seq(wayMember.way.nodes.head.id)
      }
      else if (hasRoleBackward) {
        Seq(wayMember.way.nodes.last.id)
      }
      else {
        // bidirectional fragment
        Seq(
          wayMember.way.nodes.head.id,
          wayMember.way.nodes.last.id
        )
      }
    }
  }

  def connectableNodeIdsUp: Seq[Long] = {
    if (isClosedLoop) {
      wayMember.way.nodes.map(_.id).dropRight(1)
    }
    else {
      if (hasRoleForward) {
        Seq(wayMember.way.nodes.last.id)
      }
      else if (hasRoleBackward) {
        Seq(wayMember.way.nodes.head.id)
      }
      else {
        // bidirectional fragment
        Seq(
          wayMember.way.nodes.head.id,
          wayMember.way.nodes.last.id
        )
      }
    }
  }

  def hasRoleForward: Boolean = wayMember.role.contains("forward")

  def hasRoleBackward: Boolean = wayMember.role.contains("backward")

  def isRoundabout: Boolean = wayMember.way.tags.has("junction", "roundabout")

  def isUnidirectional: Boolean = {
    hasRoleForward || hasRoleBackward
  }

  def startNode: Node = {
    if (hasRoleBackward) {
      wayMember.way.nodes.last
    }
    else {
      wayMember.way.nodes.head
    }
  }

  def endNode: Node = {
    if (hasRoleBackward) {
      wayMember.way.nodes.head
    }
    else {
      wayMember.way.nodes.last
    }
  }

  def connection(otherLink: WayMemberLink): Option[Long] = {
    connectableNodeIds.flatMap { nodeId1 =>
      otherLink.connectableNodeIds
        .filter(nodeId2 => nodeId1 == nodeId2)
        .headOption
    }.headOption
  }

  def connectionUp(otherLink: WayMemberLink): Option[Long] = {
    connectableNodeIds.flatMap { nodeId1 =>
      otherLink.connectableNodeIdsUp
        .filter(nodeId2 => nodeId1 == nodeId2)
        .headOption
    }.headOption
  }

  def canConnectUpTo(otherLinkOption: Option[WayMemberLink]): Boolean = {
    otherLinkOption match {
      case None => false
      case Some(otherLink) =>
        connectableNodeIdsUp.flatMap { nodeId1 =>
          otherLink.connectableNodeIdsUp
            .filter(nodeId2 => nodeId1 == nodeId2)
            .headOption
        }.nonEmpty
    }
  }
}
