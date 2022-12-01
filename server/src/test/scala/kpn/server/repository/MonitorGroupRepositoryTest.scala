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

      val route1 = newMonitorRoute(group._id, "route1", "route one", None, Some(1L))
      val route2 = newMonitorRoute(group._id, "route2", "route two", None, Some(2L))
      val route3 = newMonitorRoute(group._id, "route3", "route three", None, Some(3L))

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

  test("deleteGroup") {

    withDatabase { database =>

      val group = newMonitorGroup("group", "")
      val route1 = newMonitorRoute(group._id, "route1", "")
      val route2 = newMonitorRoute(group._id, "route2", "")
      val reference1 = newMonitorRouteReference(route1._id)
      val reference2 = newMonitorRouteReference(route2._id)
      val state1 = newMonitorRouteState(route1._id)
      val state2 = newMonitorRouteState(route2._id)

      database.monitorGroups.save(group)
      database.monitorRoutes.save(route1)
      database.monitorRoutes.save(route2)
      database.monitorRouteReferences.save(reference1)
      database.monitorRouteReferences.save(reference2)
      database.monitorRouteStates.save(state1)
      database.monitorRouteStates.save(state2)

      database.monitorGroups.findByObjectId(group._id) should equal(Some(group))

      database.monitorRoutes.findByObjectId(route1._id) should equal(Some(route1))
      database.monitorRoutes.findByObjectId(route2._id) should equal(Some(route2))

      database.monitorRouteReferences.findByObjectId(reference1._id) should equal(Some(reference1))
      database.monitorRouteReferences.findByObjectId(reference2._id) should equal(Some(reference2))

      database.monitorRouteStates.findByObjectId(state1._id) should equal(Some(state1))
      database.monitorRouteStates.findByObjectId(state2._id) should equal(Some(state2))

      val groupRepository = new MonitorGroupRepositoryImpl(database)
      groupRepository.deleteGroup(group._id)

      database.monitorGroups.findByObjectId(group._id) should equal(None)
      database.monitorRoutes.findByObjectId(route1._id) should equal(None)
      database.monitorRoutes.findByObjectId(route2._id) should equal(None)
      database.monitorRouteReferences.findByObjectId(reference1._id) should equal(None)
      database.monitorRouteReferences.findByObjectId(reference2._id) should equal(None)
      database.monitorRouteStates.findByObjectId(state1._id) should equal(None)
      database.monitorRouteStates.findByObjectId(state2._id) should equal(None)
    }
  }
}
