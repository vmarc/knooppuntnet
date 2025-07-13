package kpn.server.monitor.repository

import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.custom.Timestamp
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newMonitorGroup
import kpn.core.test.TestObjects.newMonitorReference
import kpn.core.test.TestObjects.newMonitorRoute
import kpn.core.test.TestObjects.newMonitorRouteChange
import kpn.core.test.TestObjects.newMonitorRouteRelation
import kpn.core.test.TestObjects.newMonitorState
import kpn.server.monitor.domain.MonitorRouteChange

class MonitorRouteRepositoryTest extends MongoTest {

  test("changes/changesCount") {
    pendingRedesign()

    val routeRepository = new MonitorRouteRepositoryImpl(database)

    val change1 = buildChange("group-1", 101, 1, Timestamp(2020, 8, 11), happy = false)
    val change2 = buildChange("group-1", 101, 2, Timestamp(2020, 8, 12), happy = true)
    val change3 = buildChange("group-1", 102, 3, Timestamp(2020, 8, 13), happy = false)
    val change4 = buildChange("group-2", 103, 4, Timestamp(2020, 8, 14), happy = false)
    val change5 = buildChange("group-2", 103, 5, Timestamp(2020, 8, 15), happy = true)

    routeRepository.saveRouteChange(change1)
    routeRepository.saveRouteChange(change2)
    routeRepository.saveRouteChange(change3)
    routeRepository.saveRouteChange(change4)
    routeRepository.saveRouteChange(change5)

    routeRepository.changesCount(MonitorChangesParameters()) should equal(5)
    routeRepository.changes(MonitorChangesParameters()) should equal(
      Seq(
        change5,
        change4,
        change3,
        change2,
        change1
      )
    )

    routeRepository.changesCount(MonitorChangesParameters(impact = true)) should equal(2)
    assertEqual(
      routeRepository.changes(MonitorChangesParameters(impact = true)),
      Seq(
        change5,
        change2
      )
    )

    routeRepository.groupChangesCount("group-1", MonitorChangesParameters()) should equal(3)
    assertEqual(
      routeRepository.groupChanges("group-1", MonitorChangesParameters()),
      Seq(
        change3,
        change2,
        change1
      )
    )

    routeRepository.groupChangesCount("group-1", MonitorChangesParameters(impact = true)) should equal(1)
    assertEqual(
      routeRepository.groupChanges("group-1", MonitorChangesParameters(impact = true)),
      Seq(
        change2
      )
    )

    pending // use string monitor id instead of long id

    routeRepository.routeChangesCount("101", MonitorChangesParameters()) should equal(2)
    assertEqual(
      routeRepository.routeChanges("101", MonitorChangesParameters()),
      Seq(
        change2,
        change1
      )
    )

    routeRepository.routeChangesCount("101", MonitorChangesParameters(impact = true)) should equal(1)
    assertEqual(
      routeRepository.routeChanges("101", MonitorChangesParameters(impact = true)),
      Seq(
        change2
      )
    )
  }

  test("deleteRoute") {

    val group = newMonitorGroup("group")
    val route = newMonitorRoute(group._id, "route", "description")
    val reference = newMonitorReference(route._id, Some(1))
    val state = newMonitorState(routeId = route._id, relationId = 1)

    database.monitorGroups.save(group)
    database.monitorRoutes.save(route)
    database.monitorReferences.save(reference)
    database.monitorStates.save(state)

    database.monitorRoutes.findByObjectId(route._id) should equal(Some(route))
    database.monitorReferences.findByObjectId(reference._id) should equal(Some(reference))
    database.monitorStates.findByObjectId(state._id) should equal(Some(state))

    val routeRepository = new MonitorRouteRepositoryImpl(database)
    routeRepository.deleteRoute(route._id)

    database.monitorRoutes.findByObjectId(route._id) should equal(None)
    database.monitorReferences.findByObjectId(reference._id) should equal(None)
    database.monitorStates.findByObjectId(state._id) should equal(None)
  }

  test("route with nested sub-relations") {

    val group = newMonitorGroup("group")
    val route = newMonitorRoute(
      group._id,
      "route",
      "description",
      relation = Some(
        newMonitorRouteRelation(
          1,
          "1",
          relations = Seq(
            newMonitorRouteRelation(
              11,
              "11",
              relations = Seq(
                newMonitorRouteRelation(111, "111"),
                newMonitorRouteRelation(112, "112")
              )
            ),
            newMonitorRouteRelation(
              12,
              "12",
              relations = Seq(
                newMonitorRouteRelation(121, "121"),
                newMonitorRouteRelation(122, "122")
              )
            )
          )
        )
      )
    )

    database.monitorGroups.save(group)
    database.monitorRoutes.save(route)

    database.monitorRoutes.findByObjectId(route._id) should equal(Some(route))
  }

