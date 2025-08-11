package kpn.server.monitor.route.update

import kpn.database.base.Database
import org.scalamock.stubs.Stubs

object MonitorUpdaterTestSupport extends Stubs {

  def configuration(database: Database): MonitorUpdaterConfiguration = {
    val monitorRouteRelationRepository: MonitorRouteRelationRepository = stub[MonitorRouteRelationRepository]
    val monitorRouteStructureLoader: MonitorRouteStructureLoader = stub[MonitorRouteStructureLoader]
    new MonitorUpdaterConfiguration(
      database,
      monitorRouteRelationRepository,
      monitorRouteStructureLoader
    )
  }
}
