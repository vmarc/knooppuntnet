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

  private var lastDownFragment: Option[StructureFragment] = None
  private var lastUpFragment: Option[StructureFragment] = None

  def analyze(): Seq[StructureElementGroup] = {
    Triplet.slide(wayMembers).zipWithIndex.foreach { case (triplet, index) =>
      if (traceEnabled) {
        trace(s"${index + 1} way ${triplet.current.way.id} role=${triplet.current.role}")
      }
      processWayMember(triplet)
    }
    finalizeCurrentGroup()
    elementGroups.toSeq
  }

  private def processWayMember(triplet: Triplet[WayMember]): Unit = {

    if (isClosedLoop(triplet.current)) {
      processClosedLoop(triplet)
    }
    else if (triplet.current.isUnidirectional || triplet.current.isRoundabout /* TODO different for hiking routes? */ ) {
      processUnidirectionalWay(triplet)
    }
    else {
      processBidirectionalWay(triplet)
    }
  }

  private def processUnidirectionalWay(triplet: Triplet[WayMember]): Unit = {
    elementDirection match {
      case Some(ElementDirection.Down) =>
        processNextUnidirectionalWayDown(triplet)
      case Some(ElementDirection.Up) =>
        processNextUnidirectionalWayUp(triplet)
      case _ =>
        processFirstUnidirectionalWay(triplet)
    }
  }

  private def processBidirectionalWay(triplet: Triplet[WayMember]): Unit = {
    if (elementDirection.nonEmpty) {
      if (traceEnabled) {
        trace(s"  first way after unidirectional element")
      }

      finalizeCurrentElement()
      elementDirection = None
      addBidirectionalFragment(triplet)
    }
    else {
      val calculatedEndNodeId = lastDownFragment.map(_.forwardEndNodeId)

      calculatedEndNodeId match {
        case Some(endNodeId) =>
          if (endNodeId == triplet.current.startNode.id) {
            addBidirectionalFragment(triplet.current)
          }
          else if (endNodeId == triplet.current.endNode.id) {
            addBidirectionalFragment(triplet.current, reversed = true)
          }
          else {
            // no connection
            finalizeCurrentGroup()
            addBidirectionalFragment(triplet)
          }

        case None =>
          // first fragment of this element, look at the next way for connection
          addBidirectionalFragment(triplet)
      }
    }
  }

  private def processClosedLoop(triplet: Triplet[WayMember]): Unit = {

    if (triplet.next.isEmpty) {
      processClosedLoopAtEndOfRoute(triplet)
    }
    else {

      lastDownFragment.map(_.forwardEndNodeId) match {
        case None =>

          triplet.next match {
            case None =>
              throw new Exception("illegal state")

            case Some(nextWayMember) =>
              val connectingNodeIds1 = triplet.current.way.nodes.map(_.id).dropRight(1)
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
                  val nodeIds = triplet.current.way.nodes.map(_.id)
                  addDownElement(triplet.current, nodeIds)

                case Some(endNodeId) =>

                  finalizeCurrentElement()
                  val wayNodeIds = triplet.current.way.nodes.map(_.id)
                  // TODO for walking routes, should choose shortest path here instead of adding both forward and backward element
                  StructureUtil.closedLoopNodeIds(wayNodeIds.head, endNodeId, wayNodeIds) match {
                    case None => throw new Exception("internal error TODO better message")
                    case Some(nodeIds) =>
                      addDownElement(triplet.current, nodeIds)
                  }
              }
          }
        case Some(startNodeId) =>

          triplet.next match {
            case None =>
              processClosedLoopAtEndOfRoute(triplet)

            case Some(nextWayMember) =>
              val connectingNodeIds1 = triplet.current.way.nodes.map(_.id).dropRight(1)
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
                  val nodeIds = triplet.current.way.nodes.map(_.id)
                  addDownElement(triplet.current, nodeIds)

                case Some(endNodeId) =>

                  finalizeCurrentElement()
                  val wayNodeIds = triplet.current.way.nodes.map(_.id)
                  // TODO for walking routes, should choose shortest path here instead of adding both forward and backward element
                  StructureUtil.closedLoopNodeIds(startNodeId, endNodeId, wayNodeIds) match {
                    case None => throw new Exception("internal error TODO better message")
                    case Some(nodeIds) =>
                      addDownElement(triplet.current, nodeIds)
                  }
              }
          }
      }

      lastUpFragment.map(_.backwardStartNodeId) match {
        case None =>

          triplet.next match {
            case None =>
              throw new Exception("illegal state")

            case Some(nextWayMember) =>
              val connectingNodeIds1 = triplet.current.way.nodes.map(_.id).dropRight(1)
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
                  val nodeIds = triplet.current.way.nodes.map(_.id)
                  addUpElement(triplet.current, nodeIds)

                case Some(startNodeId) =>

                  finalizeCurrentElement()
                  val wayNodeIds = triplet.current.way.nodes.map(_.id)
                  // TODO for walking routes, should choose shortest path here instead of adding both forward and backward element
                  StructureUtil.closedLoopNodeIds(startNodeId, wayNodeIds.head, wayNodeIds) match {
                    case None => throw new Exception("internal error TODO better message")
                    case Some(nodeIds) =>
                      addUpElement(triplet.current, nodeIds)
                  }
              }
          }
        case Some(endNodeId) =>

          triplet.next match {
            case None =>
              processClosedLoopAtEndOfRoute(triplet)

            case Some(nextWayMember) =>
              val connectingNodeIds1 = triplet.current.way.nodes.map(_.id).dropRight(1)

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
                  val nodeIds = triplet.current.way.nodes.map(_.id)
                  addUpElement(triplet.current, nodeIds)

                case Some(startNodeId) =>

                  finalizeCurrentElement()
                  val wayNodeIds = triplet.current.way.nodes.map(_.id)
                  // TODO for walking routes, should choose shortest path here instead of adding both forward and backward element
                  StructureUtil.closedLoopNodeIds(startNodeId, endNodeId, wayNodeIds) match {
                    case None =>
                      throw new Exception("internal error TODO better message")
                    case Some(nodeIds) =>
                      addUpElement(triplet.current, nodeIds)
                  }
              }
          }
      }
    }
  }

  private def processClosedLoopAtEndOfRoute(triplet: Triplet[WayMember]): Unit = {

    finalizeCurrentElement()
    lastDownFragment.map(_.forwardEndNodeId) match {
      case None =>
        // this closed loop is both the start and the end of the route in forward direction
        val nodeIds = triplet.current.way.nodes.map(_.id)
        addDownElement(triplet.current, nodeIds)

      case Some(startNodeId) =>

        val wayNodeIds = triplet.current.way.nodes.map(_.id)
        StructureUtil.closedLoopNodeIds(startNodeId, wayNodeIds) match {
          case None =>
            finalizeCurrentGroup()
          case Some(nodeIds) =>
            addDownElement(triplet.current, nodeIds)
        }
    }

    lastUpFragment.map(_.backwardStartNodeId) match {
      case None =>
        // this closed loop is both the start and the end of the route in forward direction
        val nodeIds = triplet.current.way.nodes.map(_.id)
        addUpElement(triplet.current, nodeIds)

      case Some(startNodeId) =>

        val wayNodeIds = triplet.current.way.nodes.map(_.id)
        StructureUtil.closedLoopNodeIds(startNodeId, wayNodeIds) match {
          case None =>
            finalizeCurrentGroup()
          case Some(nodeIds) =>
            addUpElement(triplet.current, nodeIds)
        }
    }
  }

  private def addBidirectionalFragment(triplet: Triplet[WayMember]): Unit = {
    triplet.next match {
      case Some(nextWayMember) =>
        addBidirectionalFragmentByLookingAheadAtNextWayMember(triplet, nextWayMember)
      case None =>
        addLastBidirectionalFragment(triplet)
    }
  }

  private def addBidirectionalFragmentByLookingAheadAtNextWayMember(triplet: Triplet[WayMember], nextWayMember: WayMember): Unit = {

    val connectingNodeIds1 = Seq(
      triplet.current.way.nodes.last.id,
      triplet.current.way.nodes.head.id
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
        addBidirectionalFragment(triplet.current)
        finalizeCurrentGroup()

      case Some(nodeId) =>
        if (nodeId == triplet.current.way.nodes.last.id) {
          addBidirectionalFragment(triplet.current)
        }
        else {
          addBidirectionalFragment(triplet.current, reversed = true)
        }
    }
  }

  private def addBidirectionalFragmentByLookingAheadAtClosedLoop(triplet: Triplet[WayMember], nextWayMember: WayMember): Unit = {

    val connectingNodeIds1 = Seq(
      triplet.current.way.nodes.last.id,
      triplet.current.way.nodes.head.id
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
        addBidirectionalFragment(triplet.current)
        finalizeCurrentGroup()

      case Some(nodeId) =>


        if (nodeId == triplet.current.way.nodes.last.id) {
          addBidirectionalFragment(triplet.current)
        }
        else {
          addBidirectionalFragment(triplet.current, reversed = true)
        }
    }
  }


  private def addLastBidirectionalFragment(triplet: Triplet[WayMember]): Unit = {

    lastDownFragment match {

      case Some(previousForwardFragment) =>

        if (triplet.current.startNode.id == previousForwardFragment.forwardEndNodeId) {
          addBidirectionalFragment(triplet.current)
        }
        else if (triplet.current.endNode.id == previousForwardFragment.forwardEndNodeId) {
          addBidirectionalFragment(triplet.current, reversed = true)
        }
        else {
          finalizeCurrentGroup()
          addBidirectionalFragment(triplet.current)
        }

      case None =>
        finalizeCurrentGroup()
        addBidirectionalFragment(triplet.current)
    }
  }

  private def processFirstUnidirectionalWay(triplet: Triplet[WayMember]): Unit = {
    if (traceEnabled) {
      trace(s"  first way in unidirectional element")
    }

    finalizeCurrentElement()
    elements.lastOption match {
      case None =>

        // the very first way member in the route is unidirectional
        // TODO does the element direction really depend on the role? or always 'Down'?
        if (triplet.current.hasRoleForward || triplet.current.isRoundabout) {
          elementDirection = Some(ElementDirection.Down)
        } else if (triplet.current.hasRoleBackward) {
          elementDirection = Some(ElementDirection.Up)
        }
        else {
          throw new IllegalStateException("a unidirectional wayMember should have role 'forward' or 'backward'")
        }

      case Some(previousElement) =>
        if (previousElement.forwardEndNodeId == triplet.current.startNode.id) {
          elementDirection = Some(ElementDirection.Down)
        }
        else if (previousElement.forwardEndNodeId == triplet.current.startNode.id) {
          elementDirection = Some(ElementDirection.Up)
        }
        else {
          finalizeCurrentGroup()
          elementDirection = Some(ElementDirection.Down)
        }
    }

    val nodeIds = if (triplet.current.hasRoleBackward) {
      triplet.current.way.nodes.reverse.map(_.id)
    }
    else {
      triplet.current.way.nodes.map(_.id)
    }
    val fragment = StructureFragment(
      triplet.current.way,
      bidirectional = false,
      nodeIds
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

  private def processNextUnidirectionalWayDown(triplet: Triplet[WayMember]): Unit = {
    val nodeIds = if (triplet.current.hasRoleBackward) {
      triplet.current.way.nodes.reverse.map(_.id)
    }
    else {
      triplet.current.way.nodes.map(_.id)
    }
    lastDownFragment match {
      case None =>
        throw new Exception("illegal state??")

      case Some(previousFragment) =>
        if (nodeIds.head == previousFragment.forwardEndNodeId) {
          val fragment = StructureFragment(
            triplet.current.way,
            bidirectional = false,
            nodeIds
          )
          currentElementFragments.addOne(fragment)
          lastDownFragment = Some(fragment)
        }
        else {
          lastUpFragment match {
            case None =>
              throw new Exception("what to do here?")
            case Some(previousFragment) =>
              if (nodeIds.last == triplet.current.endNode.id) {
                finalizeCurrentElement()
                elementDirection = Some(ElementDirection.Up)
                val fragment = StructureFragment(
                  triplet.current.way,
                  bidirectional = false,
                  nodeIds
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
  }

  private def processNextUnidirectionalWayUp(triplet: Triplet[WayMember]): Unit = {

    val nodeIds = if (triplet.current.hasRoleBackward) {
      triplet.current.way.nodes.reverse.map(_.id)
    }
    else {
      triplet.current.way.nodes.map(_.id)
    }
    lastUpFragment match {
      case None => throw new Exception("illegal state?")
      case Some(previousFragment) =>
        if (nodeIds.last == previousFragment.backwardStartNodeId) {
          val fragment = StructureFragment(
            triplet.current.way,
            bidirectional = false,
            nodeIds
          )
          currentElementFragments.addOne(fragment)
          lastUpFragment = Some(fragment)
        }
        else {
          lastDownFragment match {
            case None => throw new Exception("what to do here?")
            case Some(previousFragment) =>
              if (nodeIds.head == previousFragment.forwardEndNodeId) {
                // switch direction
                val fragment = StructureFragment(
                  triplet.current.way,
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

  private def isClosedLoop(wayMember: WayMember): Boolean = {
    val way = wayMember.way
    way.nodes.size > 2 && way.nodes.head == way.nodes.last
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
