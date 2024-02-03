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
    Triplet.slide(wayMembers).foreach { wayMemberCursor =>
      contextCurrentWayMember = wayMemberCursor.current
      contextNextWayMember = wayMemberCursor.next
      processWayMember()
    }
    finalizeCurrentGroup()
    elementGroups.toSeq
  }

  private def processWayMember(): Unit = {
    if (traceEnabled) {
      trace(s"way ${contextCurrentWayMember.way.id} role=${contextCurrentWayMember.role}")
    }

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

      elementDirection match {
        case Some(ElementDirection.Up) => reverseFragments()
        case _ =>
      }

      finalizeCurrentElement()
      elementDirection = None
      addBidirectionalFragment()
    }
    else {
      val calculatedEndNodeId = lastForwardFragment.map(_.endNodeId)

      calculatedEndNodeId match {
        case Some(endNodeId) =>
          if (endNodeId == contextCurrentWayMember.startNode.id) {
            val fragment = StructureFragment.from(contextCurrentWayMember.way)
            lastForwardFragment = Some(fragment)
            lastBackwardFragment = Some(fragment)
            currentElementFragments.addOne(fragment)
          }
          else if (endNodeId == contextCurrentWayMember.endNode.id) {
            val fragment = StructureFragment.from(contextCurrentWayMember.way, reversed = true)
            lastForwardFragment = Some(fragment)
            lastBackwardFragment = Some(fragment)
            currentElementFragments.addOne(fragment)
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
    val calculatedStartNodeId = lastForwardFragment.map(_.endNodeId)

    calculatedStartNodeId match {
      case None =>
        contextNextWayMember match {
          case None =>
            // The closed loop is the first fragment of the current element and there is no next element

            val nodeIds = contextCurrentWayMember.way.nodes.map(_.id)

            val forwardFragment = StructureFragment(contextCurrentWayMember.way, nodeIds)
            lastForwardFragment = Some(forwardFragment)
            val forwardElement = StructureElement.from(Seq(forwardFragment), Some(ElementDirection.Down))
            elements.addOne(forwardElement)

            val backwardFragment = StructureFragment(contextCurrentWayMember.way, nodeIds.reverse)
            lastBackwardFragment = Some(backwardFragment)
            val backwardElement = StructureElement.from(Seq(backwardFragment), Some(ElementDirection.Up))
            elements.addOne(backwardElement)

          case Some(nextWayMember) =>

            // The closed loop is the first fragment of the current element

            val connectingNodeIds1 = contextCurrentWayMember.way.nodes.map(_.id)

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

                val forwardFragment = StructureFragment(contextCurrentWayMember.way, nodeIds)
                lastForwardFragment = Some(forwardFragment)
                val forwardElement = StructureElement.from(Seq(forwardFragment), Some(ElementDirection.Down))
                elements.addOne(forwardElement)

                val backwardFragment = StructureFragment(contextCurrentWayMember.way, nodeIds.reverse)
                lastBackwardFragment = Some(backwardFragment)
                val backwardElement = StructureElement.from(Seq(backwardFragment), Some(ElementDirection.Up))
                elements.addOne(backwardElement)

              case Some(nodeId) =>

                finalizeCurrentElement()
                val wayNodeIds = contextCurrentWayMember.way.nodes.map(_.id)
                // TODO for walking routes, should choose shortest path here instead of adding both forward and backward element
                StructureUtil.closedLoopNodeIds(wayNodeIds.head, nodeId, wayNodeIds) match {
                  case None => throw new Exception("internal error TODO better message")
                  case Some(nodeIds) =>
                    val forwardFragment = StructureFragment(contextCurrentWayMember.way, nodeIds)
                    lastForwardFragment = Some(forwardFragment)
                    val element = StructureElement.from(Seq(forwardFragment), Some(ElementDirection.Down))
                    elements.addOne(element)
                }

                StructureUtil.closedLoopNodeIds(nodeId, wayNodeIds.head, wayNodeIds) match {
                  case None => throw new Exception("internal error TODO better message")
                  case Some(nodeIds) =>
                    val backwardFragment = StructureFragment(contextCurrentWayMember.way, nodeIds)
                    lastBackwardFragment = Some(backwardFragment)
                    val element = StructureElement.from(Seq(backwardFragment), Some(ElementDirection.Up))
                    elements.addOne(element)
                }
            }
        }

      case Some(startNodeId) =>

        contextNextWayMember match {
          case None =>

            val wayNodeIds = contextCurrentWayMember.way.nodes.map(_.id)
            StructureUtil.closedLoopNodeIds(startNodeId, wayNodeIds) match {
              case None =>
                throw new Exception("internal error TODO better message")
              case Some(nodeIds) =>

                finalizeCurrentElement()
                val forwardFragment = StructureFragment(contextCurrentWayMember.way, nodeIds)
                lastForwardFragment = Some(forwardFragment)
                val forwardElement = StructureElement.from(Seq(forwardFragment), Some(ElementDirection.Down))
                elements.addOne(forwardElement)

                val backwardFragment = StructureFragment(contextCurrentWayMember.way, nodeIds.reverse)
                lastBackwardFragment = Some(backwardFragment)
                val element = StructureElement.from(Seq(backwardFragment), Some(ElementDirection.Up))
                elements.addOne(element)
            }

          case Some(nextWayMember) =>

            val connectingNodeIds1 = contextCurrentWayMember.way.nodes.map(_.id)

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
                val wayNodeIds = contextCurrentWayMember.way.nodes.map(_.id)
                StructureUtil.closedLoopNodeIds(startNodeId, wayNodeIds) match {
                  case None => throw new Exception("internal error TODO better message")
                  case Some(nodeIds) =>
                    val fragment = StructureFragment(contextCurrentWayMember.way, nodeIds)
                    lastForwardFragment = Some(fragment)
                    lastBackwardFragment = Some(fragment)
                    currentElementFragments.addOne(fragment)
                    finalizeCurrentGroup()
                }

              case Some(nodeId) =>

                finalizeCurrentElement()
                val wayNodeIds = contextCurrentWayMember.way.nodes.map(_.id)
                // TODO for walking routes, should choose shortest path here instead of adding both forward and backward element
                StructureUtil.closedLoopNodeIds(startNodeId, nodeId, wayNodeIds) match {
                  case None => throw new Exception("internal error TODO better message")
                  case Some(nodeIds) =>
                    val forwardFragment = StructureFragment(contextCurrentWayMember.way, nodeIds)
                    lastForwardFragment = Some(forwardFragment)
                    val element = StructureElement.from(Seq(forwardFragment), Some(ElementDirection.Down))
                    elements.addOne(element)
                }

                StructureUtil.closedLoopNodeIds(nodeId, startNodeId, wayNodeIds) match {
                  case None => throw new Exception("internal error TODO better message")
                  case Some(nodeIds) =>
                    val backwardFragment = StructureFragment(contextCurrentWayMember.way, nodeIds)
                    lastBackwardFragment = Some(backwardFragment)
                    val element = StructureElement.from(Seq(backwardFragment), Some(ElementDirection.Up))
                    elements.addOne(element)
                }
            }
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
          trace(s"  current way does not connect to next way")
        }
        val fragment = StructureFragment.from(contextCurrentWayMember.way)
        lastForwardFragment = Some(fragment)
        lastBackwardFragment = Some(fragment)
        currentElementFragments.addOne(fragment)

        finalizeCurrentGroup()

      case Some(nodeId) =>
        if (nodeId == contextCurrentWayMember.way.nodes.last.id) {
          val fragment = StructureFragment.from(contextCurrentWayMember.way)
          lastForwardFragment = Some(fragment)
          lastBackwardFragment = Some(fragment)
          currentElementFragments.addOne(fragment)
        }
        else {
          val fragment = StructureFragment.from(contextCurrentWayMember.way, reversed = true)
          lastForwardFragment = Some(fragment)
          lastBackwardFragment = Some(fragment)
          currentElementFragments.addOne(fragment)
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
        // TODO can share this code with addBidirectionalFragmentByLookingAheadAtNextWayMember()?
        val fragment = StructureFragment.from(contextCurrentWayMember.way)
        lastForwardFragment = Some(fragment)
        lastBackwardFragment = Some(fragment)
        currentElementFragments.addOne(fragment)

        finalizeCurrentGroup()

      case Some(nodeId) =>


        if (nodeId == contextCurrentWayMember.way.nodes.last.id) {
          val fragment = StructureFragment.from(contextCurrentWayMember.way)
          lastForwardFragment = Some(fragment)
          lastBackwardFragment = Some(fragment)
          currentElementFragments.addOne(fragment)
        }
        else {
          val fragment = StructureFragment.from(contextCurrentWayMember.way, reversed = true)
          lastForwardFragment = Some(fragment)
          lastBackwardFragment = Some(fragment)
          currentElementFragments.addOne(fragment)
        }
    }
  }


  private def addLastBidirectionalFragment(): Unit = {

    if (traceEnabled) {
      println("---")
      println(s"contextCurrentWayMember.startNode=${contextCurrentWayMember.startNode.id}")
      println(s"contextCurrentWayMember.endNode=${contextCurrentWayMember.endNode.id}")
      println(s"lastForwardFragment.endNode=${lastForwardFragment.map(_.endNodeId)}")
      println(s"lastBackwardFragment.endNode=${lastBackwardFragment.map(_.endNodeId)}")
    }

    val downEndNodeId = elements.findLast(_.direction.contains(ElementDirection.Down)).map(_.endNodeId)
    val upEndNodeId = elements.findLast(_.direction.contains(ElementDirection.Up)).map(_.startNodeId)
    val anyEndNodeId = elements.lastOption.map(_.endNodeId)

    if (traceEnabled) {
      println(s"downEndNodeId=$downEndNodeId")
      println(s"upEndNodeId=$upEndNodeId")
      println(s"anyEndNodeId=$anyEndNodeId")
    }

    val calculatedEndNodeId = elements.lastOption.map { lastElement =>
      if (lastElement.direction.contains(ElementDirection.Down)) {
        lastElement.endNodeId
      }
      else if (lastElement.direction.contains(ElementDirection.Up)) {
        lastElement.startNodeId
      }
      else {
        lastElement.endNodeId
      }
    }
    if (traceEnabled) {
      println(s"calculatedEndNodeId=$calculatedEndNodeId")
      println("---")
    }
    calculatedEndNodeId match {
      case None =>
        val fragment = StructureFragment.from(contextCurrentWayMember.way)
        lastForwardFragment = Some(fragment)
        lastBackwardFragment = Some(fragment)
        currentElementFragments.addOne(fragment)

      case Some(endNodeId) =>

        if (contextCurrentWayMember.startNode.id == endNodeId) {
          val fragment = StructureFragment.from(contextCurrentWayMember.way)
          lastForwardFragment = Some(fragment)
          lastBackwardFragment = Some(fragment)
          currentElementFragments.addOne(fragment)
        }
        else if (contextCurrentWayMember.endNode.id == endNodeId) {
          val fragment = StructureFragment.from(contextCurrentWayMember.way, reversed = true)
          lastForwardFragment = Some(fragment)
          lastBackwardFragment = Some(fragment)
          currentElementFragments.addOne(fragment)
        }
        else {
          finalizeCurrentGroup()
          val fragment = StructureFragment.from(contextCurrentWayMember.way)
          lastForwardFragment = Some(fragment)
          lastBackwardFragment = Some(fragment)
          currentElementFragments.addOne(fragment)
        }
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
        if (previousElement.endNodeId == contextCurrentWayMember.startNode.id) {
          elementDirection = Some(ElementDirection.Down)
        }
        else if (previousElement.endNodeId == contextCurrentWayMember.startNode.id) {
          elementDirection = Some(ElementDirection.Up)
        }
        else {
          finalizeCurrentGroup()
          elementDirection = Some(ElementDirection.Down)
        }
    }

    val fragment = StructureFragment.from(
      contextCurrentWayMember.way,
      // direction = Some(Direction.Backward),
      reversed = contextCurrentWayMember.hasRoleBackward
    )
    currentElementFragments.addOne(fragment)
  }

  private def processNextUnidirectionalWayDown(): Unit = {
    findPreviousFragment() match {
      case None =>
        throw new Exception("illegal state??")

      case Some(previousFragment) =>
        if (contextCurrentWayMember.startNode.id == previousFragment.endNodeId) {
          val fragment = StructureFragment.from(contextCurrentWayMember.way)
          lastForwardFragment = Some(fragment)
          currentElementFragments.addOne(fragment)
        }
        else {
          currentElementFragments.headOption match {
            case None => finalizeCurrentGroup()
            case Some(firstFragmentInElement) =>
              if (contextCurrentWayMember.endNode.id == firstFragmentInElement.startNodeId) {
                // switch
                finalizeCurrentElement()
                elementDirection = Some(ElementDirection.Up)
                val reversed = contextCurrentWayMember.hasRoleBackward
                val fragment = StructureFragment.from(contextCurrentWayMember.way, reversed)
                currentElementFragments.addOne(fragment)
                // addFragmentReversed(contextCurrentWayMember)
              }
              else {
                finalizeCurrentGroup()
              }
          }
        }
    }
  }

  private def processNextUnidirectionalWayUp(): Unit = {
    findPreviousFragment() match {
      case None => throw new Exception("illegal state?")
      case Some(previousFragment) =>
        if (contextCurrentWayMember.endNode.id == previousFragment.startNodeId) {
          val reversed = contextCurrentWayMember.hasRoleBackward
          val fragment = StructureFragment.from(contextCurrentWayMember.way, reversed)
          currentElementFragments.addOne(fragment)
          // addFragmentReversed(contextCurrentWayMember)
        }
        else {
          currentElementFragments.headOption match {
            case None =>
              finalizeCurrentGroup()
            case Some(firstFragmentInElement) =>
              // TODO following lines probably not ok
              if (contextCurrentWayMember.endNode.id == firstFragmentInElement.startNodeId) {
                // switch
                finalizeCurrentElement()
                elementDirection = Some(ElementDirection.Down)
                val fragment = StructureFragment.from(contextCurrentWayMember.way)
                lastBackwardFragment = Some(fragment)
                currentElementFragments.addOne(fragment)
              }
              else {
                reverseFragments()
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
    val element = StructureElement.from(fragments, elementDirection)
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

  private def reverseFragments(): Unit = {
    val reversed = currentElementFragments.reverse
    currentElementFragments.clear()
    currentElementFragments.addAll(reversed)
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
}
