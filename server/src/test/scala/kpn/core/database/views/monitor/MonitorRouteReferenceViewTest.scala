package kpn.core.database.views.monitor

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.MonitorAdminRouteRepositoryImpl

class MonitorRouteReferenceViewTest extends UnitTest with SharedTestObjects {

  test("reference") {
    withCouchDatabase { database =>
      val routeRepository = new MonitorAdminRouteRepositoryImpl(database)
      routeRepository.saveRouteState(newMonitorRouteState(101L, referenceKey = Some("20200811000000")))
      MonitorRouteReferenceView.reference(database, 101L, stale = false) should equal(Some("20200811000000"))
    }
  }
}
