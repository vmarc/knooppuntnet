package kpn.server.monitor.repository

import kpn.api.common.Bounds
import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.custom.Timestamp
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.monitor.domain.MonitorRouteChange

class MonitorRouteRepositoryTest extends UnitTest with SharedTestObjects {

  test("changes/changesCount") {

    withDatabase { database =>

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
      routeRepository.changes(MonitorChangesParameters(impact = true)).shouldMatchTo(
        Seq(
          change5,
          change2
        )
      )

      routeRepository.groupChangesCount("group-1", MonitorChangesParameters()) should equal(3)
      routeRepository.groupChanges("group-1", MonitorChangesParameters()).shouldMatchTo(
        Seq(
          change3,
          change2,
          change1
        )
      )

      routeRepository.groupChangesCount("group-1", MonitorChangesParameters(impact = true)) should equal(1)
      routeRepository.groupChanges("group-1", MonitorChangesParameters(impact = true)).shouldMatchTo(
        Seq(
          change2
        )
      )

      pending // use string monitor id instead of long id

      routeRepository.routeChangesCount("101", MonitorChangesParameters()) should equal(2)
      routeRepository.routeChanges("101", MonitorChangesParameters()).shouldMatchTo(
        Seq(
          change2,
          change1
        )
      )

      routeRepository.routeChangesCount("101", MonitorChangesParameters(impact = true)) should equal(1)
      routeRepository.routeChanges("101", MonitorChangesParameters(impact = true)).shouldMatchTo(
        Seq(
          change2
        )
      )
    }
  }

  test("deleteRoute") {

    withDatabase { database =>

      val group = newMonitorGroup("group")
      val route = newMonitorRoute(group._id, "route", "description")
      val reference = newMonitorRouteReference(route._id, 0)
      val state = newMonitorRouteState(route._id, 1)

      database.monitorGroups.save(group)
      database.monitorRoutes.save(route)
      database.monitorRouteReferences.save(reference)
      database.monitorRouteStates.save(state)

      database.monitorRoutes.findByObjectId(route._id) should equal(Some(route))
      database.monitorRouteReferences.findByObjectId(reference._id) should equal(Some(reference))
      database.monitorRouteStates.findByObjectId(state._id) should equal(Some(state))

      val routeRepository = new MonitorRouteRepositoryImpl(database)
      routeRepository.deleteRoute(route._id)

      database.monitorRoutes.findByObjectId(route._id) should equal(None)
      database.monitorRouteReferences.findByObjectId(reference._id) should equal(None)
      database.monitorRouteStates.findByObjectId(state._id) should equal(None)
    }
  }

  test("route with nested sub-relations") {

    withDatabase { database =>

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
  }

  test("superRouteRelationSummary") {

    withDatabase { database =>

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

      val reference1 = newMonitorRouteReference(
        route._id,
        11,
        distance = 100
      )
      val reference2 = newMonitorRouteReference(
        route._id,
        12,
        distance = 200
      )

      database.monitorGroups.save(group)
      database.monitorRoutes.save(route)

      database.monitorRouteReferences.save(reference1)
      database.monitorRouteReferences.save(reference2)

      val routeRepository = new MonitorRouteRepositoryImpl(database)
      val distance = routeRepository.superRouteReferenceSummary(route._id)

      distance should equal(Some(300L))
    }
  }

  test("superRouteStateSummary") {

    withDatabase { database =>

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

      val state1 = newMonitorRouteState(
        route._id,
        relationId = 11,
        wayCount = 10,
        osmDistance = 100,
        deviations = Seq(
          MonitorRouteDeviation(
            1,
            meters = 20,
            distance = 0,
            bounds = Bounds(),
            geoJson = ""
          ),
          MonitorRouteDeviation(
            2,
            meters = 30,
            distance = 0,
            bounds = Bounds(),
            geoJson = ""
          )
        )
      )
      val state2 = newMonitorRouteState(
        route._id,
        relationId = 12,
        wayCount = 20,
        osmDistance = 200,
        deviations = Seq(
          MonitorRouteDeviation(
            1,
            meters = 40,
            distance = 0,
            bounds = Bounds(),
            geoJson = ""
          ),
        )
      )

      database.monitorGroups.save(group)
      database.monitorRoutes.save(route)
      database.monitorRouteStates.save(state1)
      database.monitorRouteStates.save(state2)

      val routeRepository = new MonitorRouteRepositoryImpl(database)
      routeRepository.superRouteStateSummary(route._id) match {
        case None => fail("could not retrieve state summary")
        case Some(monitorRouteStateSummary) =>
          monitorRouteStateSummary.deviationDistance should equal(90L)
          monitorRouteStateSummary.deviationCount should equal(3L)
          monitorRouteStateSummary.osmWayCount should equal(30L)
          monitorRouteStateSummary.osmDistance should equal(300L)
      }
    }
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
