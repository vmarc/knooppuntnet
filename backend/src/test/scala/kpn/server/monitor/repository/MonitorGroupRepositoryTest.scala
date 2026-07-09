package kpn.server.monitor.repository

import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newMonitorGroup
import kpn.core.test.TestObjects.newMonitorReference
import kpn.core.test.TestObjects.newMonitorRoute
import kpn.core.test.TestObjects.newMonitorState

class MonitorGroupRepositoryTest extends MongoTest {

  test("all/add/delete") {

    val repository = new MonitorGroupRepository(database)

    repository.groups() shouldBe empty
    repository.groupByName("name1") should equal(None)

    val group1 = newMonitorGroup("name1", "description1")
    val group2 = newMonitorGroup("name2", "description2")

    repository.saveGroup(group1)
    repository.saveGroup(group2)

    repository.groupByName("name1") should equal(Some(group1))
    repository.groupByName("name2") should equal(Some(group2))

    assertEqual(
      repository.groups(),
      Seq(
        group1,
        group2
      )
    )

    repository.deleteGroup(group1._id)
    assertEqual(
      repository.groups(),
      Seq(
        group2
      )
    )

    repository.deleteGroup(group2._id)
    repository.groups() shouldBe empty
  }

  test("groupRoutes") {

    val groupRepository = new MonitorGroupRepository(database)

    val group = newMonitorGroup("group-name", "group description")
    groupRepository.saveGroup(group)

    val route1 = newMonitorRoute(group._id, "route1", "route one", None, Some(1))
    val route2 = newMonitorRoute(group._id, "route2", "route two", None, Some(2))
    val route3 = newMonitorRoute(group._id, "route3", "route three", None, Some(3))

    database.monitorRoutes.save(route1)
    database.monitorRoutes.save(route2)
    database.monitorRoutes.save(route3)

    assertEqual(
      groupRepository.groupRoutes(group._id),
      Seq(
        route1,
        route2,
        route3
      )
    )
  }

  test("deleteGroup") {

    val group = newMonitorGroup("group")
    val route1 = newMonitorRoute(group._id, "route1")
    val route2 = newMonitorRoute(group._id, "route2")
    val reference1 = newMonitorReference(route1._id, Some(1))
    val reference2 = newMonitorReference(route2._id, Some(2))
    val state1 = newMonitorState(routeId = route1._id, relationId = 1)
    val state2 = newMonitorState(routeId = route2._id, relationId = 2)

    database.monitorGroups.save(group)
    database.monitorRoutes.save(route1)
    database.monitorRoutes.save(route2)
    database.monitorReferences.save(reference1)
    database.monitorReferences.save(reference2)
    database.monitorStates.save(state1)
    database.monitorStates.save(state2)

    database.monitorGroups.findByObjectId(group._id) should equal(Some(group))

    database.monitorRoutes.findByObjectId(route1._id) should equal(Some(route1))
    database.monitorRoutes.findByObjectId(route2._id) should equal(Some(route2))

    database.monitorReferences.findByObjectId(reference1._id) should equal(Some(reference1))
    database.monitorReferences.findByObjectId(reference2._id) should equal(Some(reference2))

    database.monitorStates.findByObjectId(state1._id) should equal(Some(state1))
    database.monitorStates.findByObjectId(state2._id) should equal(Some(state2))

    val groupRepository = new MonitorGroupRepository(database)
    groupRepository.deleteGroup(group._id)

    database.monitorGroups.findByObjectId(group._id) should equal(None)
    database.monitorRoutes.findByObjectId(route1._id) should equal(None)
    database.monitorRoutes.findByObjectId(route2._id) should equal(None)
    database.monitorReferences.findByObjectId(reference1._id) should equal(None)
    database.monitorReferences.findByObjectId(reference2._id) should equal(None)
    database.monitorStates.findByObjectId(state1._id) should equal(None)
    database.monitorStates.findByObjectId(state2._id) should equal(None)
  }
}
