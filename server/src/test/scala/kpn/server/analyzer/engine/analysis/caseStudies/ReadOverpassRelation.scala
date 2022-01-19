package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Timestamp
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryRelation
import kpn.core.tools.monitor.MonitorDemoRoute
import org.apache.commons.io.FileUtils

import java.io.File

object ReadOverpassRelation {

  def main1(args: Array[String]): Unit = {
    val routeIds = MonitorDemoRoute.routes.filter(_.routeId > 1L).map(_.routeId).distinct.sorted
    routeIds.foreach { routeId =>
      saveRelation(None, s"/kpn/monitor-demo/$routeId.xml", routeId)
    }
  }

  def main(args: Array[String]): Unit = {
    val dir = "/home/marcv/wrk/projects1/knooppuntnet/server/src/test/resources/case-studies/"
    saveRelation(Some(Timestamp(2022, 1, 20, 0, 0, 0)), dir + "13519504.xml", 13519504L)
    saveRelation(Some(Timestamp(2022, 1, 20, 0, 0, 0)), dir + "13619463.xml", 13619463L)
    saveRelation(Some(Timestamp(2022, 1, 20, 0, 0, 0)), dir + "13626627.xml", 13626627L)
    saveRelation(Some(Timestamp(2022, 1, 20, 0, 0, 0)), dir + "11829059.xml", 11829059L)
    saveRelation(Some(Timestamp(2022, 1, 20, 0, 0, 0)), dir + "11829061.xml", 11829061L)
    saveRelation(Some(Timestamp(2022, 1, 20, 0, 0, 0)), dir + "13504960.xml", 13504960L)
    saveRelation(Some(Timestamp(2022, 1, 20, 0, 0, 0)), dir + "13508056.xml", 13508056L)
    saveRelation(Some(Timestamp(2022, 1, 20, 0, 0, 0)), dir + "13508061.xml", 13508061L)

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
}
