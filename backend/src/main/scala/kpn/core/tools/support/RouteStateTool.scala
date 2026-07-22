package kpn.core.tools.support

import kpn.core.util.Log
import kpn.core.util.Util
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository

object RouteStateTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      new RouteStateTool(database).run()
    }
  }
}

class RouteStateTool(database: Database) {

  private val log = Log(classOf[RouteStateTool])

  def run(): Unit = {
    val groupName = "fr-nwn"
    val routeName = "GTJ"

    val monitorGroupRepository = new MonitorGroupRepository(database)
    val monitorRouteRepository = new MonitorRouteRepository(database)

    val group = log.infoElapsed {
      val result = monitorGroupRepository.groupByName(groupName).get
      ("groupByName", result)
    }

    val route = log.infoElapsed {
      val result = monitorRouteRepository.routeByName(group._id, routeName).get
      ("routeByName", result)
    }

    val routeState = log.infoElapsed {
      val result = monitorRouteRepository.state(route._id, route.relationId.get).get
      ("state", result)
    }

    println(s"${routeState.deviations.size} deviations")
    routeState.deviations.foreach { deviation =>
      println(s"  ${deviation.id} ${Util.humanReadableBytes(deviation.lines.map(_.length).sum)}")
    }
  }
}
