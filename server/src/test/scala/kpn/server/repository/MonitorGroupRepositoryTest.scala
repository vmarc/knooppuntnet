package kpn.server.repository

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorGroup
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.api.monitor.domain.MonitorRoute

class MonitorGroupRepositoryTest extends UnitTest with SharedTestObjects {

  test("all/add/delete") {

    withDatabase { database =>

      val repository = new MonitorGroupRepositoryImpl(database)

      repository.groups() shouldBe empty
      repository.group("name1") should equal(None)

      repository.saveGroup(MonitorGroup("name1", "description1"))
      repository.saveGroup(MonitorGroup("name2", "description2"))
      repository.group("name1") should equal(Some(MonitorGroup("name1", "description1")))
      repository.group("name2") should equal(Some(MonitorGroup("name2", "description2")))
      repository.groups().shouldMatchTo(
        Seq(
          MonitorGroup("name1", "description1"),
          MonitorGroup("name2", "description2")
        )
      )

      repository.deleteGroup("name1")
      repository.groups().shouldMatchTo(
        Seq(
          MonitorGroup("name2", "description2")
        )
      )

      repository.deleteGroup("name2")
      repository.groups() shouldBe empty
    }
  }

  test("groupRoutes") {

    withDatabase { database =>

      val repository = new MonitorGroupRepositoryImpl(database)

      repository.saveGroup(MonitorGroup("group-name", "group one"))
      database.monitorRoutes.save(newMonitorRoute("group-name", "route1", "route one", 1L))
      database.monitorRoutes.save(newMonitorRoute("group-name", "route2", "route two", 2L))
      database.monitorRoutes.save(newMonitorRoute("group-name", "route3", "route three", 3L))

      repository.groupRoutes("group-name").shouldMatchTo(
        Seq(
          MonitorRoute("group-name:route1", "group-name", "route1", "route one", 1L),
          MonitorRoute("group-name:route2", "group-name", "route2", "route two", 2L),
          MonitorRoute("group-name:route3", "group-name", "route3", "route three", 3L)
        )
      )
    }
  }
}
