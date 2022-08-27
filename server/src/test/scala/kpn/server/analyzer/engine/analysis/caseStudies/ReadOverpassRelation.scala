package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Timestamp
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryNode
import kpn.core.overpass.QueryRelation
import kpn.core.tools.monitor.MonitorDemoRoute
import org.apache.commons.io.FileUtils

import java.io.File

object ReadOverpassRelation {

  def main1(args: Array[String]): Unit = {
    val routeIds = MonitorDemoRoute.routes.flatMap(_.relationId).distinct.sorted
    routeIds.foreach { routeId =>
      saveRelation(None, s"/kpn/monitor-demo/$routeId.xml", routeId)
    }
  }

  def main(args: Array[String]): Unit = {
    val dir = "/home/marcv/wrk/projects1/knooppuntnet/server/src/test/resources/case-studies/"
    saveRelation(None, s"$dir/12280062L", 12280062L)
  }

  private def saveRelation(timestamp: Option[Timestamp], filename: String, relationId: Long): Unit = {
    println(s"save $filename")
    val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
    val xml = overpassQueryExecutor.executeQuery(timestamp, QueryRelation(relationId))
    FileUtils.writeStringToFile(new File(filename), xml, "UTF-8")
  }

  private def saveNode(timestamp: Option[Timestamp], filename: String, nodeId: Long): Unit = {
    println(s"save $filename")
    val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
    val xml = overpassQueryExecutor.executeQuery(timestamp, QueryNode(nodeId))
    FileUtils.writeStringToFile(new File(filename), xml, "UTF-8")
  }
}
