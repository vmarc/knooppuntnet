package kpn.server.analyzer.engine.monitor.structure

import kpn.api.common.data.Member
import kpn.api.common.data.WayMember

import scala.collection.mutable

object StructureElementAnalyzer {
  def analyze(members: Seq[Member], traceEnabled: Boolean = false): Seq[StructureElementGroup] = {
    val wayMembers = members.flatMap { member =>
      member match {
        case wayMember: WayMember => Some(wayMember)
        case _ => None
      }
    }
    if (wayMembers.exists(_.way.nodes.length < 2)) {
      throw new IllegalStateException("ways with less that 2 nodes should have been filtered out at this point")
    }
    new StructureElementAnalyzer(wayMembers, traceEnabled).analyze()
  }
}

class StructureElementAnalyzer(wayMembers: Seq[WayMember], traceEnabled: Boolean = false) {

  private var elementDirection: Option[ElementDirection.Value] = None

  private val elementGroups = mutable.Buffer[StructureElementGroup]()
  private val elements = mutable.Buffer[StructureElement]()
  private val currentElementFragments = mutable.Buffer[StructureFragment]()

  private var lastDownFragment: Option[StructureFragment] = None
  private var lastUpFragment: Option[StructureFragment] = None

  def analyze(): Seq[StructureElementGroup] = {
    val links = WayMemberLink.from(wayMembers)
    links.zipWithIndex.foreach { case (link, index) =>
      if (traceEnabled) {
        trace(s"${index + 1} way ${link.wayId} role=${link.wayMember.role}")
      }
      processWayMember(link)
    }
    finalizeCurrentGroup()
    elementGroups.toSeq
  }

  private def processWayMember(link: WayMemberLink): Unit = {
    if (link.isClosedLoop) {
      processClosedLoop(link)
    }
    else if (link.isUnidirectional || link.isRoundabout /* TODO different for hiking routes? */ ) {
      processUnidirectionalWay(link)
    }
    else {
      processBidirectionalWay(link)
    }
  }

  private def processUnidirectionalWay(link: WayMemberLink): Unit = {
    elementDirection match {
      case Some(ElementDirection.Down) =>
        processNextUnidirectionalWayDown(link)
      case Some(ElementDirection.Up) =>
        processNextUnidirectionalWayUp(link)
      case _ =>
        processFirstUnidirectionalWay(link)
    }
  }

  private def processBidirectionalWay(link: WayMemberLink): Unit = {
    if (elementDirection.nonEmpty) {
      if (traceEnabled) {
        trace(s"  first way after unidirectional element")
      }
      finalizeCurrentElement()
      elementDirection = None
      addBidirectionalFragment(link)
    }
    else {
      lastDownFragment.map(_.forwardEndNodeId) match {
        case Some(endNodeId) =>
          if (endNodeId == link.startNode.id) {
            addBidirectionalFragment(link.wayMember)
          }
          else if (endNodeId == link.endNode.id) {
            addBidirectionalFragment(link.wayMember, reversed = true)
          }
          else {
            // no connection
            finalizeCurrentGroup()
            addBidirectionalFragment(link)
          }

        case None =>
          // first fragment of this element, look at the next way for connection
          addBidirectionalFragment(link)
      }
    }
  }

