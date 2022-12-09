package kpn.core.tools.monitor

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.api.monitor.route.MonitorRouteRelationRepository
import kpn.server.repository.MonitorGroupRepositoryImpl
import kpn.server.repository.MonitorRouteRepositoryImpl

object MonitorRouteMigrationTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-experimental") { database =>
      val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
      new MonitorRouteMigrationTool(database, overpassQueryExecutor).migrate()
    }
  }
}

class MonitorRouteMigrationTool(database: Database, overpassQueryExecutor: OverpassQueryExecutor) {

  private val groupRepository = new MonitorGroupRepositoryImpl(database)
  private val routeRepository = new MonitorRouteRepositoryImpl(database)
  private val routeRelationRepository = new MonitorRouteRelationRepository(overpassQueryExecutor)

  def migrate(): Unit = {
    groupRepository.groups().sortBy(_.name).foreach { group =>
      groupRepository.groupRoutes(group._id).sortBy(_.name).foreach { route =>
        println(s"${group.name}:${route.name}")
        route.relationId match {
          case None => println("    no relationId")
          case Some(relationId) =>
            routeRelationRepository.load(None, relationId) match {
              case None => println("    could not load relation from overpass")
              case Some(relation) =>
                println("    migrated")
                routeRepository.saveRoute(
                  route.copy(
                    relation = Some(
                      MonitorRouteRelation.from(relation, None)
                    )
                  )
                )
            }
        }
      }
    }
  }
}
