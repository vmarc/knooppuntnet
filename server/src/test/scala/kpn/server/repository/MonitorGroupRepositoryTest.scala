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

      repository.groupByName("name1") should equal(Some(group1))
      repository.groupByName("name2") should equal(Some(group2))

      repository.groups().shouldMatchTo(
        Seq(
          group1,
          group2
        )
      )

      repository.deleteGroup(group1._id)
      repository.groups().shouldMatchTo(
        Seq(
          group2
        )
      )

      repository.deleteGroup(group2._id)
      repository.groups() shouldBe empty
    }
  }

  test("groupRoutes") {

    withDatabase { database =>

      val groupRepository = new MonitorGroupRepositoryImpl(database)

      val group = newMonitorGroup("group-name", "group description")
      groupRepository.saveGroup(group)

      val route1 = newMonitorRoute(group._id, "route1", "route one", Some(1L))
      val route2 = newMonitorRoute(group._id, "route2", "route two", Some(2L))
      val route3 = newMonitorRoute(group._id, "route3", "route three", Some(3L))

      database.monitorRoutes.save(route1)
      database.monitorRoutes.save(route2)
      database.monitorRoutes.save(route3)

      groupRepository.groupRoutes(group._id).shouldMatchTo(
        Seq(
          route1,
          route2,
          route3
        )
      )
    }
  }
}
