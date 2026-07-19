package kpn.server.monitor.route.update

import kpn.database.base.Database
import org.scalamock.stubs.Stub
import org.scalamock.stubs.Stubs

class MonitorUpdaterSetup(database: Database) extends Stubs {
  val monitorRouteRelationRepository: Stub[MonitorRouteRelationRepository] = stub[MonitorRouteRelationRepository]
  val monitorRouteStructureLoader: Stub[MonitorRouteStructureLoader] = stub[MonitorRouteStructureLoader]
  val configuration = new MonitorUpdaterConfiguration(
    database,
    monitorRouteRelationRepository,
    monitorRouteStructureLoader
  )
}
