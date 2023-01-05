package kpn.server.api.monitor.route

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.custom.Day
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MonitorUpdaterTest15 extends UnitTest with SharedTestObjects {

  test("update/upload - route not found") {

    withDatabase() { database =>

      val configuration = MonitorUpdaterTestSupport.configuration(database)

      val properties = MonitorRouteProperties(
        groupName = "group-name",
        name = "route-name",
        description = "description",
        comment = Some("comment"),
        relationId = Some(1),
        referenceType = "osm",
        referenceDay = Some(Day(2022, 8, Some(11))),
        referenceFileChanged = false,
        referenceFilename = None
      )

      val group = newMonitorGroup("group-name")
      configuration.monitorGroupRepository.saveGroup(group)

      val saveResult1 = configuration.monitorUpdater.update("user", "group-name", "unknown-route-name", properties)
      saveResult1 should equal(
        MonitorRouteSaveResult(
          exception = Some("""Could not find route with name "unknown-route-name" in group "group-name"""")
        )
      )

      val saveResult2 = configuration.monitorUpdater.upload("user", "group-name", "unknown-route-name", 1, Day(2022, 8, 11), "filename", null)
      saveResult2 should equal(
        MonitorRouteSaveResult(
          exception = Some("""Could not find route with name "unknown-route-name" in group "group-name"""")
        )
      )
    }
  }
}
