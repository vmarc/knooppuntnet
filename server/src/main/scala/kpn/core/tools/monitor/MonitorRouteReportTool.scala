package kpn.core.tools.monitor

import kpn.database.util.Mongo
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorGroupRepositoryImpl
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl

object MonitorRouteReportTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      val groupRepository = new MonitorGroupRepositoryImpl(database)
      val routeRepository = new MonitorRouteRepositoryImpl(database)
      new MonitorRouteReportTool(groupRepository, routeRepository).report()
    }
  }
}

class MonitorRouteReportTool(
  groupRepository: MonitorGroupRepository,
  routeRepository: MonitorRouteRepository
) {
  def report(): Unit = {
    groupRepository.groups().sortBy(_.name).foreach { group =>
      println(s"group ${group.name}")
      groupRepository.groupRoutes(group._id).sortBy(_.name).foreach { route =>
        val stateCount = routeRepository.routeStateCount(route._id)
        val stateSize = routeRepository.routeStateSize(route._id)
        println(s"  route ${route.name}: $stateCount states, size=${"%.0fMb".format(stateSize / 1000000d)}")
      }
    }
  }
}
