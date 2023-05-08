package kpn.server.monitor.route

import kpn.database.base.Database
import org.scalamock.scalatest.MockFactory

object MonitorUpdaterTestSupport extends MockFactory {

  def configuration(database: Database): MonitorUpdaterConfiguration = {
    val monitorRouteRelationRepository: MonitorRouteRelationRepository = stub[MonitorRouteRelationRepository]
    new MonitorUpdaterConfiguration(database, monitorRouteRelationRepository)
  }
}
