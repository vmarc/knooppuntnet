package kpn.core.tools.monitor

import kpn.database.util.Mongo
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorGroupRepositoryImpl
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl

object MonitorRouteReportTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-monitor") { database =>
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
        val hasRole = route.relation match {
          case None => false
          case Some(relation) =>
            relation.relations.flatMap(_.role).size > 0
        }
        println(s"  route ${route.name} has role: $hasRole")
      }
    }
  }
}
