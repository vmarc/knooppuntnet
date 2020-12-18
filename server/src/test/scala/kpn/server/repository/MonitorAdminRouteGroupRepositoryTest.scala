package kpn.server.repository

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteGroup
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MonitorAdminRouteGroupRepositoryTest extends UnitTest with SharedTestObjects {

  test("all/add/delete") {
    withDatabase(true) { database =>
      val repository = new MonitorAdminRouteGroupRepositoryImpl(database)

      repository.all() should equal(Seq())
      repository.group("name1") should equal(None)

      repository.saveGroup(MonitorRouteGroup("name1", "description1"))
      repository.saveGroup(MonitorRouteGroup("name2", "description2"))
      repository.group("name1") should equal(Some(MonitorRouteGroup("name1", "description1")))
      repository.group("name2") should equal(Some(MonitorRouteGroup("name2", "description2")))
      repository.all(stale = false) should equal(
        Seq(
          MonitorRouteGroup("name1", "description1"),
          MonitorRouteGroup("name2", "description2")
        )
      )

      repository.deleteGroup("name1")
      repository.all(stale = false) should equal(
        Seq(
          MonitorRouteGroup("name2", "description2")
        )
      )

      repository.deleteGroup("name2")
      repository.all(stale = false) should equal(Seq())

    }
  }
}
