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
  private var contextEndNodeId: Option[Long] = None

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
      if (elementDirection.isEmpty) {
        processFirstUnidirectionalWay()
      }
      else {
        processNextUnidirectionalWay()
      }
    }
    else {
      if (elementDirection.nonEmpty) {
        debug(s"  first way after unidirectional element")

        elementDirection match {
          case Some(ElementDirection.Up) => reverseFragments()
          case _ =>
        }

        finalizeCurrentElement() match {
          case None =>
          case Some(element) =>
            if (element.isLoop) {
              val elementNodeIds = element.fragments.zipWithIndex.flatMap { case (fragment, index) =>
                if (index == 0) {
                  Seq(fragment.startNode.id, fragment.endNode.id)
                }
                else {
                  Seq(fragment.endNode.id)
                }
              }

              val splitNodeIdOption = elementNodeIds.find { nodeId =>
                nodeId == contextCurrentWayMember.startNode.id || nodeId == contextCurrentWayMember.endNode.id
              }

              splitNodeIdOption match {
                case None => // no connection, leave the loop as-is
                case Some(splitNodeId) =>
                  // split
                  val fragments1 = element.fragments.takeWhile(fragment => fragment.startNode.id != splitNodeId)
                  val fragments2 = element.fragments.drop(fragments1.length)

                  val oppositeDirection = elementDirection match {
                    case Some(ElementDirection.Down) => Some(ElementDirection.Up)
                    case Some(ElementDirection.Up) => Some(ElementDirection.Down)
                    case _ => None
                  }
                  val element1 = MonitorRouteElement.from(fragments1, elementDirection)
                  val element2 = MonitorRouteElement.from(fragments2, oppositeDirection)

                  debug(s"    split element1: direction=${element1.direction}, fragments: ${element1.fragments.map(_.string).mkString(", ")}")
                  debug(s"    split element2: direction=${element2.direction}, fragments: ${element2.fragments.map(_.string).mkString(", ")}")

                  elements.remove(elements.length - 1)
                  elements.addOne(element1)
                  elements.addOne(element2)
                  contextEndNodeId = Some(splitNodeId)
              }
            }
        }

        elementDirection = None
        addBidirectionalFragmentByLookingAheadAtNextWayMember()
      }
      else {
        contextEndNodeId match {
          case Some(endNodeId) =>
            if (endNodeId == contextCurrentWayMember.startNode.id) {
              addFragment(contextCurrentWayMember)
            }
            else if (endNodeId == contextCurrentWayMember.endNode.id) {
              addFragmentReversed(contextCurrentWayMember)
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

        contextEndNodeId match {
          case None => addFragment(contextCurrentWayMember)
          case Some(endNodeId) =>

            if (contextCurrentWayMember.startNode.id == endNodeId) {
              addFragment(contextCurrentWayMember)
            }
            else if (contextCurrentWayMember.endNode.id == endNodeId) {
              addFragmentReversed(contextCurrentWayMember)
            }
            else {
              finalizeCurrentGroup()
              addFragment(contextCurrentWayMember)
            }
        }

      case Some(nextWayMember) =>

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
            addFragment(contextCurrentWayMember)
            finalizeCurrentGroup()

          case Some(nodeId) =>
            if (nodeId == contextCurrentWayMember.way.nodes.last.id) {
              addFragment(contextCurrentWayMember)
            }
            else {
              addFragmentReversed(contextCurrentWayMember)
            }
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

    doAddFragment(
      MonitorRouteFragment(
        contextCurrentWayMember.way,
        reversed = contextCurrentWayMember.hasRoleBackward
      )
    )
  }

  private def processNextUnidirectionalWay(): Unit = {
    if (elementDirection.contains(ElementDirection.Down)) {
      findPreviousFragment() match {
        case None =>
          throw new Exception("illegal state??")

        case Some(previousFragment) =>
          if (contextCurrentWayMember.startNode.id == previousFragment.endNode.id) {
            addFragment(contextCurrentWayMember)
          }
          else {
            currentElementFragments.headOption match {
              case None => finalizeCurrentGroup()
              case Some(firstFragmentInElement) =>
                if (contextCurrentWayMember.endNode.id == firstFragmentInElement.startNode.id) {
                  // switch
                  finalizeCurrentElement()
                  elementDirection = Some(ElementDirection.Up)
                  addFragment(contextCurrentWayMember)
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

          if (contextCurrentWayMember.endNode.id == previousFragment.startNode.id) {
            addFragment(contextCurrentWayMember)
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
                  addFragment(contextCurrentWayMember)
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

  private def addFragment(wayMember: WayMember): Unit = {
    doAddFragment(MonitorRouteFragment(contextCurrentWayMember.way))
  }

  private def addFragmentReversed(wayMember: WayMember): Unit = {
    doAddFragment(MonitorRouteFragment(contextCurrentWayMember.way, reversed = true))
  }

  private def doAddFragment(fragment: MonitorRouteFragment): Unit = {
    currentElementFragments.addOne(fragment)
    contextEndNodeId = Some(fragment.endNode.id)
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
