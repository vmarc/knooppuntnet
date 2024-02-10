package kpn.server.analyzer.engine.monitor.structure

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

  def isRoundabout: Boolean = wayMember.way.tags.has("junction", "roundabout")

  def isUnidirectional: Boolean = {
    wayMember.role.contains("forward") || wayMember.role.contains("backward")
  }

  def connection(otherLink: WayMemberLink): Option[Long] = {
    forwardConnection(otherLink) match {
      case None => None
        backwardConnection(otherLink) match {
          case None => None
          case Some(nodeId) => Some(nodeId)

        }
      case Some(nodeId) => Some(nodeId)
    }
  }

  def forwardConnection(otherLink: WayMemberLink): Option[Long] = {
    forwardConnectableNodeIds.flatMap { nodeId1 =>
      otherLink.forwardConnectableNodeIds
        .filter(nodeId2 => nodeId1 == nodeId2)
        .headOption
    }.headOption
  }

  def backwardConnection(otherLink: WayMemberLink): Option[Long] = {
    forwardConnectableNodeIds.flatMap { nodeId1 =>
      otherLink.backwardConnectableNodeIds
        .filter(nodeId2 => nodeId1 == nodeId2)
        .headOption
    }.headOption
  }

  def canBackwardConnectTo(otherLinkOption: Option[WayMemberLink]): Boolean = {
    otherLinkOption match {
      case None => false
      case Some(otherLink) =>
        backwardConnectableNodeIds.flatMap { nodeId1 =>
          otherLink.backwardConnectableNodeIds
            .filter(nodeId2 => nodeId1 == nodeId2)
            .headOption
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