  private def processClosedLoop(closedLoopLink: WayMemberLink): Unit = {

    closedLoopLink.next match {
      case None =>
        processClosedLoopAtEndOfRoute(closedLoopLink)

      case Some(nextLink) =>

        lastDownFragment.map(_.forwardEndNodeId) match {
          case None =>

            closedLoopLink.connection(nextLink) match {
              case None =>
                if (traceEnabled) {
                  trace(s"  current closed loop way does not connect to next way")
                }
                finalizeCurrentElement()
                addDownElement(closedLoopLink.wayMember, closedLoopLink.nodeIds)

              case Some(endNodeId) =>

                finalizeCurrentElement()
                val wayNodeIds = closedLoopLink.nodeIds
                // TODO for walking routes, should choose shortest path here instead of adding both forward and backward element
                StructureUtil.closedLoopNodeIds(wayNodeIds.head, endNodeId, wayNodeIds) match {
                  case None => throw new Exception("internal error TODO better message")
                  case Some(nodeIds) =>
                    addDownElement(closedLoopLink.wayMember, nodeIds)
                }
            }

          case Some(lastFragmentEndNodeId) =>

            val startNodeId = if (closedLoopLink.nodeIds.contains(lastFragmentEndNodeId)) {
              lastFragmentEndNodeId
            }
            else {
              // no link to previous down fragment, start new group
              finalizeCurrentGroup()
              // assume first node is start node
              closedLoopLink.nodeIds.head
            }

            closedLoopLink.connection(nextLink) match {
              case None =>
                if (traceEnabled) {
                  trace(s"  current closed loop way does not connect to next way")
                }
                finalizeCurrentElement()
                addDownElement(closedLoopLink.wayMember, closedLoopLink.nodeIds)

              case Some(endNodeId) =>

                finalizeCurrentElement()
                // TODO for walking routes, should choose shortest path here instead of adding both forward and backward element
                StructureUtil.closedLoopNodeIds(startNodeId, endNodeId, closedLoopLink.nodeIds) match {
                  case None => throw new Exception("internal error TODO better message")
                  case Some(nodeIds) =>
                    addDownElement(closedLoopLink.wayMember, nodeIds)
                }
            }
        }

        lastUpFragment.map(_.backwardStartNodeId) match {
          case None =>
            val endNodeId = closedLoopLink.nodeIds.head
            lookRoundaboutAhead(closedLoopLink, endNodeId, nextLink)

          case Some(connectionNodeId) =>
            val endNodeId = if (closedLoopLink.nodeIds.contains(connectionNodeId)) {
              connectionNodeId
            }
            else {
              closedLoopLink.nodeIds.head
            }
            lookRoundaboutAhead(closedLoopLink, endNodeId, nextLink)
        }
    }
  }

  private def lookRoundaboutAhead(closedLoopLink: WayMemberLink, endNodeId: Long, aheadLink: WayMemberLink): Unit = {
    closedLoopLink.connectionUp(aheadLink) match {
      case None =>
        aheadLink.next match {
          case Some(link) =>
            lookRoundaboutAhead(closedLoopLink, endNodeId, link)
          case None =>
            finalizeCurrentGroup()
        }

      case Some(startNodeId) =>

        finalizeCurrentElement()
        val wayNodeIds = closedLoopLink.nodeIds
        // TODO for walking routes, should choose shortest path here instead of adding both forward and backward element
        StructureUtil.closedLoopNodeIds(startNodeId, endNodeId, wayNodeIds) match {
          case None =>
            throw new Exception("internal error TODO better message")
          case Some(nodeIds) =>
            addUpElement(closedLoopLink.wayMember, nodeIds)
        }
    }
  }

  private def processClosedLoopAtEndOfRoute(link: WayMemberLink): Unit = {

    finalizeCurrentElement()
    lastDownFragment.map(_.forwardEndNodeId) match {
      case None =>
        // this closed loop is both the start and the end of the route in forward direction
        addDownElement(link.wayMember, link.nodeIds)

      case Some(startNodeId) =>

        StructureUtil.closedLoopNodeIds(startNodeId, link.nodeIds) match {
          case Some(nodeIds) =>
            addDownElement(link.wayMember, nodeIds)
          case None =>
            finalizeCurrentGroup()
        }
    }

    lastUpFragment.map(_.backwardStartNodeId) match {
      case None =>
        // this closed loop is both the start and the end of the route in forward direction
        addUpElement(link.wayMember, link.nodeIds)

      case Some(startNodeId) =>
        StructureUtil.closedLoopNodeIds(startNodeId, link.nodeIds) match {
          case Some(nodeIds) =>
            addUpElement(link.wayMember, nodeIds)
          case None =>
            finalizeCurrentGroup()
        }
    }
  }

  private def addBidirectionalFragment(link: WayMemberLink): Unit = {
    link.next match {
      case Some(nextLink) =>
        addBidirectionalFragmentByLookingAheadAtNextWayMember(link, nextLink)
      case None =>
        addLastBidirectionalFragment(link)
    }
  }

