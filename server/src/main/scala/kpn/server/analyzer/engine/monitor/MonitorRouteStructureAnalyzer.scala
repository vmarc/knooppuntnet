package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.RelationMember
import kpn.api.common.data.WayMember
import kpn.api.custom.Relation
import kpn.server.analyzer.engine.analysis.route.RouteWay

import scala.collection.mutable

class MonitorRouteStructureAnalyzer {

  private val connectionAnalyzer = new MonitorRouteConnecctionAnalyzer

  private var startWayMember: Option[WayMember] = None
  private var processedStartWayMember = false

  val segments = mutable.Buffer[Seq[RouteWay]]()
  val currentSegment = mutable.Buffer[RouteWay]()
  var endNodeId: Long = 0

  def analyzeRoute(relation: Relation): Seq[Seq[RouteWay]] = {
    relation.members.foreach { member =>
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
          currentSegment.addOne(RouteWay(wayMember.way))
        }
    }

    if (currentSegment.nonEmpty) {
      segments.addOne(currentSegment.toSeq)
    }
    segments.toSeq
  }

  private def processRelationMember(relationMember: RelationMember): Unit = {
    // TODO implement
  }

  private def processWayMember(currentWayMember: WayMember): Unit = {

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

  private def processStart(wayMember1: WayMember, wayMember2: WayMember): Unit = {
    val routeWays = connectionAnalyzer.analyze(wayMember1, wayMember2)
    if (routeWays.isEmpty) {
      // false start, the first 2 ways do not connect
      //   make way1 a separate segment
      //   make way2 the first way
      segments.addOne(Seq(RouteWay(wayMember1.way)))
      startWayMember = Some(wayMember2)
    }
    else {
      currentSegment.addAll(routeWays)
      startWayMember = None
      processedStartWayMember = true
      // TODO endNodeId: handle way without nodes or only one node
      endNodeId = routeWays.last.endNode.id
    }
  }

  private def processNextWayMember(wayMember: WayMember): Unit = {
    if (endNodeId == wayMember.way.nodes.head.id) {
      currentSegment.addOne(RouteWay(wayMember.way))
      endNodeId = wayMember.way.nodes.last.id
    }
    else if (endNodeId == wayMember.way.nodes.last.id) {
      currentSegment.addOne(RouteWay(wayMember.way, reversed = true))
      endNodeId = wayMember.way.nodes.head.id
    }
    else {
      if (currentSegment.nonEmpty) {
        segments.addOne(currentSegment.toSeq)
        currentSegment.clear()
      }
      startWayMember = Some(wayMember)
      processedStartWayMember = false
    }
  }

  private def debug(message: String): Unit = {
    println(message)
  }
}
