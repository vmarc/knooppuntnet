package kpn.server.analyzer.engine.monitor.structure

import kpn.api.common.data.Member
import kpn.api.common.data.WayMember
import kpn.core.util.Triplet

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

  private var contextCurrentWayMember: WayMember = null
  private var contextNextWayMember: Option[WayMember] = None

  private var lastForwardFragment: Option[StructureFragment] = None
  private var lastBackwardFragment: Option[StructureFragment] = None

  def analyze(): Seq[StructureElementGroup] = {
    Triplet.slide(wayMembers).zipWithIndex.foreach { case (wayMemberCursor, index) =>
      contextCurrentWayMember = wayMemberCursor.current
      contextNextWayMember = wayMemberCursor.next
      if (traceEnabled) {
        trace(s"${index + 1} way ${contextCurrentWayMember.way.id} role=${contextCurrentWayMember.role}")
      }
      processWayMember()
    }
    finalizeCurrentGroup()
    elementGroups.toSeq
  }

  private def processWayMember(): Unit = {

    if (isClosedLoop(contextCurrentWayMember)) {
      processClosedLoop()
    }
    else if (contextCurrentWayMember.isUnidirectional || contextCurrentWayMember.isRoundabout /* TODO different for hiking routes? */ ) {
      processUnidirectionalWay()
    }
    else {
      processBidirectionalWay()
    }
  }

  private def processUnidirectionalWay(): Unit = {
    elementDirection match {
      case None =>
        processFirstUnidirectionalWay()
      case Some(ElementDirection.Down) =>
        processNextUnidirectionalWayDown()
      case Some(ElementDirection.Up) =>
        processNextUnidirectionalWayUp()
    }
  }

  private def processBidirectionalWay(): Unit = {
    if (elementDirection.nonEmpty) {
      if (traceEnabled) {
        trace(s"  first way after unidirectional element")
      }

      finalizeCurrentElement()
      elementDirection = None
      addBidirectionalFragment()
    }
    else {
      val calculatedEndNodeId = lastForwardFragment.map(_.forwardEndNodeId)

      calculatedEndNodeId match {
        case Some(endNodeId) =>
          if (endNodeId == contextCurrentWayMember.startNode.id) {
            addBidirectionalFragment(contextCurrentWayMember)
          }
          else if (endNodeId == contextCurrentWayMember.endNode.id) {
            addBidirectionalFragment(contextCurrentWayMember, reversed = true)
          }
          else {
            // no connection
            finalizeCurrentGroup()
            addBidirectionalFragment()
          }

        case None =>
          // first fragment of this element, look at the next way for connection
          addBidirectionalFragment()
      }
    }
  }

  private def processClosedLoop(): Unit = {

    contextNextWayMember match {
      case None =>
        processClosedLoopAtEndOfRoute()

      case Some(nextWayMember) =>

        lastForwardFragment.map(_.forwardEndNodeId) match {
          case None =>

            val connectingNodeIds1 = contextCurrentWayMember.way.nodes.map(_.id).dropRight(1)
            val connectingNodeIds2 = if (isClosedLoop(nextWayMember)) {
              nextWayMember.way.nodes.map(_.id).dropRight(1)
            }
            else {
              if (nextWayMember.hasRoleForward) {
                Seq(nextWayMember.way.nodes.head.id)
              }
              else if (nextWayMember.hasRoleBackward) {
                Seq(nextWayMember.way.nodes.last.id)
              }
              else {
                // bidirectional fragment
                Seq(
                  nextWayMember.way.nodes.head.id,
                  nextWayMember.way.nodes.last.id
                )
              }
            }

            val connectingNodeId = connectingNodeIds1.flatMap { nodeId1 =>
              connectingNodeIds2
                .filter(nodeId2 => nodeId1 == nodeId2)
                .headOption
            }.headOption

            connectingNodeId match {
              case None =>

                if (traceEnabled) {
                  trace(s"  current closed loop way does not connect to next way")
                }

                finalizeCurrentElement()
                val nodeIds = contextCurrentWayMember.way.nodes.map(_.id)

                val forwardFragment = StructureFragment(contextCurrentWayMember.way, bidirectional = false, nodeIds)
                lastForwardFragment = Some(forwardFragment)
                val forwardElement = StructureElement.from(Seq(forwardFragment), Some(ElementDirection.Down))
                elements.addOne(forwardElement)

              case Some(endNodeId) =>

                finalizeCurrentElement()
                val wayNodeIds = contextCurrentWayMember.way.nodes.map(_.id)
                // TODO for walking routes, should choose shortest path here instead of adding both forward and backward element
                StructureUtil.closedLoopNodeIds(wayNodeIds.head, endNodeId, wayNodeIds) match {
                  case None => throw new Exception("internal error TODO better message")
                  case Some(nodeIds) =>
                    val forwardFragment = StructureFragment(contextCurrentWayMember.way, bidirectional = false, nodeIds)
                    lastForwardFragment = Some(forwardFragment)
                    val element = StructureElement.from(Seq(forwardFragment), Some(ElementDirection.Down))
                    elements.addOne(element)
                }
            }

          case Some(startNodeId) =>

            val connectingNodeIds1 = contextCurrentWayMember.way.nodes.map(_.id).dropRight(1)
            val connectingNodeIds2 = if (isClosedLoop(nextWayMember)) {
              nextWayMember.way.nodes.map(_.id)
            }
            else {
              if (nextWayMember.hasRoleForward) {
                Seq(nextWayMember.way.nodes.head.id)
              }
              else if (nextWayMember.hasRoleBackward) {
                Seq(nextWayMember.way.nodes.last.id)
              }
              else {
                // bidirectional fragment
                Seq(
                  nextWayMember.way.nodes.head.id,
                  nextWayMember.way.nodes.last.id
                )
              }
            }

            val connectingNodeId = connectingNodeIds1.flatMap {
              nodeId1 =>
                connectingNodeIds2
                  .filter(nodeId2 => nodeId1 == nodeId2)
                  .headOption
            }.headOption

            connectingNodeId match {
              case None =>

                if (traceEnabled) {
                  trace(s"  current closed loop way does not connect to next way")
                }

                finalizeCurrentElement()
                val nodeIds = contextCurrentWayMember.way.nodes.map(_.id)

                val forwardFragment = StructureFragment(contextCurrentWayMember.way, bidirectional = false, nodeIds)
                lastForwardFragment = Some(forwardFragment)
                val forwardElement = StructureElement.from(Seq(forwardFragment), Some(ElementDirection.Down))
                elements.addOne(forwardElement)

              case Some(endNodeId) =>

                finalizeCurrentElement()
                val wayNodeIds = contextCurrentWayMember.way.nodes.map(_.id)
                // TODO for walking routes, should choose shortest path here instead of adding both forward and backward element
                StructureUtil.closedLoopNodeIds(startNodeId, endNodeId, wayNodeIds) match {
                  case None => throw new Exception("internal error TODO better message")
                  case Some(nodeIds) =>
                    val forwardFragment = StructureFragment(contextCurrentWayMember.way, bidirectional = false, nodeIds)
                    lastForwardFragment = Some(forwardFragment)
                    val element = StructureElement.from(Seq(forwardFragment), Some(ElementDirection.Down))
                    elements.addOne(element)
                }
            }
        }

        lastBackwardFragment.map(_.backwardStartNodeId) match {
          case None =>

            val connectingNodeIds1 = contextCurrentWayMember.way.nodes.map(_.id).dropRight(1)
            val connectingNodeIds2 = if (isClosedLoop(nextWayMember)) {
              nextWayMember.way.nodes.map(_.id).dropRight(1)
            }
            else {
              if (nextWayMember.hasRoleForward) {
                Seq(nextWayMember.way.nodes.head.id)
              }
              else if (nextWayMember.hasRoleBackward) {
                Seq(nextWayMember.way.nodes.last.id)
              }
              else {
                // bidirectional fragment
                Seq(
                  nextWayMember.way.nodes.head.id,
                  nextWayMember.way.nodes.last.id
                )
              }
            }

            val connectingNodeId = connectingNodeIds1.flatMap { nodeId1 =>
              connectingNodeIds2
                .filter(nodeId2 => nodeId1 == nodeId2)
                .headOption
            }.headOption

            connectingNodeId match {
              case None =>
                if (traceEnabled) {
                  trace(s"  current closed loop way does not connect to next way")
                }
                finalizeCurrentElement()
                val nodeIds = contextCurrentWayMember.way.nodes.map(_.id)
                val backwardFragment = StructureFragment(contextCurrentWayMember.way, bidirectional = false, nodeIds)
                lastBackwardFragment = Some(backwardFragment)
                val backwardElement = StructureElement.from(Seq(backwardFragment), Some(ElementDirection.Up))
                elements.addOne(backwardElement)

              case Some(startNodeId) =>

                finalizeCurrentElement()
                val wayNodeIds = contextCurrentWayMember.way.nodes.map(_.id)
                // TODO for walking routes, should choose shortest path here instead of adding both forward and backward element
                StructureUtil.closedLoopNodeIds(startNodeId, wayNodeIds.head, wayNodeIds) match {
                  case None => throw new Exception("internal error TODO better message")
                  case Some(nodeIds) =>
                    val backwardFragment = StructureFragment(contextCurrentWayMember.way, bidirectional = false, nodeIds)
                    lastBackwardFragment = Some(backwardFragment)
                    val element = StructureElement.from(Seq(backwardFragment), Some(ElementDirection.Up))
                    elements.addOne(element)
                }
            }

          case Some(endNodeId) =>

            val connectingNodeIds1 = contextCurrentWayMember.way.nodes.map(_.id).dropRight(1)

            val connectingNodeIds2 = if (isClosedLoop(nextWayMember)) {
              nextWayMember.way.nodes.map(_.id)
            }
            else {
              if (nextWayMember.hasRoleForward) {
                Seq(nextWayMember.way.nodes.head.id)
              }
              else if (nextWayMember.hasRoleBackward) {
                Seq(nextWayMember.way.nodes.last.id)
              }
              else {
                // bidirectional fragment
                Seq(
                  nextWayMember.way.nodes.head.id,
                  nextWayMember.way.nodes.last.id
                )
              }
            }

            val connectingNodeId = connectingNodeIds1.flatMap { nodeId1 =>
              connectingNodeIds2
                .filter(nodeId2 => nodeId1 == nodeId2)
                .headOption
            }.headOption

            connectingNodeId match {
              case None =>

                if (traceEnabled) {
                  trace(s"  current closed loop way does not connect to next way")
                }

                finalizeCurrentElement()
                val nodeIds = contextCurrentWayMember.way.nodes.map(_.id)

                val backwardFragment = StructureFragment(contextCurrentWayMember.way, bidirectional = false, nodeIds)
                lastBackwardFragment = Some(backwardFragment)
                val backwardElement = StructureElement.from(Seq(backwardFragment), Some(ElementDirection.Up))
                elements.addOne(backwardElement)

              case Some(startNodeId) =>

                finalizeCurrentElement()
                val wayNodeIds = contextCurrentWayMember.way.nodes.map(_.id)
                // TODO for walking routes, should choose shortest path here instead of adding both forward and backward element
                StructureUtil.closedLoopNodeIds(startNodeId, endNodeId, wayNodeIds) match {
                  case None =>
                    throw new Exception("internal error TODO better message")
                  case Some(nodeIds) =>
                    val backwardFragment = StructureFragment(contextCurrentWayMember.way, bidirectional = false, nodeIds)
                    lastBackwardFragment = Some(backwardFragment)
                    val element = StructureElement.from(Seq(backwardFragment), Some(ElementDirection.Up))
                    elements.addOne(element)
                }
            }
        }
    }
  }

  private def processClosedLoopAtEndOfRoute(): Unit = {

    finalizeCurrentElement()
    lastForwardFragment.map(_.forwardEndNodeId) match {
      case None =>
        // this closed loop is both the start and the end of the route in forward direction
        val nodeIds = contextCurrentWayMember.way.nodes.map(_.id)
        val forwardFragment = StructureFragment(contextCurrentWayMember.way, bidirectional = false, nodeIds)
        lastForwardFragment = Some(forwardFragment)
        val forwardElement = StructureElement.from(Seq(forwardFragment), Some(ElementDirection.Down))
        elements.addOne(forwardElement)

      case Some(startNodeId) =>

        val wayNodeIds = contextCurrentWayMember.way.nodes.map(_.id)
        StructureUtil.closedLoopNodeIds(startNodeId, wayNodeIds) match {
          case None =>
            finalizeCurrentGroup()
          case Some(nodeIds) =>
            val forwardFragment = StructureFragment(contextCurrentWayMember.way, bidirectional = false, nodeIds)
            lastForwardFragment = Some(forwardFragment)
            val element = StructureElement.from(Seq(forwardFragment), Some(ElementDirection.Down))
            elements.addOne(element)
        }
    }

    lastBackwardFragment.map(_.backwardStartNodeId) match {
      case None =>
        // this closed loop is both the start and the end of the route in forward direction
        val nodeIds = contextCurrentWayMember.way.nodes.map(_.id)
        val backwardFragment = StructureFragment(contextCurrentWayMember.way, bidirectional = false, nodeIds)
        lastBackwardFragment = Some(backwardFragment)
        val backwardElement = StructureElement.from(Seq(backwardFragment), Some(ElementDirection.Up))
        elements.addOne(backwardElement)

      case Some(startNodeId) =>

        val wayNodeIds = contextCurrentWayMember.way.nodes.map(_.id)
        StructureUtil.closedLoopNodeIds(startNodeId, wayNodeIds) match {
          case None =>
            finalizeCurrentGroup()
          case Some(nodeIds) =>
            val forwardFragment = StructureFragment(contextCurrentWayMember.way, bidirectional = false, nodeIds)
            lastForwardFragment = Some(forwardFragment)
            val element = StructureElement.from(Seq(forwardFragment), Some(ElementDirection.Up))
            elements.addOne(element)
        }
    }
  }

  private def addBidirectionalFragment(): Unit = {
    contextNextWayMember match {
      case Some(nextWayMember) =>
        addBidirectionalFragmentByLookingAheadAtNextWayMember(nextWayMember)
      case None =>
        addLastBidirectionalFragment()
    }
  }

  private def addBidirectionalFragmentByLookingAheadAtNextWayMember(nextWayMember: WayMember): Unit = {

    val connectingNodeIds1 = Seq(
      contextCurrentWayMember.way.nodes.last.id,
      contextCurrentWayMember.way.nodes.head.id
    )

    val connectingNodeIds2 = if (isClosedLoop(nextWayMember)) {
      nextWayMember.way.nodes.map(_.id).dropRight(1)
    }
    else {
      if (nextWayMember.hasRoleForward) {
        Seq(nextWayMember.way.nodes.head.id)
      }
      else if (nextWayMember.hasRoleBackward) {
        Seq(nextWayMember.way.nodes.last.id)
      }
      else {
        // bidirectional fragment
        Seq(
          nextWayMember.way.nodes.head.id,
          nextWayMember.way.nodes.last.id
        )
      }
    }

    val connectingNodeId = connectingNodeIds1.flatMap { nodeId1 =>
      connectingNodeIds2
        .filter(nodeId2 => nodeId1 == nodeId2)
        .headOption
    }.headOption

    connectingNodeId match {
      case None =>
        if (traceEnabled) {
          trace(s"  current way does not connect to next way")
        }
        addBidirectionalFragment(contextCurrentWayMember)
        finalizeCurrentGroup()

      case Some(nodeId) =>
        if (nodeId == contextCurrentWayMember.way.nodes.last.id) {
          addBidirectionalFragment(contextCurrentWayMember)
        }
        else {
          addBidirectionalFragment(contextCurrentWayMember, reversed = true)
        }
    }
  }

  private def addBidirectionalFragmentByLookingAheadAtClosedLoop(nextWayMember: WayMember): Unit = {

    val connectingNodeIds1 = Seq(
      contextCurrentWayMember.way.nodes.last.id,
      contextCurrentWayMember.way.nodes.head.id
    )

    val connectingNodeIds2 = nextWayMember.way.nodes.map(_.id)

    val connectingNodeId = connectingNodeIds1.flatMap { nodeId1 =>
      connectingNodeIds2
        .filter(nodeId2 => nodeId1 == nodeId2)
        .headOption
    }.headOption

    connectingNodeId match {
      case None =>
        if (traceEnabled) {
          trace(s"  current way does not connect to next way")
        }
        addBidirectionalFragment(contextCurrentWayMember)
        finalizeCurrentGroup()

      case Some(nodeId) =>


        if (nodeId == contextCurrentWayMember.way.nodes.last.id) {
          addBidirectionalFragment(contextCurrentWayMember)
        }
        else {
          addBidirectionalFragment(contextCurrentWayMember, reversed = true)
        }
    }
  }


  private def addLastBidirectionalFragment(): Unit = {

    lastForwardFragment match {

      case Some(previousForwardFragment) =>

        if (contextCurrentWayMember.startNode.id == previousForwardFragment.forwardEndNodeId) {
          addBidirectionalFragment(contextCurrentWayMember)
        }
        else if (contextCurrentWayMember.endNode.id == previousForwardFragment.forwardEndNodeId) {
          addBidirectionalFragment(contextCurrentWayMember, reversed = true)
        }
        else {
          finalizeCurrentGroup()
          addBidirectionalFragment(contextCurrentWayMember)
        }

      case None =>
        finalizeCurrentGroup()
        addBidirectionalFragment(contextCurrentWayMember)
    }
  }

  private def processFirstUnidirectionalWay(): Unit = {
    if (traceEnabled) {
      trace(s"  first way in unidirectional element")
    }

    finalizeCurrentElement()
    elements.lastOption match {
      case None =>

        // the very first way member in the route is unidirectional
        // TODO does the element direction really depend on the role? or always 'Down'?
        if (contextCurrentWayMember.hasRoleForward || contextCurrentWayMember.isRoundabout) {
          elementDirection = Some(ElementDirection.Down)
        } else if (contextCurrentWayMember.hasRoleBackward) {
          elementDirection = Some(ElementDirection.Up)
        }
        else {
          throw new IllegalStateException("a unidirectional wayMember should have role 'forward' or 'backward'")
        }

      case Some(previousElement) =>
        if (previousElement.forwardEndNodeId == contextCurrentWayMember.startNode.id) {
          elementDirection = Some(ElementDirection.Down)
        }
        else if (previousElement.forwardEndNodeId == contextCurrentWayMember.startNode.id) {
          elementDirection = Some(ElementDirection.Up)
        }
        else {
          finalizeCurrentGroup()
          elementDirection = Some(ElementDirection.Down)
        }
    }

    val nodeIds = if (contextCurrentWayMember.hasRoleBackward) {
      contextCurrentWayMember.way.nodes.reverse.map(_.id)
    }
    else {
      contextCurrentWayMember.way.nodes.map(_.id)
    }
    val fragment = StructureFragment(
      contextCurrentWayMember.way,
      bidirectional = false,
      nodeIds
    )
    currentElementFragments.addOne(fragment)

    elementDirection match {
      case Some(ElementDirection.Down) =>
        lastForwardFragment = Some(fragment)
      case Some(ElementDirection.Up) =>
        lastBackwardFragment = Some(fragment)
      case _ =>
        lastForwardFragment = Some(fragment)
        lastBackwardFragment = Some(fragment)
    }
  }

  private def processNextUnidirectionalWayDown(): Unit = {
    lastForwardFragment match {
      case None =>
        throw new Exception("illegal state??")

      case Some(previousFragment) =>
        if (contextCurrentWayMember.startNode.id == previousFragment.forwardEndNodeId) {
          val nodeIds = if (contextCurrentWayMember.hasRoleBackward) {
            contextCurrentWayMember.way.nodes.reverse.map(_.id)
          }
          else {
            contextCurrentWayMember.way.nodes.map(_.id)
          }
          val fragment = StructureFragment(
            contextCurrentWayMember.way,
            bidirectional = false,
            nodeIds
          )
          lastForwardFragment = Some(fragment)
          currentElementFragments.addOne(fragment)
        }
        else {
          currentElementFragments.headOption match {
            case None => finalizeCurrentGroup()
            case Some(firstFragmentInElement) =>
              if (contextCurrentWayMember.endNode.id == firstFragmentInElement.forwardStartNodeId) {
                // switch
                finalizeCurrentElement()
                elementDirection = Some(ElementDirection.Up)
                val nodeIds = if (contextCurrentWayMember.hasRoleBackward) {
                  contextCurrentWayMember.way.nodes.reverse.map(_.id)
                }
                else {
                  contextCurrentWayMember.way.nodes.map(_.id)
                }
                val fragment = StructureFragment(
                  contextCurrentWayMember.way,
                  bidirectional = false,
                  nodeIds
                )
                lastBackwardFragment = Some(fragment)
                currentElementFragments.addOne(fragment)
              }
              else {
                finalizeCurrentGroup()
              }
          }
        }
    }
  }

  private def processNextUnidirectionalWayUp(): Unit = {

    lastBackwardFragment match {
      case None => throw new Exception("illegal state?")
      case Some(previousFragment) =>
        if (contextCurrentWayMember.endNode.id == previousFragment.backwardStartNodeId) {
          val nodeIds = if (contextCurrentWayMember.hasRoleBackward) {
            contextCurrentWayMember.way.nodes.reverse.map(_.id)
          }
          else {
            contextCurrentWayMember.way.nodes.map(_.id)
          }
          val fragment = StructureFragment(
            contextCurrentWayMember.way,
            bidirectional = false,
            nodeIds
          )
          lastBackwardFragment = Some(fragment)
          currentElementFragments.addOne(fragment)
        }
        else {
          currentElementFragments.headOption match {
            case None =>
              finalizeCurrentGroup()
            case Some(firstFragmentInElement) =>
              // TODO following lines probably not ok
              if (contextCurrentWayMember.endNode.id == firstFragmentInElement.backwardEndNodeId) {
                // switch
                finalizeCurrentElement()
                elementDirection = Some(ElementDirection.Down)
                val nodeIds = contextCurrentWayMember.way.nodes.map(_.id)
                val fragment = StructureFragment(
                  contextCurrentWayMember.way,
                  bidirectional = false,
                  nodeIds
                )
                lastForwardFragment = Some(fragment)
                currentElementFragments.addOne(fragment)
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

  private def findPreviousFragment(): Option[StructureFragment] = {
    currentElementFragments.lastOption match {
      case Some(fragment) => Some(fragment)
      case None =>
        elements.lastOption match {
          case Some(element) => element.fragments.lastOption
          case None => None
        }
    }
  }

  private def trace(message: String): Unit = {
    if (traceEnabled) {
      println(message)
    }
  }

  private def isClosedLoop(wayMember: WayMember): Boolean = {
    val way = wayMember.way
    way.nodes.size > 2 && way.nodes.head == way.nodes.last
  }

  private def addBidirectionalFragment(wayMember: WayMember, reversed: Boolean = false): Unit = {
    val nodes = if (reversed) wayMember.way.nodes.reverse else wayMember.way.nodes
    val nodeIds = nodes.map(_.id)
    val fragment = StructureFragment(wayMember.way, bidirectional = true, nodeIds)
    lastForwardFragment = Some(fragment)
    lastBackwardFragment = Some(fragment)
    currentElementFragments.addOne(fragment)
  }
}