  private def addBidirectionalFragmentByLookingAheadAtNextWayMember(link: WayMemberLink, nextLink: WayMemberLink): Unit = {

    link.connection(nextLink) match {
      case None =>
        link.connectionUp(nextLink) match {
          case None =>

            if (traceEnabled) {
              trace(s"  current way does not connect to next way")
            }
            addBidirectionalFragment(link.wayMember)
            finalizeCurrentGroup()

          case Some(nodeId) =>
            if (nodeId == link.nodeIds.last) {
              addBidirectionalFragment(link.wayMember)
            }
            else {
              addBidirectionalFragment(link.wayMember, reversed = true)
            }
        }
      case Some(nodeId) =>
        if (nodeId == link.wayMember.way.nodes.last.id) {
          addBidirectionalFragment(link.wayMember)
        }
        else {
          addBidirectionalFragment(link.wayMember, reversed = true)
        }
    }
  }

  private def addLastBidirectionalFragment(link: WayMemberLink): Unit = {

    lastDownFragment match {

      case Some(previousDownFragment) =>

        if (link.startNode.id == previousDownFragment.forwardEndNodeId) {
          addBidirectionalFragment(link.wayMember)
        }
        else if (link.endNode.id == previousDownFragment.forwardEndNodeId) {
          addBidirectionalFragment(link.wayMember, reversed = true)
        }
        else {
          finalizeCurrentGroup()
          addBidirectionalFragment(link.wayMember)
        }

      case None =>
        finalizeCurrentGroup()
        addBidirectionalFragment(link.wayMember)
    }
  }

  private def processFirstUnidirectionalWay(link: WayMemberLink): Unit = {
    if (traceEnabled) {
      trace(s"  first way in unidirectional element")
    }

    finalizeCurrentElement()

    lastDownFragment match {
      case None =>
        elementDirection = Some(ElementDirection.Down)

      case Some(previousDownFragment) =>
        if (previousDownFragment.forwardEndNodeId == link.startNode.id) {
          elementDirection = Some(ElementDirection.Down)
        }
        else {
          lastUpFragment match {
            case None =>
              elementDirection = Some(ElementDirection.Down)
            case Some(previousUpFragment) =>
              if (previousUpFragment.backwardEndNodeId == link.nodeIds.last) {
                elementDirection = Some(ElementDirection.Up)
              }
              else {
                finalizeCurrentGroup()
                elementDirection = Some(ElementDirection.Down)
              }
          }
        }
    }

    val fragment = StructureFragment(
      link.wayMember.way,
      bidirectional = false,
      link.nodeIds
    )
    currentElementFragments.addOne(fragment)

    elementDirection match {
      case Some(ElementDirection.Down) =>
        lastDownFragment = Some(fragment)
      case Some(ElementDirection.Up) =>
        lastUpFragment = Some(fragment)
      case _ =>
        lastDownFragment = Some(fragment)
        lastUpFragment = Some(fragment)
    }
  }

  private def processNextUnidirectionalWayDown(link: WayMemberLink): Unit = {
    val nodeIds = link.nodeIds
    lastDownFragment match {
      case None =>
        throw new IllegalStateException("there should be a lastDownFragment at this point")

      case Some(previousDownFragment) =>

        // look ahead at next member, if this way does not connect to the next way, then it is possibly an up fragment
        if (!link.canConnectUpTo(link.next)) {
          lastUpFragment match {
            case None =>
              xxx(link, previousDownFragment)

            case Some(previousUpFragment) =>

              if (previousUpFragment.backwardStartNodeId == link.endNode.id) {
                finalizeCurrentElement()
                elementDirection = Some(ElementDirection.Up)
                val fragment = StructureFragment(
                  link.wayMember.way,
                  bidirectional = false,
                  nodeIds
                )
                currentElementFragments.addOne(fragment)
                lastUpFragment = Some(fragment)
              }
              else {
                xxx(link, previousDownFragment)
              }
          }
        }
        else {
          xxx(link, previousDownFragment)
        }
    }
  }

