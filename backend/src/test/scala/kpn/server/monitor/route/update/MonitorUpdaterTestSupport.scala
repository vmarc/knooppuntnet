package kpn.server.monitor.route.update

import kpn.database.base.Database
import org.scalamock.stubs.Stub
import org.scalamock.stubs.Stubs

object MonitorUpdaterTestSupport extends Stubs {

  def configuration(database: Database): MonitorUpdaterConfiguration = {
    val monitorRouteRelationRepository: Stub[MonitorRouteRelationRepository] = stub[MonitorRouteRelationRepository]
    val monitorRouteStructureLoader: Stub[MonitorRouteStructureLoader] = stub[MonitorRouteStructureLoader]
    new MonitorUpdaterConfiguration(
      database,
      monitorRouteRelationRepository,
      monitorRouteStructureLoader
    )
  }
}
