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

class MonitorUpdaterTest12b extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("route update - move to group that does not exist") {

    withDatabase() { database =>

      val config = new MonitorUpdaterConfiguration(database)

      val group1 = newMonitorGroup("group1")

      val route = newMonitorRoute(
        group1._id,
        name = "route",
        relationId = Some(1),
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        referenceType = "osm",
        referenceDay = Some(Day(2022, 8, Some(1))),
        referenceFilename = None,
      )
      val state = newMonitorRouteState(
        routeId = route._id,
        relationId = 1,
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
      )
      val reference = newMonitorRouteReference(
        routeId = route._id,
        relationId = route.relationId,
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        referenceType = "osm",
        referenceDay = Day(2022, 8, 1),
      )

      config.monitorGroupRepository.saveGroup(group1)
      config.monitorRouteRepository.saveRoute(route)
      config.monitorRouteRepository.saveRouteState(state)
      config.monitorRouteRepository.saveRouteReference(reference)

      val properties = MonitorRouteProperties(
        groupName = "group2", // <-- changed, but there is no group2
        name = "route",
        description = "",
        comment = None,
        relationId = Some(1),
        referenceType = "osm",
        referenceDay = Some(Day(2022, 8, Some(11))),
        referenceFilename = None,
        referenceFileChanged = false,
      )

      Time.set(Timestamp(2022, 8, 12, 12, 0, 0))
      val saveResult = config.monitorUpdater.update("user", "group1", "route", properties)

      saveResult should equal(
        MonitorRouteSaveResult(
          exception = Some("""Could not find group with name "group2"""")
        )
      )

      val updatedRoute = config.monitorRouteRepository.routeByName(group1._id, "route").get
      val updatedState = config.monitorRouteRepository.routeState(route._id, 1).get
      val updatedReference = config.monitorRouteRepository.routeRelationReference(route._id, 1).get

      updatedRoute should equal(route)
      updatedState should equal(state)
      updatedReference should equal(reference)
    }
  }
}
