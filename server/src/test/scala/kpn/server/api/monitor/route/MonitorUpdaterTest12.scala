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

class MonitorUpdaterTest12 extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("route update - change group") {

    withDatabase() { database =>

      val configuration = MonitorUpdaterTestSupport.configuration(database)

      val group1 = newMonitorGroup("group1")
      val group2 = newMonitorGroup("group2")
      val route = newMonitorRoute(
        group1._id,
        name = "route",
        relationId = Some(1),
        referenceType = "osm",
        referenceDay = Some(Day(2022, 8, Some(11))),
        referenceFilename = None,
      )
      val reference = newMonitorRouteReference(
        routeId = route._id,
        relationId = 1,
        referenceType = "osm",
        referenceDay = Day(2022, 8, 11),
      )

      configuration.monitorGroupRepository.saveGroup(group1)
      configuration.monitorGroupRepository.saveGroup(group2)
      configuration.monitorRouteRepository.saveRoute(route)
      configuration.monitorRouteRepository.saveRouteReference(reference)

      val properties = MonitorRouteProperties(
        groupName = group2.name, // <-- changed
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
      val saveResult = configuration.monitorUpdater.update("user", "group1", "route", properties)

      saveResult should equal(MonitorRouteSaveResult())

      val updatedRoute = configuration.monitorRouteRepository.routeByName(group2._id, "route").get
      val updatedReference = configuration.monitorRouteRepository.routeRelationReference(route._id, 1).get
      // TODO val updatedState = config.monitorRouteRepository.routeState(route._id, 1).get

      updatedRoute.groupId should equal(group2._id)
      updatedReference should equal(reference)
      // TODO assert state not updated
    }
  }
}
