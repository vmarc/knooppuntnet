package kpn.core.tools.monitor.support

import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.monitor.route.update.MonitorRouteRelationRepository
import kpn.server.monitor.route.update.MonitorRouteStructureLoader
import kpn.server.monitor.route.update.MonitorUpdateContext
import kpn.server.monitor.route.update.MonitorUpdaterConfiguration
import kpn.server.monitor.route.update.MonitorUpdateReporterLogger

object MonitorCreatePpRoutesTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      val tool = new MonitorCreatePpRoutesTool(configuration(database))
      tool.run()
    }
  }

  private def configuration(database: Database): MonitorUpdaterConfiguration = {
    val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
    val monitorRouteRelationRepository = new MonitorRouteRelationRepository(overpassQueryExecutor)
    val monitorRouteStructureLoader = new MonitorRouteStructureLoader(overpassQueryExecutor)
    new MonitorUpdaterConfiguration(
      database,
      monitorRouteRelationRepository,
      monitorRouteStructureLoader
    )
  }
}

class MonitorCreatePpRoutesTool(configuration: MonitorUpdaterConfiguration) {

  def run(): Unit = {

    val sourceGroup = configuration.monitorGroupRepository.groupByName("SGR").get
    val targetGroup = configuration.monitorGroupRepository.groupByName("SGR-PP").get

    configuration.monitorGroupRepository.groupRoutes(sourceGroup._id).sortBy(_.name).foreach { route =>
      val targetRouteName = "PP-" + route.name
      configuration.monitorRouteRepository.routeByName(targetGroup._id, targetRouteName) match {
        case Some(targetRoute) =>
        case None =>
          if (route.relationId.nonEmpty && route.osmSegmentCount > 0) {
            configuration.monitorRouteUpdateExecutor.execute(
              MonitorUpdateContext(
                "create-pp-routes",
                new MonitorUpdateReporterLogger(),
                MonitorRouteUpdate(
                  action = "add",
                  groupName = targetGroup.name,
                  routeName = targetRouteName,
                  referenceType = "osm",
                  referenceNow = Some(true),
                  description = Some(route.description),
                  comment = route.comment,
                  relationId = route.relationId,
                )
              )
            )
          }
      }
    }
  }
}
