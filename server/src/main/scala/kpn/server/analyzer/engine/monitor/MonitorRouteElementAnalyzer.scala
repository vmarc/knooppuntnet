package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.Member
import kpn.api.common.data.WayMember
import kpn.core.util.Triplet

import scala.collection.mutable

object MonitorRouteElementAnalyzer {
  def analyze(members: Seq[Member]): Seq[MonitorRouteElementGroup] = {
    val wayMembers = members.flatMap { member =>
      member match {
        case wayMember: WayMember => Some(wayMember)
        case _ => None
      }
    }
    if (wayMembers.exists(_.way.nodes.length < 2)) {
      throw new IllegalStateException("ways with less that 2 nodes should have been filtered out at this point")
    }
    new MonitorRouteElementAnalyzer(wayMembers).analyze()
  }
}

class MonitorRouteElementAnalyzer(wayMembers: Seq[WayMember]) {

  private var elementDirection: Option[ElementDirection.Value] = None

  private val elementGroups = mutable.Buffer[MonitorRouteElementGroup]()
  private val elements = mutable.Buffer[MonitorRouteElement]()
  private val currentElementFragments = mutable.Buffer[MonitorRouteFragment]()

  private var contextCurrentWayMember: WayMember = null
  private var contextNextWayMember: Option[WayMember] = None

  private var lastForwardFragment: Option[MonitorRouteFragment] = None
  private var lastBackwardFragment: Option[MonitorRouteFragment] = None

  def analyze(): Seq[MonitorRouteElementGroup] = {
    Triplet.slide(wayMembers).foreach { wayMemberCursor =>
      contextCurrentWayMember = wayMemberCursor.current
      contextNextWayMember = wayMemberCursor.next
      processWayMember()
    }
    finalizeCurrentGroup()
    elementGroups.toSeq
  }