  private def xxx(link: WayMemberLink, previousDownFragment: StructureFragment) {
    if (link.nodeIds.head == previousDownFragment.forwardEndNodeId) {
      val fragment = StructureFragment(
        link.wayMember.way,
        bidirectional = false,
        link.nodeIds
      )
      currentElementFragments.addOne(fragment)
      lastDownFragment = Some(fragment)
    }
    else {
      lastUpFragment match {
        case None =>
          // switching direction here? first time there could be an UP fragment
          // look at start of current down element
          currentElementFragments.headOption match {
            case Some(firstDownFragment) =>
              if (firstDownFragment.forwardStartNodeId == link.endNode.id) {
                finalizeCurrentElement()
                elementDirection = Some(ElementDirection.Up)
                val fragment = StructureFragment(
                  link.wayMember.way,
                  bidirectional = false,
                  link.nodeIds
                )
                currentElementFragments.addOne(fragment)
                lastUpFragment = Some(fragment)
              }
              else {
                finalizeCurrentGroup()
              }
            case None =>
              finalizeCurrentGroup()
          }

        case Some(previousFragment) =>
          if (link.nodeIds.last == link.endNode.id) {
            finalizeCurrentElement()
            elementDirection = Some(ElementDirection.Up)
            val fragment = StructureFragment(
              link.wayMember.way,
              bidirectional = false,
              link.nodeIds
            )
            currentElementFragments.addOne(fragment)
            lastUpFragment = Some(fragment)
          }
          else {
            finalizeCurrentGroup()
          }
      }
    }
  }

  private def processNextUnidirectionalWayUp(link: WayMemberLink): Unit = {

    val nodeIds = link.nodeIds
    lastUpFragment match {
      case None => throw new Exception("illegal state?")
      case Some(previousFragment) =>
        if (nodeIds.last == previousFragment.backwardStartNodeId) {
          val fragment = StructureFragment(
            link.wayMember.way,
            bidirectional = false,
            nodeIds
          )
          currentElementFragments.addOne(fragment)
          lastUpFragment = Some(fragment)
        }
        else {
          lastDownFragment match {
            case None =>
              throw new Exception("what to do here?")
            case Some(previousFragment) =>
              if (nodeIds.head == previousFragment.forwardEndNodeId) {
                // switch direction
                val fragment = StructureFragment(
                  link.wayMember.way,
                  bidirectional = false,
                  nodeIds
                )
                finalizeCurrentElement()
                elementDirection = Some(ElementDirection.Down)
                currentElementFragments.addOne(fragment)
                lastDownFragment = Some(fragment)
              }
              else {
                finalizeCurrentGroup()
              }
          }
        }
    }
  }

  private def finalizeCurrentElement(): Option[StructureElement] = {
    if (currentElementFragments.nonEmpty) {
      val element = addElement(currentElementFragments.toSeq)
      currentElementFragments.clear()
      Some(element)
    }
    else {
      None
    }
  }

  private def finalizeCurrentGroup(): Unit = {
    finalizeCurrentElement()
    if (elements.nonEmpty) {
      if (traceEnabled) {
        trace(s"  add group")
      }
      elementGroups.addOne(StructureElementGroup(elements.toSeq))
      elements.clear()
    }
  }

  private def addElement(fragments: Seq[StructureFragment]): StructureElement = {
    if (traceEnabled) {
      trace(s"  add element: direction=$elementDirection, fragments: ${fragments.map(_.string).mkString(", ")}")
    }
    val sortedFragments = if (elementDirection.contains(ElementDirection.Up)) fragments.reverse else fragments
    val element = StructureElement.from(sortedFragments, elementDirection)
    elements.addOne(element)
    element
  }

  private def trace(message: String): Unit = {
    if (traceEnabled) {
      println(message)
    }
  }

  private def addBidirectionalFragment(wayMember: WayMember, reversed: Boolean = false): Unit = {
    val nodes = if (reversed) wayMember.way.nodes.reverse else wayMember.way.nodes
    val nodeIds = nodes.map(_.id)
    val fragment = StructureFragment(wayMember.way, bidirectional = true, nodeIds)
    currentElementFragments.addOne(fragment)
    lastDownFragment = Some(fragment)
    lastUpFragment = Some(fragment)
  }

  private def addDownElement(wayMember: WayMember, nodeIds: Seq[Long]): Unit = {
    val fragment = StructureFragment(wayMember.way, bidirectional = false, nodeIds)
    val element = StructureElement.from(Seq(fragment), Some(ElementDirection.Down))
    elements.addOne(element)
    lastDownFragment = Some(fragment)
  }

  private def addUpElement(wayMember: WayMember, nodeIds: Seq[Long]): Unit = {
    val fragment = StructureFragment(wayMember.way, bidirectional = false, nodeIds)
    val element = StructureElement.from(Seq(fragment), Some(ElementDirection.Up))
    elements.addOne(element)
    lastUpFragment = Some(fragment)
  }
}
