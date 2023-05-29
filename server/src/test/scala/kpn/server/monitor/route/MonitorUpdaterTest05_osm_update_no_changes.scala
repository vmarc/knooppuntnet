package kpn.server.monitor.route

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.custom.Day
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest05_osm_update_no_changes extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("route update - no changes") {

    withDatabase() { database =>

      val configuration = MonitorUpdaterTestSupport.configuration(database)

      val group = newMonitorGroup("group")
      val route = newMonitorRoute(
        group._id,
        name = "route",
        relationId = Some(1),
        user = "user",
        referenceType = "osm",
        referenceDay = Some(Day(2022, 8, Some(11))),
        referenceFilename = None,
      )
      val reference = newMonitorRouteReference(
        routeId = route._id,
        relationId = Some(1),
        referenceType = "osm",
        referenceDay = Day(2022, 8, 11),
      )
      val state = newMonitorRouteState(
        route._id,
        1,
        timestamp = Timestamp(2022, 8, 11),
      )

      configuration.monitorGroupRepository.saveGroup(group)
      configuration.monitorRouteRepository.saveRoute(route)
      configuration.monitorRouteRepository.saveRouteReference(reference)
      configuration.monitorRouteRepository.saveRouteState(state)

      val properties = MonitorRouteProperties(
        groupName = group.name,
        name = "route",
        description = "",
        comment = None,
        relationId = Some(1),
        referenceType = "osm",
        referenceDay = Some(Day(2022, 8, Some(11))),
        referenceFilename = None,
        referenceFileChanged = false,
      )

      Time.set(Timestamp(2023, 1, 1))
      val saveResult = configuration.monitorUpdater.update("user", "group", "route", properties)

      saveResult should equal(MonitorRouteSaveResult())

      val updatedRoute = configuration.monitorRouteRepository.routeByName(group._id, "route").get
      val updatedReference = configuration.monitorRouteRepository.routeRelationReference(route._id, 1).get
      val updatedState = configuration.monitorRouteRepository.routeState(route._id, 1).get

      updatedRoute should equal(route)
      updatedReference should equal(reference)
      updatedState should equal(state)
    }
  }
}
