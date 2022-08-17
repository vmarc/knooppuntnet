package kpn.server.repository

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MonitorGroupRepositoryTest extends UnitTest with SharedTestObjects {

  test("all/add/delete") {

    withDatabase { database =>

      val repository = new MonitorGroupRepositoryImpl(database)

      repository.groups() shouldBe empty
      repository.groupByName("name1") should equal(None)

      val group1 = newMonitorGroup("name1", "description1")
      val group2 = newMonitorGroup("name2", "description2")

      repository.saveGroup(group1)
      repository.saveGroup(group2)

      repository.groupByName("name1") should equal(group1)
      repository.groupByName("name2") should equal(group2)

      repository.groups().shouldMatchTo(
        Seq(
          group1,
          group2
        )
      )

      repository.deleteGroup("name1")
      repository.groups().shouldMatchTo(
        Seq(
          group2
        )
      )

      repository.deleteGroup("name2")
      repository.groups() shouldBe empty
    }
  }

  test("groupRoutes") {

    withDatabase { database =>

      val groupRepository = new MonitorGroupRepositoryImpl(database)

      val group = newMonitorGroup("group-name", "group description")
      groupRepository.saveGroup(newMonitorGroup("group-name", "group one"))

      val route1 = newMonitorRoute(group._id, "route1", "route one", 1L)
      val route2 = newMonitorRoute(group._id, "route2", "route two", 2L)
      val route3 = newMonitorRoute(group._id, "route3", "route three", 3L)

      database.monitorRoutes.save(route1)
      database.monitorRoutes.save(route2)
      database.monitorRoutes.save(route3)

      groupRepository.groupRoutes("group-name").shouldMatchTo(
        Seq(
          route1,
          route2,
          route3
        )
      )
    }
  }
}