  private def processWayMember(): Unit = {
    debug(s"way ${contextCurrentWayMember.way.id} role=${contextCurrentWayMember.role}")
    if (contextCurrentWayMember.isUnidirectional) {
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
      debug(s"  first way after unidirectional element")

      elementDirection match {
        case Some(ElementDirection.Up) => reverseFragments()
        case _ =>
      }

      finalizeCurrentElement()
      elementDirection = None
      addBidirectionalFragment()
    }
    else {
      val calculatedEndNodeId = currentElementFragments.lastOption.map(_.endNode.id) match {
        case None => elements.lastOption.map(_.endNodeId)
        case Some(endNodeId) => Some(endNodeId)
      }


      calculatedEndNodeId match {
        case Some(endNodeId) =>
          if (endNodeId == contextCurrentWayMember.startNode.id) {
            val fragment = MonitorRouteFragment(contextCurrentWayMember.way)
            lastForwardFragment = Some(fragment)
            lastBackwardFragment = Some(fragment)
            currentElementFragments.addOne(fragment)
          }
          else if (endNodeId == contextCurrentWayMember.endNode.id) {
            val fragment = MonitorRouteFragment(contextCurrentWayMember.way, reversed = true)
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

  private def addBidirectionalFragment(): Unit = {
    contextNextWayMember match {
      case Some(nextWayMember) => addBidirectionalFragmentByLookingAheadAtNextWayMember(nextWayMember)
      case None => addLastBidirectionalFragment()
    }
  }

  private def addBidirectionalFragmentByLookingAheadAtNextWayMember(nextWayMember: WayMember): Unit = {

    val connectingNodeIds1 = Seq(
      contextCurrentWayMember.way.nodes.last.id,
      contextCurrentWayMember.way.nodes.head.id
    )

    val connectingNodeIds2 = if (nextWayMember.hasRoleForward) {
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

    val connectingNodeId = connectingNodeIds1.flatMap { nodeId1 =>
      connectingNodeIds2
        .filter(nodeId2 => nodeId1 == nodeId2)
        .headOption
    }.headOption

    connectingNodeId match {
      case None =>
        debug(s"  current way does not connect to next way")
        val fragment = MonitorRouteFragment(contextCurrentWayMember.way)
        lastForwardFragment = Some(fragment)
        lastBackwardFragment = Some(fragment)
        currentElementFragments.addOne(fragment)

        finalizeCurrentGroup()

      case Some(nodeId) =>
        if (nodeId == contextCurrentWayMember.way.nodes.last.id) {
          val fragment = MonitorRouteFragment(contextCurrentWayMember.way)
          lastForwardFragment = Some(fragment)
          lastBackwardFragment = Some(fragment)
          currentElementFragments.addOne(fragment)
        }
        else {
          val fragment = MonitorRouteFragment(contextCurrentWayMember.way, reversed = true)
          lastForwardFragment = Some(fragment)
          lastBackwardFragment = Some(fragment)
          currentElementFragments.addOne(fragment)
        }
    }
  }

  private def addLastBidirectionalFragment(): Unit = {

    println("---")
    println(s"contextCurrentWayMember.startNode=${contextCurrentWayMember.startNode.id}")
    println(s"contextCurrentWayMember.endNode=${contextCurrentWayMember.endNode.id}")
    println(s"lastForwardFragment.endNode=${lastForwardFragment.map(_.endNode.id)}")
    println(s"lastBackwardFragment.endNode=${lastBackwardFragment.map(_.endNode.id)}")

    val downEndNodeId = elements.findLast(_.direction.contains(ElementDirection.Down)).map(_.endNodeId)
    val upEndNodeId = elements.findLast(_.direction.contains(ElementDirection.Up)).map(_.startNodeId)
    val anyEndNodeId = elements.lastOption.map(_.endNodeId)

    println(s"downEndNodeId=$downEndNodeId")
    println(s"upEndNodeId=$upEndNodeId")
    println(s"anyEndNodeId=$anyEndNodeId")

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
    println(s"calculatedEndNodeId=$calculatedEndNodeId")

    println("---")

    calculatedEndNodeId match {
      case None =>
        val fragment = MonitorRouteFragment(contextCurrentWayMember.way)
        lastForwardFragment = Some(fragment)
        lastBackwardFragment = Some(fragment)
        currentElementFragments.addOne(fragment)

      case Some(endNodeId) =>

        if (contextCurrentWayMember.startNode.id == endNodeId) {
          val fragment = MonitorRouteFragment(contextCurrentWayMember.way)
          lastForwardFragment = Some(fragment)
          lastBackwardFragment = Some(fragment)
          currentElementFragments.addOne(fragment)
        }
        else if (contextCurrentWayMember.endNode.id == endNodeId) {
          val fragment = MonitorRouteFragment(contextCurrentWayMember.way, reversed = true)
          lastForwardFragment = Some(fragment)
          lastBackwardFragment = Some(fragment)
          currentElementFragments.addOne(fragment)
        }
        else {
          finalizeCurrentGroup()
          val fragment = MonitorRouteFragment(contextCurrentWayMember.way)
          lastForwardFragment = Some(fragment)
          lastBackwardFragment = Some(fragment)
          currentElementFragments.addOne(fragment)
        }
    }
  }


  private def processFirstUnidirectionalWay(): Unit = {
    debug(s"  first way in unidirectional element")

    finalizeCurrentElement()
    elements.lastOption match {
      case None =>

        // the very first way member in the route is unidirectional
        // TODO does the element direction really depend on the role? or always 'Down'?
        if (contextCurrentWayMember.hasRoleForward) {
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

    val fragment = MonitorRouteFragment(
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
        if (contextCurrentWayMember.startNode.id == previousFragment.endNode.id) {
          val fragment = MonitorRouteFragment(contextCurrentWayMember.way)
          lastForwardFragment = Some(fragment)
          currentElementFragments.addOne(fragment)
        }
        else {
          currentElementFragments.headOption match {
            case None => finalizeCurrentGroup()
            case Some(firstFragmentInElement) =>
              if (contextCurrentWayMember.endNode.id == firstFragmentInElement.startNode.id) {
                // switch
                finalizeCurrentElement()
                elementDirection = Some(ElementDirection.Up)
                val reversed = contextCurrentWayMember.hasRoleBackward
                val fragment = MonitorRouteFragment(contextCurrentWayMember.way, reversed)
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
        if (contextCurrentWayMember.endNode.id == previousFragment.startNode.id) {
          val reversed = contextCurrentWayMember.hasRoleBackward
          val fragment = MonitorRouteFragment(contextCurrentWayMember.way, reversed)
          currentElementFragments.addOne(fragment)
          // addFragmentReversed(contextCurrentWayMember)
        }
        else {
          currentElementFragments.headOption match {
            case None =>
              finalizeCurrentGroup()
            case Some(firstFragmentInElement) =>
              // TODO following lines probably not ok
              if (contextCurrentWayMember.endNode.id == firstFragmentInElement.startNode.id) {
                // switch
                finalizeCurrentElement()
                elementDirection = Some(ElementDirection.Down)
                val fragment = MonitorRouteFragment(contextCurrentWayMember.way)
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

  private def finalizeCurrentElement(): Option[MonitorRouteElement] = {
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
      debug(s"  add group")
      elementGroups.addOne(MonitorRouteElementGroup(elements.toSeq))
      elements.clear()
    }
  }

  private def addElement(fragments: Seq[MonitorRouteFragment]): MonitorRouteElement = {
    debug(s"  add element: direction=$elementDirection, fragments: ${fragments.map(_.string).mkString(", ")}")
    val element = MonitorRouteElement.from(fragments, elementDirection)
    elements.addOne(element)
    element
  }

  private def findPreviousFragment(): Option[MonitorRouteFragment] = {
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

  private def debug(message: String): Unit = {
    println(message)
  }
}
