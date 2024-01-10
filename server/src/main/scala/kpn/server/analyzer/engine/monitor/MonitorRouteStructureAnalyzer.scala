package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.RelationMember
import kpn.api.common.data.Way
import kpn.api.common.data.WayMember
import kpn.api.custom.Relation
import kpn.server.analyzer.engine.analysis.route.RouteWay

import scala.collection.mutable

class MonitorRouteStructureAnalyzer {

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
    // TODO still have to look at role "forward" here

    val routeWays = firstConnection(wayMember1.way, wayMember2.way)
    if (routeWays.isEmpty) {
      // false start, the first 2 ways do not connect
      //   make way1 a separate segment
      //   make way2 the first way
      segments.addOne(Seq(RouteWay(wayMember1.way)))
      startWayMember = Some(wayMember2)
    }
    else {
      currentSegment.addAll(routeWays)
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

  private def firstConnection(way1: Way, way2: Way): Seq[RouteWay] = {
    if (way1.nodes.last.id == way2.nodes.head.id) {
      Seq(
        RouteWay(way1),
        RouteWay(way2)
      )
    }
    else if (way1.nodes.last.id == way2.nodes.last.id) {
      Seq(
        RouteWay(way1),
        RouteWay(way2, reversed = true)
      )
    }
    else if (way1.nodes.head.id == way2.nodes.head.id) {
      Seq(
        RouteWay(way1, reversed = true),
        RouteWay(way2)
      )
    }
    else if (way1.nodes.head.id == way2.nodes.last.id) {
      Seq(
        RouteWay(way1, reversed = true),
        RouteWay(way2, reversed = true)
      )
    }
    else {
      Seq.empty
    }
  }

  private def debug(message: String): Unit = {
    println(message)
  }
}
