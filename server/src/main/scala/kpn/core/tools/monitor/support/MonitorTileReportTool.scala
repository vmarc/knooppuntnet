package kpn.core.tools.monitor.support

import kpn.core.util.Log
import kpn.database.util.Mongo
import kpn.server.monitor.repository.MonitorRelationRepository
import kpn.server.monitor.repository.MonitorRelationRepositoryImpl
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl

object MonitorTileReportTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-monitor") { database =>
      val routeRepository = new MonitorRouteRepositoryImpl(database)
      val relationRepository = new MonitorRelationRepositoryImpl(database)
      val tool = new MonitorTileReportTool(
        routeRepository,
        relationRepository
      )
      tool.report()
    }
  }
}

class MonitorTileReportTool(
  routeRepository: MonitorRouteRepository,
  relationRepository: MonitorRelationRepository
) {
  private val log = Log(classOf[MonitorTileReportTool])

  def report(): Unit = {
    val tileNames = readTileNames()
    println("|level|tile count|")
    println("|---|---|")
    (1 to 14).foreach { level =>
      val tileCount = tileNames.filter(_.startsWith(s"$level-")).size
      println(s"|$level|$tileCount|")
    }
    val totalTileCount = tileNames.size
    println(s"|total|$totalTileCount|")
  }

  private def readTileNames(): Seq[String] = {
    log.infoElapsed {
      val tileNames = relationRepository.allTiles().map(_.name)
      (s"read ${tileNames.size} tile names", tileNames)
    }
  }
}
