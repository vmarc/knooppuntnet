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

  private var oneWayDirection: Option[String] = None // "up" or "down"

  private val elementGroups = mutable.Buffer[MonitorRouteElementGroup]()
  private val elements = mutable.Buffer[MonitorRouteElement]()
  private val currentElementFragments = mutable.Buffer[MonitorRouteFragment]()

  private var currentWayMember: WayMember = null
  private var contextNextWayMember: Option[WayMember] = None

  def analyze(): Seq[MonitorRouteElementGroup] = {
    Triplet.slide(wayMembers).foreach { wayMemberCursor =>
      currentWayMember = wayMemberCursor.current
      contextNextWayMember = wayMemberCursor.next
      processWayMember()
    }
    finalizeCurrentGroup()
    elementGroups.toSeq
  }

  private def processWayMember(): Unit = {
    debug(s"way ${currentWayMember.way.id} role=${currentWayMember.role}")
    if (currentWayMember.isUnidirectional) {
      if (oneWayDirection.isEmpty) {
        processFirstUnidirectionalWay()
      }
      else {
        processNextUnidirectionalWay()
      }
    }
    else {
      if (oneWayDirection.nonEmpty) {
        debug(s"  first way after unidirectional element")

        oneWayDirection match {
          case None =>
          case Some("up") => reverseFragments()
        }

        finalizeCurrentElement()
        oneWayDirection = None
        addBidirectionalFragmentByLookingAheadAtNextWayMember()
      }
      else {
        findPreviousFragment().map(_.endNode.id) match {
          case Some(endNodeId) =>
            if (endNodeId == currentWayMember.startNode.id) {
              addFragment(currentWayMember)
            }
            else if (endNodeId == currentWayMember.endNode.id) {
              addFragmentReversed(currentWayMember)
            }
            else {
              // no connection
              finalizeCurrentGroup()
              addBidirectionalFragmentByLookingAheadAtNextWayMember()
            }

          case None =>
            // first fragment of this element, look at the next way for connection
            addBidirectionalFragmentByLookingAheadAtNextWayMember()
        }
      }
    }
  }

  private def addBidirectionalFragmentByLookingAheadAtNextWayMember(): Unit = {
    contextNextWayMember match {
      case None =>
        // this is the last way in the route, and 'reverse' or not does not matter
        addFragment(currentWayMember)

      case Some(nextWayMember) =>

        val connectingNodeIds1 = Seq(
          currentWayMember.way.nodes.last.id,
          currentWayMember.way.nodes.head.id
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

        val fragmentOption = connectingNodeId.map { nodeId =>
          if (nodeId == currentWayMember.way.nodes.last.id) {
            MonitorRouteFragment(currentWayMember.way)
          }
          else {
            MonitorRouteFragment(currentWayMember.way, reversed = true)
          }
        }

        fragmentOption match {
          case Some(fragment) => currentElementFragments.addOne(fragment)
          case None =>
            debug(s"  current way does not connect to next way")
            addElement(Seq(MonitorRouteFragment(currentWayMember.way)))
            finalizeCurrentGroup()
        }
    }
  }

  private def processFirstUnidirectionalWay(): Unit = {
    debug(s"  first way in unidirectional element")

    finalizeCurrentElement()
    elements.lastOption match {
      case None => throw new Exception("not implemented yet")
      case Some(previousElement) =>
        if (previousElement.endNodeId == currentWayMember.startNode.id) {
          oneWayDirection = Some("down")
        }
        else if (previousElement.endNodeId == currentWayMember.startNode.id) {
          oneWayDirection = Some("up")
        }
        else {
          finalizeCurrentGroup()
          oneWayDirection = Some("down")
        }
    }

    currentElementFragments.addOne(
      MonitorRouteFragment(
        currentWayMember.way,
        reversed = currentWayMember.hasRoleBackward
      )
    )
  }

  private def processNextUnidirectionalWay(): Unit = {
    if (oneWayDirection.contains("down")) {
      findPreviousFragment() match {
        case None =>

          throw new Exception("TODO")

        case Some(previousFragment) =>
          if (currentWayMember.startNode.id == previousFragment.endNode.id) {
            addFragment(currentWayMember)
          }
          else {
            currentElementFragments.headOption match {
              case None => finalizeCurrentGroup()
              case Some(firstFragmentInElement) =>
                if (currentWayMember.endNode.id == firstFragmentInElement.startNode.id) {
                  // switch
                  finalizeCurrentElement()
                  oneWayDirection = Some("up")
                  addFragment(currentWayMember)
                }
                else {
                  finalizeCurrentGroup()
                }
            }
          }
      }
    }
    else {
      findPreviousFragment() match {
        case None => throw new Exception("illegal state?")
        case Some(previousFragment) =>

          if (currentWayMember.endNode.id == previousFragment.startNode.id) {
            addFragment(currentWayMember)
          }
          else {
            currentElementFragments.headOption match {
              case None =>
                finalizeCurrentGroup()
              case Some(firstFragmentInElement) =>
                // TODO following lines probably not ok
                if (currentWayMember.endNode.id == firstFragmentInElement.startNode.id) {
                  // switch
                  finalizeCurrentElement()
                  oneWayDirection = Some("down")
                  addFragment(currentWayMember)
                }
                else {
                  reverseFragments()
                  finalizeCurrentGroup()
                }
            }
          }
      }
    }
  }

  private def finalizeCurrentElement(): Unit = {
    if (currentElementFragments.nonEmpty) {
      addElement(currentElementFragments.toSeq)
      currentElementFragments.clear()
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

  private def addElement(fragments: Seq[MonitorRouteFragment]): Unit = {
    debug(s"  add element: direction=$oneWayDirection, fragments: ${fragments.map(_.string).mkString(", ")}")
    elements.addOne(MonitorRouteElement.from(fragments, oneWayDirection))
  }

  private def currentEndNodeId(): Option[Long] = {
    currentElementFragments.lastOption match {
      case Some(fragment) =>
        if (oneWayDirection.contains("up")) {
          Some(fragment.startNode.id)
        }
        else {
          Some(fragment.endNode.id)
        }

      case None =>
        elements.lastOption match {
          case Some(element) =>
            val fragment = element.fragments.last
            if (oneWayDirection.contains("up")) {
              Some(fragment.startNode.id)
            }
            else {
              Some(fragment.endNode.id)
            }
          case None => None
        }
    }
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

  private def addFragment(wayMember: WayMember): Unit = {
    currentElementFragments.addOne(MonitorRouteFragment(currentWayMember.way))
  }

  private def addFragmentReversed(wayMember: WayMember): Unit = {
    currentElementFragments.addOne(MonitorRouteFragment(currentWayMember.way, reversed = true))
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
