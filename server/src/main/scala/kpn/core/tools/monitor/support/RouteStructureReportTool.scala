package kpn.core.tools.monitor

import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryRelation

import scala.xml.XML

object RouteStructureReportTool {
  def main(args: Array[String]): Unit = {
    new RouteStructureReportTool().report()
  }
}

class RouteStructureReportTool {
  def report(): Unit = {
    val relationId = 12692749L
    val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
    val xmlString = overpassQueryExecutor.executeQuery(None, QueryRelation(relationId))
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    val data = new DataBuilder(rawData).data

    data.relations.get(relationId) match {
      case None =>
      case Some(rootRelation) =>
        rootRelation.relationMembers.foreach { subRelation =>
          val relation = subRelation.relation
          val startWay = relation.wayMembers.head
          val endWay = relation.wayMembers.last
          val startNodeId1 = startWay.way.nodes.head.id
          val startNodeId2 = startWay.way.nodes.last.id
          val endNodeId1 = endWay.way.nodes.last.id
          val endNodeId2 = endWay.way.nodes.head.id
          println(s"${relation.id} ${startNodeId1}  ${startNodeId2} ${endNodeId1} ${endNodeId2}")
        }
    }
  }
}
