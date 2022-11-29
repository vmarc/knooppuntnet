package kpn.core.tools.monitor

import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.repository.MonitorGroupRepositoryImpl
import kpn.server.repository.MonitorRouteRepositoryImpl

object MonitorRouteStateCleanupTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      new MonitorRouteMigrationTool(database).migrate()
    }
  }
}

class MonitorRouteStateCleanupTool(database: Database) {

  private val groupRepository = new MonitorGroupRepositoryImpl(database)
  private val routeRepository = new MonitorRouteRepositoryImpl(database)

  def cleanup(): Unit = {
    groupRepository.groups().sortBy(_.name).foreach { group =>
      println(s"group ${group.name}")
      groupRepository.groupRoutes(group._id).sortBy(_.name).foreach { route =>
        val stateInfos = routeRepository.routeStateInfos(route._id)
        if (stateInfos.nonEmpty) {
          val obsoleteStates = stateInfos.sortBy(_.timestamp).reverse.tail
          println(s"  route ${route.name}: ${obsoleteStates.size} obsolete states")
          obsoleteStates.zipWithIndex.foreach { case (stateInfo, index) =>
            database.monitorRouteStates.deleteByObjectId(stateInfo._id)
            println(s"    ${group.name} ${route.name} ${index + 1}/${obsoleteStates.size} ${stateInfo.timestamp.yyyymmddhhmmss}")
          }
        }
      }
    }
  }
}