  test("superRouteRelationSummary") {

    val group = newMonitorGroup("group")
    val route = newMonitorRoute(
      group._id,
      "route",
      "description",
      relation = Some(
        newMonitorRouteRelation(
          1,
          "1",
          relations = Seq(
            newMonitorRouteRelation(
              11,
              "11",
              relations = Seq(
                newMonitorRouteRelation(111, "111"),
                newMonitorRouteRelation(112, "112")
              )
            ),
            newMonitorRouteRelation(
              12,
              "12",
              relations = Seq(
                newMonitorRouteRelation(121, "121"),
                newMonitorRouteRelation(122, "122")
              )
            )
          )
        )
      )
    )

    val reference1 = newMonitorReference(
      route._id,
      Some(11),
      distance = 100
    )
    val reference2 = newMonitorReference(
      route._id,
      Some(12),
      distance = 200
    )

    database.monitorGroups.save(group)
    database.monitorRoutes.save(route)

    database.monitorReferences.save(reference1)
    database.monitorReferences.save(reference2)

    val routeRepository = new MonitorRouteRepositoryImpl(database)
    val distance = routeRepository.superRouteReferenceSummary(route._id)

    distance should equal(Some(300L))
  }

  test("superRouteStateSummary") {

    val group = newMonitorGroup("group")
    val route = newMonitorRoute(
      group._id,
      "route",
      "description",
      relation = Some(
        newMonitorRouteRelation(
          1,
          "1",
          relations = Seq(
            newMonitorRouteRelation(
              11,
              "11",
              relations = Seq(
                newMonitorRouteRelation(111, "111"),
                newMonitorRouteRelation(112, "112")
              )
            ),
            newMonitorRouteRelation(
              12,
              "12",
              relations = Seq(
                newMonitorRouteRelation(121, "121"),
                newMonitorRouteRelation(122, "122")
              )
            )
          )
        )
      )
    )

    val state1 = newMonitorState(
      routeId = route._id,
      relationId = 11,
      deviations = Seq(
        MonitorRouteDeviation(
          1,
          meters = 20,
          distance = 0,
          bounds = Bounds(),
          lines = Seq.empty
        ),
        MonitorRouteDeviation(
          2,
          meters = 30,
          distance = 0,
          bounds = Bounds(),
          lines = Seq.empty
        )
      )
    )
    val state2 = newMonitorState(
      routeId = route._id,
      relationId = 12,
      deviations = Seq(
        MonitorRouteDeviation(
          1,
          meters = 40,
          distance = 0,
          bounds = Bounds(),
          lines = Seq.empty
        ),
      )
    )

    database.monitorGroups.save(group)
    database.monitorRoutes.save(route)
    database.monitorStates.save(state1)
    database.monitorStates.save(state2)

    val routeRepository = new MonitorRouteRepositoryImpl(database)
    routeRepository.superRouteStateSummary(route._id) match {
      case None => fail("could not retrieve state summary")
      case Some(monitorStateSummary) =>
        monitorStateSummary.deviationDistance should equal(90L)
        monitorStateSummary.deviationCount should equal(3L)
    }
  }

  test("routeReference and routeRelationReferenceId") {

    val group = newMonitorGroup("group")
    val route = newMonitorRoute(
      group._id,
      "route",
      "description",
    )

    val reference1 = newMonitorReference(
      route._id,
      None
    )
    val reference2 = newMonitorReference(
      route._id,
      Some(1)
    )

    database.monitorGroups.save(group)
    database.monitorRoutes.save(route)
    database.monitorReferences.save(reference1)
    database.monitorReferences.save(reference2)

    val routeRepository = new MonitorRouteRepositoryImpl(database)

    assertEqual(
      routeRepository.reference(route._id, None),
      Some(reference1)
    )

    assertEqual(
      routeRepository.reference(route._id, Some(1)),
      Some(reference2)
    )

    assertEqual(
      routeRepository.routeRelationReferenceId(route._id, None),
      Some(reference1._id)
    )

    assertEqual(
      routeRepository.routeRelationReferenceId(route._id, Some(1)),
      Some(reference2._id)
    )
  }

  private def buildChange(groupName: String /*TODO MON remove*/ , routeId: Long, changeSetId: Long, timestamp: Timestamp, happy: Boolean): MonitorRouteChange = {
    newMonitorRouteChange(
      newChangeKey(
        1,
        timestamp,
        changeSetId,
        routeId
      ),
      happy = happy
    )
  }
}
