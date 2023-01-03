package kpn.server.api.monitor.route

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.custom.Day
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest11 extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  override def beforeEach(): Unit = {
    Time.set(Timestamp(2023, 2, 1))
  }

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("update name, description and comment (state and reference unchanged, no analysis)") {

    withDatabase() { database =>
      val config = new MonitorUpdaterConfiguration(database)
      val group = newMonitorGroup("group")
      val route = newMonitorRoute(
        group._id,
        name = "route-name",
        description = "description",
        comment = Some("comment"),
        relationId = Some(1L),
        referenceType = "osm",
        referenceDay = Some(Day(2022, 8, Some(11))),
        referenceFilename = None
      )
      val state = newMonitorRouteState(
        route._id,
        relationId = 1L,
        timestamp = Timestamp(2023, 1, 1),
      )

      val reference = newMonitorRouteReference(
        route._id,
        relationId = Some(1L),
        created = Timestamp(2023, 1, 1),
      )

      config.monitorGroupRepository.saveGroup(group)
      config.monitorRouteRepository.saveRoute(route)
      config.monitorRouteRepository.saveRouteState(state)
      config.monitorRouteRepository.saveRouteReference(reference)

      val properties = MonitorRouteProperties(
        groupName = group.name,
        name = "route-name-changed", // <-- changed
        description = "description-changed", // <-- changed
        comment = Some("comment-changed"), // <-- changed
        relationId = Some(1L),
        referenceType = "osm",
        referenceDay = Some(Day(2022, 8, Some(11))),
        referenceFileChanged = false,
        referenceFilename = None
      )

      val saveResult = config.monitorUpdater.update("user", group.name, route.name, properties)
      saveResult should equal(MonitorRouteSaveResult()) // not analyzed, no errors

      val updatedRoute = config.monitorRouteRepository.routeByName(group._id, "route-name-changed").get
      val updatedState = config.monitorRouteRepository.routeState(route._id, 1L).get
      val updatedReference = config.monitorRouteRepository.routeRelationReference(route._id, 1L).get

      updatedRoute.name should equal("route-name-changed")
      updatedRoute.description should equal("description-changed")
      updatedRoute.comment should equal(Some("comment-changed"))

      updatedState should equal(state) // no change
      updatedReference should equal(reference) // no change
    }
  }
}
