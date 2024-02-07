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

    val links = WayMemberLink.from(wayMembers)

    links.zipWithIndex.foreach { case (link, index) =>
      if (traceEnabled) {
        trace(s"${index + 1} way ${link.wayMember.way.id} role=${link.wayMember.role}")
      }
      processWayMember(link)
    }
    finalizeCurrentGroup()
    elementGroups.toSeq
  }

  private def processWayMember(link: WayMemberLink): Unit = {

    if (isClosedLoop(link.wayMember)) {
      processClosedLoop(link)
    }
    else if (link.wayMember.isUnidirectional || link.wayMember.isRoundabout /* TODO different for hiking routes? */ ) {
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
      val calculatedEndNodeId = lastDownFragment.map(_.forwardEndNodeId)

      calculatedEndNodeId match {
        case Some(endNodeId) =>
          if (endNodeId == link.wayMember.startNode.id) {
            addBidirectionalFragment(link.wayMember)
          }
          else if (endNodeId == link.wayMember.endNode.id) {
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

  private def processClosedLoop(link: WayMemberLink): Unit = {

    if (link.next.isEmpty) {
      processClosedLoopAtEndOfRoute(link)
    }
    else {

      lastDownFragment.map(_.forwardEndNodeId) match {
        case None =>

          link.next match {
            case None =>
              throw new Exception("illegal state")

            case Some(nextLink) =>
              val connectingNodeIds1 = link.wayMember.way.nodes.map(_.id).dropRight(1)
              val connectingNodeIds2 = if (isClosedLoop(nextLink.wayMember)) {
                nextLink.wayMember.way.nodes.map(_.id).dropRight(1)
              }
              else {
                if (nextLink.wayMember.hasRoleForward) {
                  Seq(nextLink.wayMember.way.nodes.head.id)
                }
                else if (nextLink.wayMember.hasRoleBackward) {
                  Seq(nextLink.wayMember.way.nodes.last.id)
                }
                else {
                  // bidirectional fragment
                  Seq(
                    nextLink.wayMember.way.nodes.head.id,
                    nextLink.wayMember.way.nodes.last.id
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
                  val nodeIds = link.wayMember.way.nodes.map(_.id)
                  addDownElement(link.wayMember, nodeIds)

                case Some(endNodeId) =>

                  finalizeCurrentElement()
                  val wayNodeIds = link.wayMember.way.nodes.map(_.id)
                  // TODO for walking routes, should choose shortest path here instead of adding both forward and backward element
                  StructureUtil.closedLoopNodeIds(wayNodeIds.head, endNodeId, wayNodeIds) match {
                    case None => throw new Exception("internal error TODO better message")
                    case Some(nodeIds) =>
                      addDownElement(link.wayMember, nodeIds)
                  }
              }
          }
        case Some(startNodeId) =>

          link.next match {
            case None =>
              processClosedLoopAtEndOfRoute(link)

            case Some(nextLink) =>
              val connectingNodeIds1 = link.wayMember.way.nodes.map(_.id).dropRight(1)
              val connectingNodeIds2 = if (isClosedLoop(nextLink.wayMember)) {
                nextLink.wayMember.way.nodes.map(_.id)
              }
              else {
                if (nextLink.wayMember.hasRoleForward) {
                  Seq(nextLink.wayMember.way.nodes.head.id)
                }
                else if (nextLink.wayMember.hasRoleBackward) {
                  Seq(nextLink.wayMember.way.nodes.last.id)
                }
                else {
                  // bidirectional fragment
                  Seq(
                    nextLink.wayMember.way.nodes.head.id,
                    nextLink.wayMember.way.nodes.last.id
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
                  val nodeIds = link.wayMember.way.nodes.map(_.id)
                  addDownElement(link.wayMember, nodeIds)

                case Some(endNodeId) =>

                  finalizeCurrentElement()
                  val wayNodeIds = link.wayMember.way.nodes.map(_.id)
                  // TODO for walking routes, should choose shortest path here instead of adding both forward and backward element
                  StructureUtil.closedLoopNodeIds(startNodeId, endNodeId, wayNodeIds) match {
                    case None => throw new Exception("internal error TODO better message")
                    case Some(nodeIds) =>
                      addDownElement(link.wayMember, nodeIds)
                  }
              }
          }
      }

      lastUpFragment.map(_.backwardStartNodeId) match {
        case None =>




          link.next match {
            case None =>
              throw new Exception("illegal state")

            case Some(nextLink) =>
              val connectingNodeIds1 = link.wayMember.way.nodes.map(_.id).dropRight(1)
              val connectingNodeIds2 = if (isClosedLoop(nextLink.wayMember)) {
                nextLink.wayMember.way.nodes.map(_.id).dropRight(1)
              }
              else {
                if (nextLink.wayMember.hasRoleForward) {
                  Seq(nextLink.wayMember.way.nodes.head.id)
                }
                else if (nextLink.wayMember.hasRoleBackward) {
                  Seq(nextLink.wayMember.way.nodes.last.id)
                }
                else {
                  // bidirectional fragment
                  Seq(
                    nextLink.wayMember.way.nodes.head.id,
                    nextLink.wayMember.way.nodes.last.id
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
                  val nodeIds = link.wayMember.way.nodes.map(_.id)
                  addUpElement(link.wayMember, nodeIds)

                case Some(startNodeId) =>

                  finalizeCurrentElement()
                  val wayNodeIds = link.wayMember.way.nodes.map(_.id)
                  // TODO for walking routes, should choose shortest path here instead of adding both forward and backward element
                  StructureUtil.closedLoopNodeIds(startNodeId, wayNodeIds.head, wayNodeIds) match {
                    case None => throw new Exception("internal error TODO better message")
                    case Some(nodeIds) =>
                      addUpElement(link.wayMember, nodeIds)
                  }
              }
          }


        case Some(endNodeId) =>

          link.next match {
            case None =>
              processClosedLoopAtEndOfRoute(link)

            case Some(nextLink) =>
              val connectingNodeIds1 = link.wayMember.way.nodes.map(_.id).dropRight(1)

              val connectingNodeIds2 = if (isClosedLoop(nextLink.wayMember)) {
                nextLink.wayMember.way.nodes.map(_.id)
              }
              else {
                if (nextLink.wayMember.hasRoleForward) {
                  Seq(nextLink.wayMember.way.nodes.head.id)
                }
                else if (nextLink.wayMember.hasRoleBackward) {
                  Seq(nextLink.wayMember.way.nodes.last.id)
                }
                else {
                  // bidirectional fragment
                  Seq(
                    nextLink.wayMember.way.nodes.head.id,
                    nextLink.wayMember.way.nodes.last.id
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
                  val nodeIds = link.wayMember.way.nodes.map(_.id)
                  addUpElement(link.wayMember, nodeIds)

                case Some(startNodeId) =>

                  finalizeCurrentElement()
                  val wayNodeIds = link.wayMember.way.nodes.map(_.id)
                  // TODO for walking routes, should choose shortest path here instead of adding both forward and backward element
                  StructureUtil.closedLoopNodeIds(startNodeId, endNodeId, wayNodeIds) match {
                    case None =>
                      throw new Exception("internal error TODO better message")
                    case Some(nodeIds) =>
                      addUpElement(link.wayMember, nodeIds)
                  }
              }
          }
      }
    }
  }

  private def lookRoundaboutAhead(currentWayMember: WayMember, aheadLink: WayMemberLink): Unit = {
    aheadLink.next match {
      case None =>
      case Some(aheadWayMember) =>

        val connectingNodeIds2 = if (isClosedLoop(aheadWayMember.wayMember)) {
          aheadWayMember.wayMember.way.nodes.map(_.id).dropRight(1)
        }
        else {
          if (aheadWayMember.wayMember.hasRoleForward) {
            Seq(aheadWayMember.wayMember.way.nodes.head.id)
          }
          else if (aheadWayMember.wayMember.hasRoleBackward) {
            Seq(aheadWayMember.wayMember.way.nodes.last.id)
          }
          else {
            // bidirectional fragment
            Seq(
              aheadWayMember.wayMember.way.nodes.head.id,
              aheadWayMember.wayMember.way.nodes.last.id
            )
          }
        }

        val connectingNodeIds1 = currentWayMember.way.nodes.map(_.id).dropRight(1)
        val connectingNodeId = connectingNodeIds1.flatMap { nodeId1 =>
          connectingNodeIds2
            .filter(nodeId2 => nodeId1 == nodeId2)
            .headOption
        }.headOption

        connectingNodeId match {
          case None =>
          //            lookRoundaboutAhead(currentWayMember, aheadTriplet.next)



          case Some(startNodeId) =>

          //            finalizeCurrentElement()
          //            val wayNodeIds = triplet.current.way.nodes.map(_.id)
          //            // TODO for walking routes, should choose shortest path here instead of adding both forward and backward element
          //            StructureUtil.closedLoopNodeIds(startNodeId, wayNodeIds.head, wayNodeIds) match {
          //              case None => throw new Exception("internal error TODO better message")
          //              case Some(nodeIds) =>
          //                addUpElement(triplet.current, nodeIds)
          //            }
        }


    }
  }

  private def processClosedLoopAtEndOfRoute(link: WayMemberLink): Unit = {

    finalizeCurrentElement()
    lastDownFragment.map(_.forwardEndNodeId) match {
      case None =>
        // this closed loop is both the start and the end of the route in forward direction
        val nodeIds = link.wayMember.way.nodes.map(_.id)
        addDownElement(link.wayMember, nodeIds)

      case Some(startNodeId) =>

        val wayNodeIds = link.wayMember.way.nodes.map(_.id)
        StructureUtil.closedLoopNodeIds(startNodeId, wayNodeIds) match {
          case None =>
            finalizeCurrentGroup()
          case Some(nodeIds) =>
            addDownElement(link.wayMember, nodeIds)
        }
    }

    lastUpFragment.map(_.backwardStartNodeId) match {
      case None =>
        // this closed loop is both the start and the end of the route in forward direction
        val nodeIds = link.wayMember.way.nodes.map(_.id)
        addUpElement(link.wayMember, nodeIds)

      case Some(startNodeId) =>

        val wayNodeIds = link.wayMember.way.nodes.map(_.id)
        StructureUtil.closedLoopNodeIds(startNodeId, wayNodeIds) match {
          case None =>
            finalizeCurrentGroup()
          case Some(nodeIds) =>
            addUpElement(link.wayMember, nodeIds)
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

    val connectingNodeIds1 = Seq(
      link.wayMember.way.nodes.last.id,
      link.wayMember.way.nodes.head.id
    )

    val connectingNodeIds2 = if (isClosedLoop(nextLink.wayMember)) {
      nextLink.wayMember.way.nodes.map(_.id).dropRight(1)
    }
    else {
      if (nextLink.wayMember.hasRoleForward) {
        Seq(nextLink.wayMember.way.nodes.head.id)
      }
      else if (nextLink.wayMember.hasRoleBackward) {
        Seq(nextLink.wayMember.way.nodes.last.id)
      }
      else {
        // bidirectional fragment
        Seq(
          nextLink.wayMember.way.nodes.head.id,
          nextLink.wayMember.way.nodes.last.id
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
        addBidirectionalFragment(link.wayMember)
        finalizeCurrentGroup()

      case Some(nodeId) =>
        if (nodeId == link.wayMember.way.nodes.last.id) {
          addBidirectionalFragment(link.wayMember)
        }
        else {
          addBidirectionalFragment(link.wayMember, reversed = true)
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


  private def addLastBidirectionalFragment(link: WayMemberLink): Unit = {

    lastDownFragment match {

      case Some(previousForwardFragment) =>

        if (link.wayMember.startNode.id == previousForwardFragment.forwardEndNodeId) {
          addBidirectionalFragment(link.wayMember)
        }
        else if (link.wayMember.endNode.id == previousForwardFragment.forwardEndNodeId) {
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
    elements.lastOption match {
      case None =>

        // the very first way member in the route is unidirectional
        // TODO does the element direction really depend on the role? or always 'Down'?
        if (link.wayMember.hasRoleForward || link.wayMember.isRoundabout) {
          elementDirection = Some(ElementDirection.Down)
        } else if (link.wayMember.hasRoleBackward) {
          elementDirection = Some(ElementDirection.Up)
        }
        else {
          throw new IllegalStateException("a unidirectional wayMember should have role 'forward' or 'backward'")
        }

      case Some(previousElement) =>
        if (previousElement.forwardEndNodeId == link.wayMember.startNode.id) {
          elementDirection = Some(ElementDirection.Down)
        }
        else if (previousElement.forwardEndNodeId == link.wayMember.startNode.id) {
          elementDirection = Some(ElementDirection.Up)
        }
        else {
          finalizeCurrentGroup()
          elementDirection = Some(ElementDirection.Down)
        }
    }

    val nodeIds = if (link.wayMember.hasRoleBackward) {
      link.wayMember.way.nodes.reverse.map(_.id)
    }
    else {
      link.wayMember.way.nodes.map(_.id)
    }
    val fragment = StructureFragment(
      link.wayMember.way,
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

  private def processNextUnidirectionalWayDown(link: WayMemberLink): Unit = {
    val nodeIds = if (link.wayMember.hasRoleBackward) {
      link.wayMember.way.nodes.reverse.map(_.id)
    }
    else {
      link.wayMember.way.nodes.map(_.id)
    }
    lastDownFragment match {
      case None =>
        throw new Exception("illegal state??")

      case Some(previousFragment) =>
        if (nodeIds.head == previousFragment.forwardEndNodeId) {
          val fragment = StructureFragment(
            link.wayMember.way,
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
              if (nodeIds.last == link.wayMember.endNode.id) {
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
                finalizeCurrentGroup()
              }
          }
        }
    }
  }

  private def processNextUnidirectionalWayUp(link: WayMemberLink): Unit = {

    val nodeIds = if (link.wayMember.hasRoleBackward) {
      link.wayMember.way.nodes.reverse.map(_.id)
    }
    else {
      link.wayMember.way.nodes.map(_.id)
    }
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
            case None => throw new Exception("what to do here?")
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
