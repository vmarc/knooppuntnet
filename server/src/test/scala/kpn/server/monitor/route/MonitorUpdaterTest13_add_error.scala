package kpn.server.monitor.route

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.custom.Timestamp
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest13_add_error extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  test("cannot add route that already exists") {

    withDatabase() { database =>

      val configuration = MonitorUpdaterTestSupport.configuration(database)

      val group = newMonitorGroup("group-name")
      val route = newMonitorRoute(
        group._id,
        name = "route-name",
        relationId = None,
        user = "user",
        referenceType = "osm",
        referenceTimestamp = Some(Timestamp(2022, 8, 11)),
        referenceFilename = None,
      )

      configuration.monitorGroupRepository.saveGroup(group)
      configuration.monitorRouteRepository.saveRoute(route)

      val properties = MonitorRouteProperties(
        groupName = "group-name",
        name = "route-name",
        description = "description",
        comment = Some("comment"),
        relationId = Some(1),
        referenceType = "osm",
        referenceTimestamp = Some(Timestamp(2022, 8, 11)),
        referenceFileChanged = false,
        referenceFilename = None
      )

      val saveResult = configuration.monitorUpdater.add("user", "group-name", properties)
      saveResult should equal(
        MonitorRouteSaveResult(
          exception = Some(s"""Could not add route with name "route-name": already exists (_id=${route._id.oid}) in group with name "group-name"""")
        )
      )
    }
  }
}
