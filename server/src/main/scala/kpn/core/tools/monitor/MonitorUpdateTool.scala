package kpn.core.tools.monitor

import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Exit
import kpn.database.util.Mongo
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.route.update.MonitorRouteRelationRepository
import kpn.server.monitor.route.update.MonitorRouteStructureLoader
import kpn.server.monitor.route.update.MonitorUpdaterConfiguration

object MonitorUpdateTool {
  private val log = Log(classOf[MonitorUpdateTool])

  def main(args: Array[String]): Unit = {
    val exitCode = execute(args)
    System.exit(exitCode)
  }

  private def execute(args: Array[String]): Int = {
    try {
      MonitorUpdateToolOptions.parse(args) match {
        case Some(options) => executeWithOptions(options)
        case None =>
          // arguments are bad, error message will have been displayed
          Exit.Failure
      }
    } catch {
      case e: Exception =>
        log.error(e.getMessage)
        Exit.Failure
    }
  }

  private def executeWithOptions(options: MonitorUpdateToolOptions): Int = {
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
        tool.update()
      }
      ("update completed", ())
    }
    Exit.Success
  }
}

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
    groups.foreach { group =>
      configuration.monitorGroupRepository.groupRoutes(group._id).sortBy(_.name).foreach { route =>
        updateAnalysis(group, route)
      }
    }
  }

  private def updateAnalysis(group: MonitorGroup, route: MonitorRoute): Unit = {
    configuration.monitorRouteUpdateExecutor.updateAnalysis(group, route)
  }
}
