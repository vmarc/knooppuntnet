package kpn.core.tools.monitor

import kpn.api.custom.Relation
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryRelation
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport

import scala.xml.XML

object MonitorPerformanceAnalysisTool {
  def main(args: Array[String]): Unit = {
    new MonitorPerformanceAnalysisTool().analyze()
  }
}

class MonitorPerformanceAnalysisTool() {

  //private val routeId = 6276466L
  private val routeId = 8386002L
  private val log = Log(classOf[MonitorPerformanceAnalysisTool])
  private val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()

  def analyze(): Unit = {
    readRelation() match {
      case None => throw new IllegalArgumentException("relation not found")
      case Some(routeRelation) =>
        val routeSegments = MonitorRouteAnalysisSupport.toRouteSegments(routeRelation)
        println(s"segments: ${routeSegments.size}")
    }
  }

  private def readRelation(): Option[Relation] = {
    log.infoElapsed(
      "read relation",
      {
        val xmlString = overpassQueryExecutor.executeQuery(None, QueryRelation(routeId))
        val xml = XML.loadString(xmlString)
        val rawData = new Parser().parse(xml.head)
        val data = new DataBuilder(rawData).data
        data.relations.get(routeId)
      }
    )
  }
}
