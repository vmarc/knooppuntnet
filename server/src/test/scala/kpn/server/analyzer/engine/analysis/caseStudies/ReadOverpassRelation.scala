package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Timestamp
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryRelation
import kpn.core.tools.monitor.MonitorDemoRoute

object ReadOverpassRelation {

  def main(args: Array[String]): Unit = {
    val routeIds = MonitorDemoRoute.routes.filter(_.routeId > 1L).map(_.routeId).distinct.sorted
    routeIds.foreach { routeId =>
      saveRelation(None, s"/kpn/monitor-demo/$routeId.xml", routeId)
    }
  }

  def main2(args: Array[String]): Unit = {

    // 2021-12-11T11\:49\:54Z
    saveRelation(Some(Timestamp(2021, 12, 11, 11, 49, 17)), "/kpn/wrk/before-12280144.xml", 12280144L)
    saveRelation(Some(Timestamp(2021, 12, 11, 11, 49, 17)), "/kpn/wrk/before-13538767.xml", 13538767L)

    saveRelation(Some(Timestamp(2021, 12, 11, 11, 49, 19)), "/kpn/wrk/after-12280144.xml", 12280144L)
    saveRelation(Some(Timestamp(2021, 12, 11, 11, 49, 19)), "/kpn/wrk/after-13538767.xml", 13538767L)
  }

  private def saveRelation(timestamp: Option[Timestamp], filename: String, relationId: Long): Unit = {
    println(s"save $filename")
    val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
    println(overpassQueryExecutor.executeQuery(None, QueryRelation(11527464L)))
  }
}
