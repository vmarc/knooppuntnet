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
              //  tool.testAnalyze("NL-LAW", "_LAW-2")
              //  tool.testAnalyze("NL-LAW", "_LAW-5-1")
              //  tool.testAnalyze("NL-LAW", "_LAW-5-2")
              //  tool.testAnalyze("NL-LAW", "_LAW-5-3")
              //  tool.testAnalyze("NL-LAW", "_LAW-9-1")
              //  tool.testAnalyze("NL-LAW", "_LAW-9-2")
              //  tool.testAnalyze("NL-LAW", "_SP12")
              // tool.testAnalyze("AAA", "A1") // 'osm' 2 subrelations
              // tool.testAnalyze("AAA", "T") // 'osm' 22 subrelations
              tool.testAnalyze("AAA", "A2") // 'osm' single relation

              // TODO osm single relation: 13150387 "Du Chemin Vendéen à la Voie de Tours, Surgères - Saintes"

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

  def testAnalyze(groupName: String, routeName: String): Unit = {
    configuration.monitorGroupRepository.groupByName(groupName) match {
      case None => MonitorUpdateTool.log.error(s"group not found: $groupName")
      case Some(group) =>
        configuration.monitorRouteRepository.routeByName(group._id, routeName) match {
          case None => MonitorUpdateTool.log.error(s"route not found: $groupName, $routeName")
          case Some(route) => updateAnalysis(group, route)
        }
    }
  }

  def analyze(): Unit = {
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
