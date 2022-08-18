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
    saveNode(Some(Timestamp(2021, 7, 13, 21, 7, 33)), dir + "node-2969204425-before.xml", 2969204425L)
    saveNode(Some(Timestamp(2021, 7, 13, 21, 7, 35)), dir + "node-2969204425-after.xml", 2969204425L)
    // 2021-07-13 21:07:34


//    // 2021-12-11T11\:49\:54Z
//    saveRelation(Some(Timestamp(2021, 12, 11, 11, 49, 17)), "/kpn/wrk/before-12280144.xml", 12280144L)
//    saveRelation(Some(Timestamp(2021, 12, 11, 11, 49, 17)), "/kpn/wrk/before-13538767.xml", 13538767L)
//
//    saveRelation(Some(Timestamp(2021, 12, 11, 11, 49, 19)), "/kpn/wrk/after-12280144.xml", 12280144L)
//    saveRelation(Some(Timestamp(2021, 12, 11, 11, 49, 19)), "/kpn/wrk/after-13538767.xml", 13538767L)
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
