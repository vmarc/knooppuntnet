package kpn.core.tools.monitor

import kpn.api.base.ObjectId
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.route.MonitorRouteRelationRepository
import kpn.server.monitor.route.MonitorRouteStructureLoader
import kpn.server.monitor.route.MonitorUpdaterConfiguration

object MonitorUpdateTool {
  private val log = Log(classOf[MonitorUpdateTool])

  def main(args: Array[String]): Unit = {

    val exit: Int = try {
      MonitorUpdateToolOptions.parse(args) match {
        case Some(options) =>
          Mongo.executeIn(options.databaseName) { database =>
            val overpassQueryExecutor = {
              if (options.remote) {
                new OverpassQueryExecutorRemoteImpl()
              }
              else {
                new OverpassQueryExecutorImpl()
              }
            }
            new MonitorUpdateTool(database, overpassQueryExecutor).analyze()
          }
          log.info("Done")
          0

        case None =>
          // arguments are bad, error message will have been displayed
          -1
      }
    }
    catch {
      case e: Throwable =>
        log.error(e.getMessage, e)
        -1
    }

    System.exit(exit)
  }
}

case class MonitorGroupRoute(
  group: MonitorGroup,
  routeId: ObjectId
)

class MonitorUpdateTool(
  database: Database,
  overpassQueryExecutor: OverpassQueryExecutor
) {

  private val monitorRouteRelationRepository = new MonitorRouteRelationRepository(overpassQueryExecutor)
  private val monitorRouteStructureLoader = new MonitorRouteStructureLoader(overpassQueryExecutor)
  private val configuration = new MonitorUpdaterConfiguration(
    database,
    monitorRouteRelationRepository,
    monitorRouteStructureLoader
  )

  def analyze(): Unit = {
    val groups = configuration.monitorGroupRepository.groups().sortBy(_.name)
    val groupRoutes = groups.flatMap { group =>
      configuration.monitorGroupRepository.groupRouteIds(group._id).map { routeId =>
        MonitorGroupRoute(group, routeId)
      }
    }

    groupRoutes.zipWithIndex.foreach { case (groupRoute, index) =>
      Log.context(s"${index + 1}/${groupRoutes.size}") {
        configuration.monitorUpdater.analyzeAll(groupRoute.group, groupRoute.routeId)
      }
    }
  }
}
