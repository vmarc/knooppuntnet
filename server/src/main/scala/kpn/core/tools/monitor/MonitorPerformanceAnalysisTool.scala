package kpn.core.tools.monitor

import kpn.api.custom.Relation
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryRelation
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport
import org.apache.commons.io.FileUtils

import java.io.File
import scala.xml.XML

object MonitorPerformanceAnalysisTool {
  def main(args: Array[String]): Unit = {
    new MonitorPerformanceAnalysisTool().analyze()
    //new MonitorPerformanceAnalysisTool().saveRelation()
  }
}

class MonitorPerformanceAnalysisTool() {

  private val routeId = 14312416L

  private val log = Log(classOf[MonitorPerformanceAnalysisTool])
  private val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()

  def analyze(): Unit = {
    readRelation() match {
      case None => throw new IllegalArgumentException("relation not found")
      case Some(routeRelation) =>
        log.infoElapsed(
          "toRouteSegements",
          {
            val routeSegments = MonitorRouteAnalysisSupport.toRouteSegments(routeRelation)
            println(s"segments: ${routeSegments.size}")
          }
        )
    }
  }

  private def readRelation(): Option[Relation] = {
    log.infoElapsed(
      "read relation",
      {
        val xmlString = FileUtils.readFileToString(new File(s"/kpn/monitor/$routeId.xml"), "UTF-8")
        val xml = XML.loadString(xmlString)
        val rawData = new Parser().parse(xml.head)
        val data = new DataBuilder(rawData).data
        data.relations.get(routeId)
      }
    )
  }

  private def savReadRelation(): Option[Relation] = {
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

  def saveRelation(): Unit = {
    val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
    val xmlString = overpassQueryExecutor.executeQuery(None, QueryRelation(routeId))
    FileUtils.writeStringToFile(new File(s"/kpn/monitor/$routeId.xml"), xmlString, "UTF-8")
  }
}
