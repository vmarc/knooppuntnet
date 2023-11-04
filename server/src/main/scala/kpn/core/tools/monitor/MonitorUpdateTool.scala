package kpn.core.tools.monitor

import kpn.api.base.ObjectId
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.route.update.MonitorRouteRelationRepository
import kpn.server.monitor.route.update.MonitorRouteStructureLoader
import kpn.server.monitor.route.update.MonitorUpdaterConfiguration

object MonitorUpdateTool {
  private val log = Log(classOf[MonitorUpdateTool])

  def main(args: Array[String]): Unit = {

    val exit: Int = try {
      MonitorUpdateToolOptions.parse(args) match {
        case Some(options) =>
          log.infoElapsed {
            Mongo.executeIn(options.databaseName) { database =>
              val overpassQueryExecutor = {
                if (options.remote) {
                  new OverpassQueryExecutorRemoteImpl()
                }
                else {
                  new OverpassQueryExecutorImpl()
                }
              }
              val tool = new MonitorUpdateTool(database, overpassQueryExecutor)
              //              tool.testUpdate("A", "_SP12")
              tool.update()
            }
            ("update completed", ())
          }
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

  def testUpdate(groupName: String, routeName: String): Unit = {
    configuration.monitorGroupRepository.groupByName(groupName) match {
      case None => MonitorUpdateTool.log.error(s"group not found: $groupName")
      case Some(group) =>
        configuration.monitorRouteRepository.routeByName(group._id, routeName) match {
          case None => MonitorUpdateTool.log.error(s"route not found: $groupName, $routeName")
          case Some(route) => updateAnalysis(group, route)
        }
    }
  }

  def update(): Unit = {
    val groups = configuration.monitorGroupRepository.groups().sortBy(_.name)
    val groupRoutes = groups.foreach { group =>
      configuration.monitorGroupRepository.groupRoutes(group._id).sortBy(_.name).foreach { route =>
        updateAnalysis(group, route)
      }
    }
  }

  private def updateAnalysis(group: MonitorGroup, route: MonitorRoute): Unit = {
    configuration.monitorRouteUpdateExecutor.updateAnalysis(group, route)
  }
}
