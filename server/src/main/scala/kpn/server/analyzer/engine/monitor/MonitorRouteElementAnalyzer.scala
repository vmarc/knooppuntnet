package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.Member
import kpn.api.common.data.RelationMember
import kpn.api.common.data.WayMember

import scala.collection.mutable

class MonitorRouteElementAnalyzer {

  private val connectionAnalyzer = new MonitorRouteConnectionAnalyzer
  private val nextRouteAnalyzer = new MonitorRouteNextRouteAnalyzer

  private var startWayMember: Option[WayMember] = None
  private var processedStartWayMember = false

  val elements = mutable.Buffer[MonitorRouteElement]()
  val currentElementFragments = mutable.Buffer[MonitorRouteFragment]()
  var loopFragments: Option[Seq[MonitorRouteFragment]] = None
  var endNodeId: Long = 0

  def analyzeRoute(members: Seq[Member]): Seq[MonitorRouteElement] = {
    members.foreach { member =>
      member match {
        case wayMember: WayMember => processWayMember(wayMember)
        case relationMember: RelationMember => processRelationMember(relationMember)
        case _ => None
      }
    }

    startWayMember match {
      case None => // nothing more to do here
      case Some(wayMember) =>
        if (processedStartWayMember == false) {
          currentElementFragments.addOne(MonitorRouteFragment.from(wayMember))
        }
    }

    if (currentElementFragments.nonEmpty) {
      elements.addOne(MonitorRouteElement.from(currentElementFragments.toSeq))
    }
    elements.toSeq
  }

  private def processRelationMember(relationMember: RelationMember): Unit = {
    // still to be implemented
  }

  private def processWayMember(currentWayMember: WayMember): Unit = {
    if (currentWayMember.way.nodes.length >= 2) {
      if (!processedStartWayMember) {
        startWayMember match {
          case None =>
            startWayMember = Some(currentWayMember)
          case Some(wayMember) =>
            processStart(wayMember, currentWayMember)
        }
      }
      else {
        processNextWayMember(currentWayMember)
      }
    }
  }

  private def processStart(wayMember1: WayMember, wayMember2: WayMember): Unit = {
    val routeWays = connectionAnalyzer.analyze(wayMember1, wayMember2)
    if (routeWays.isEmpty) {
      // false start, the first 2 ways do not connect
      //   make way1 a separate segment
      //   make way2 the first way
      elements.addOne(MonitorRouteElement.from(Seq(MonitorRouteFragment.from(wayMember1))))
      startWayMember = Some(wayMember2)
    }
    else {
      currentElementFragments.addAll(routeWays)
      startWayMember = None
      processedStartWayMember = true
      endNodeId = routeWays.last.endNode.id
    }
  }

  private def processNextWayMember(wayMember: WayMember): Unit = {
    loopFragments match {
      case Some(loopRouteWays) => processLoop(loopRouteWays, wayMember)
      case None => processNextWayMemberAnalyze(wayMember)
    }
  }

  private def processNextWayMemberAnalyze(wayMember: WayMember): Unit = {
    nextRouteAnalyzer.analyze(endNodeId, wayMember) match {
      case Some(routeWay) =>
        endNodeId = routeWay.endNode.id

        currentElementFragments.addOne(routeWay)

        val startNodeIds = currentElementFragments.map(_.startNode.id)

        if (startNodeIds.contains(endNodeId)) {
          // loop, split currentSegment
          val index = currentElementFragments.lastIndexWhere(_.startNode.id == endNodeId)
          val segmentBeforeLoop = currentElementFragments.take(index)
          loopFragments = Some(currentElementFragments.drop(index).toSeq)
          elements.addOne(MonitorRouteElement.from(segmentBeforeLoop.toSeq))
          currentElementFragments.clear()
        }

      case None =>
        if (currentElementFragments.nonEmpty) {
          elements.addOne(MonitorRouteElement.from(currentElementFragments.toSeq))
          currentElementFragments.clear()
        }
        startWayMember = Some(wayMember)
        processedStartWayMember = false
    }
  }

  private def processLoop(loopRouteWays: Seq[MonitorRouteFragment], wayMember: WayMember): Unit = {
    val startNodeId = wayMember.way.nodes.head.id
    val endNodeId = wayMember.way.nodes.last.id

    val index = loopRouteWays.indexWhere { routeWay =>
      routeWay.endNode.id == startNodeId
    }
    if (index >= 0) {
      val routeWays1 = loopRouteWays.take(index + 1)
      val routeWays2 = loopRouteWays.drop(index + 1)
      elements.addOne(MonitorRouteElement.from(routeWays1))
      elements.addOne(MonitorRouteElement.from(routeWays2))
    }
    else {
      elements.addOne(MonitorRouteElement.from(loopRouteWays))
    }

    startWayMember = Some(wayMember)
    processedStartWayMember = false
  }

  private def debug(message: String): Unit = {
    println(message)
  }
}
