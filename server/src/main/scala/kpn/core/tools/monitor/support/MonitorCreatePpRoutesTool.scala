package kpn.core.tools.monitor.support

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.monitor.route.update.MonitorRouteRelationRepository
import kpn.server.monitor.route.update.MonitorRouteStructureLoader
import kpn.server.monitor.route.update.MonitorUpdateArgs
import kpn.server.monitor.route.update.MonitorUpdateReporterLogger
import kpn.server.monitor.route.update.MonitorUpdaterConfiguration

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
      val targetRouteName = s"PP-${route.name}"
      configuration.monitorRouteRepository.routeByName(targetGroup._id, targetRouteName) match {
        case Some(targetRoute) =>
        case None =>
          if (route.relationId.nonEmpty && route.osmSegmentCount > 0) {
            configuration.monitorRouteUpdateExecutor.execute(
              MonitorUpdateArgs(
                "create-pp-routes",
                new MonitorUpdateReporterLogger(),
                MonitorRouteUpdate(
                  action = MonitorAction.add,
                  groupName = targetGroup.name,
                  routeName = targetRouteName,
                  referenceType = MonitorReferenceType.osmNow,
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
