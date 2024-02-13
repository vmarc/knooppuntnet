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

  private var lastForwardFragment: Option[StructureFragment] = None
  private var lastBackwardFragment: Option[StructureFragment] = None

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
      case Some(ElementDirection.Forward) =>
        processNextUnidirectionalWayForward(link)
      case Some(ElementDirection.Backward) =>
        processNextUnidirectionalWayBackward(link)
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
      lastForwardFragment.map(_.forwardEndNodeId) match {
        case Some(endNodeId) =>
          if (endNodeId == link.nodeIds.head) {
            addBidirectionalFragment(link.wayMember)
          }
          else if (endNodeId == link.nodeIds.last) {
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

        lastForwardFragment.map(_.forwardEndNodeId) match {
          case None =>

            closedLoopLink.forwardConnection(nextLink) match {
              case None =>
                if (traceEnabled) {
                  trace(s"  current closed loop way does not connect to next way")
                }
                finalizeCurrentElement()
                addForwardElement(closedLoopLink.wayMember, closedLoopLink.nodeIds)

              case Some(endNodeId) =>

                finalizeCurrentElement()
                val wayNodeIds = closedLoopLink.nodeIds
                // TODO for hiking routes, should choose shortest path here instead of adding both forward and backward element
                StructureUtil.closedLoopNodeIds(wayNodeIds.head, endNodeId, wayNodeIds) match {
                  case None => throw new Exception("internal error TODO better message")
                  case Some(nodeIds) =>
                    addForwardElement(closedLoopLink.wayMember, nodeIds)
                }
            }

          case Some(lastFragmentEndNodeId) =>

            val connectsToLastForwardFragment = closedLoopLink.nodeIds.contains(lastFragmentEndNodeId)
            val startNodeId = if (connectsToLastForwardFragment) {
              lastFragmentEndNodeId
            }
            else {
              // no link to previous forward fragment, start new group
              // assume first node is start node
              closedLoopLink.nodeIds.head
            }

            closedLoopLink.forwardConnection(nextLink) match {
              case None =>
                if (traceEnabled) {
                  trace(s"  current closed loop way does not connect to next way")
                }
                if (connectsToLastForwardFragment) {
                  finalizeCurrentElement()
                  addForwardElement(closedLoopLink.wayMember, closedLoopLink.nodeIds)
                }

              case Some(endNodeId) =>

                finalizeCurrentElement()
                // TODO for hiking routes, should choose shortest path here instead of adding both forward and backward element
                StructureUtil.closedLoopNodeIds(startNodeId, endNodeId, closedLoopLink.nodeIds) match {
                  case None => throw new Exception("internal error TODO better message")
                  case Some(nodeIds) =>
                    addForwardElement(closedLoopLink.wayMember, nodeIds)
                }
            }
        }

        lastBackwardFragment.map(_.backwardStartNodeId) match {
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
    closedLoopLink.backwardConnection(aheadLink) match {
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
        // TODO for hiking routes, should choose shortest path here instead of adding both forward and backward element
        StructureUtil.closedLoopNodeIds(startNodeId, endNodeId, wayNodeIds) match {
          case None =>
            throw new Exception("internal error TODO better message")
          case Some(nodeIds) =>
            addBackwardElement(closedLoopLink.wayMember, nodeIds)
        }
    }
  }

  private def processClosedLoopAtEndOfRoute(link: WayMemberLink): Unit = {

    finalizeCurrentElement()
    lastForwardFragment.map(_.forwardEndNodeId) match {
      case None =>
        // this closed loop is both the start and the end of the route in forward direction
        addForwardElement(link.wayMember, link.nodeIds)

      case Some(startNodeId) =>

        StructureUtil.closedLoopNodeIds(startNodeId, link.nodeIds) match {
          case Some(nodeIds) =>
            addForwardElement(link.wayMember, nodeIds)
          case None =>
            finalizeCurrentGroup()
        }
    }

    lastBackwardFragment.map(_.backwardStartNodeId) match {
      case None =>
        // this closed loop is both the start and the end of the route in forward direction
        addBackwardElement(link.wayMember, link.nodeIds)

      case Some(startNodeId) =>
        StructureUtil.closedLoopNodeIds(startNodeId, link.nodeIds) match {
          case Some(nodeIds) =>
            addBackwardElement(link.wayMember, nodeIds)
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
  }

  private def addLastBidirectionalFragment(link: WayMemberLink): Unit = {

    lastForwardFragment match {

      case Some(previousForwardFragment) =>

        if (link.nodeIds.head == previousForwardFragment.forwardEndNodeId) {
          addBidirectionalFragment(link.wayMember)
        }
        else if (link.nodeIds.last == previousForwardFragment.forwardEndNodeId) {
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

    lastForwardFragment match {
      case None =>
        elementDirection = Some(ElementDirection.Forward)

      case Some(previousForwardFragment) =>
        if (previousForwardFragment.forwardEndNodeId == link.nodeIds.head) {
          elementDirection = Some(ElementDirection.Forward)
        }
        else {
          lastBackwardFragment match {
            case None =>
              elementDirection = Some(ElementDirection.Forward)
            case Some(previousBackwardFragment) =>
              if (previousBackwardFragment.backwardStartNodeId == link.nodeIds.last) {
                elementDirection = Some(ElementDirection.Backward)
              }
              else {
                finalizeCurrentGroup()
                elementDirection = Some(ElementDirection.Forward)
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
      case Some(ElementDirection.Forward) =>
        lastForwardFragment = Some(fragment)
      case Some(ElementDirection.Backward) =>
        lastBackwardFragment = Some(fragment)
      case _ =>
        lastForwardFragment = Some(fragment)
        lastBackwardFragment = Some(fragment)
    }
  }

  private def processNextUnidirectionalWayForward(link: WayMemberLink): Unit = {
    val nodeIds = link.nodeIds
    lastForwardFragment match {
      case None =>
        throw new IllegalStateException("there should be a lastForwardFragment at this point")

      case Some(previousForwardFragment) =>

        // look ahead at next member, if this way does not connect to the next way, then it is possibly an up fragment
        if (!link.canBackwardConnectTo(link.next)) {
          lastBackwardFragment match {
            case None =>
              xxx(link, previousForwardFragment)

            case Some(previousBackwardFragment) =>

              if (previousBackwardFragment.backwardStartNodeId == link.nodeIds.last) {
                finalizeCurrentElement()
                elementDirection = Some(ElementDirection.Backward)
                val fragment = StructureFragment(
                  link.wayMember.way,
                  bidirectional = false,
                  nodeIds
                )
                currentElementFragments.addOne(fragment)
                lastBackwardFragment = Some(fragment)
              }
              else {
                xxx(link, previousForwardFragment)
              }
          }
        }
        else {
          xxx(link, previousForwardFragment)
        }
    }
  }

  private def xxx(link: WayMemberLink, previousForwardFragment: StructureFragment): Unit = {
    if (link.nodeIds.head == previousForwardFragment.forwardEndNodeId) {
      val fragment = StructureFragment(
        link.wayMember.way,
        bidirectional = false,
        link.nodeIds
      )
      currentElementFragments.addOne(fragment)
      lastForwardFragment = Some(fragment)
    }
    else {
      lastBackwardFragment match {
        case None =>
          // switching direction here? first time there could be an UP fragment
          // look at start of current forward element
          currentElementFragments.headOption match {
            case Some(firstForwardFragment) =>
              if (firstForwardFragment.forwardStartNodeId == link.nodeIds.last) {
                finalizeCurrentElement()
                elementDirection = Some(ElementDirection.Backward)
                val fragment = StructureFragment(
                  link.wayMember.way,
                  bidirectional = false,
                  link.nodeIds
                )
                currentElementFragments.addOne(fragment)
                lastBackwardFragment = Some(fragment)
              }
              else {
                finalizeCurrentGroup()
              }
            case None =>
              finalizeCurrentGroup()
          }

        case Some(previousFragment) =>
          if (previousFragment.backwardEndNodeId == link.nodeIds.last) {
            finalizeCurrentElement()
            elementDirection = Some(ElementDirection.Backward)
            val fragment = StructureFragment(
              link.wayMember.way,
              bidirectional = false,
              link.nodeIds
            )
            currentElementFragments.addOne(fragment)
            lastBackwardFragment = Some(fragment)
          }
          else {
            finalizeCurrentGroup()
          }
      }
    }
  }

  private def processNextUnidirectionalWayBackward(link: WayMemberLink): Unit = {

    val nodeIds = link.nodeIds
    lastBackwardFragment match {
      case None => throw new Exception("illegal state?")
      case Some(previousFragment) =>
        if (nodeIds.last == previousFragment.backwardStartNodeId) {
          val fragment = StructureFragment(
            link.wayMember.way,
            bidirectional = false,
            nodeIds
          )
          currentElementFragments.addOne(fragment)
          lastBackwardFragment = Some(fragment)
        }
        else {
          lastForwardFragment match {
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
                elementDirection = Some(ElementDirection.Forward)
                currentElementFragments.addOne(fragment)
                lastForwardFragment = Some(fragment)
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
    val sortedFragments = if (elementDirection.contains(ElementDirection.Backward)) fragments.reverse else fragments
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
    lastForwardFragment = Some(fragment)
    lastBackwardFragment = Some(fragment)
  }

  private def addForwardElement(wayMember: WayMember, nodeIds: Seq[Long]): Unit = {
    val fragment = StructureFragment(wayMember.way, bidirectional = false, nodeIds)
    val element = StructureElement.from(Seq(fragment), Some(ElementDirection.Forward))
    elements.addOne(element)
    lastForwardFragment = Some(fragment)
  }

  private def addBackwardElement(wayMember: WayMember, nodeIds: Seq[Long]): Unit = {
    val fragment = StructureFragment(wayMember.way, bidirectional = false, nodeIds)
    val element = StructureElement.from(Seq(fragment), Some(ElementDirection.Backward))
    elements.addOne(element)
    lastBackwardFragment = Some(fragment)
  }
}
