package kpn.server.api.monitor.route

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.custom.Day
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest14 extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  test("add/update/upload - group not found") {

    withDatabase() { database =>

      val config = new MonitorUpdaterConfiguration(database)

      val properties = MonitorRouteProperties(
        groupName = "unknown-group",
        name = "route-name",
        description = "description",
        comment = Some("comment"),
        relationId = Some(1L),
        referenceType = "osm",
        referenceDay = Some(Day(2022, 8, Some(11))),
        referenceFileChanged = false,
        referenceFilename = None
      )

      val saveResult1 = config.monitorUpdater.add("user", "unknown-group", properties)
      saveResult1 should equal(
        MonitorRouteSaveResult(
          exception = Some("""Could not find group with name "unknown-group"""")
        )
      )

      val saveResult2 = config.monitorUpdater.update("user", "unknown-group", "route-name", properties)
      saveResult2 should equal(
        MonitorRouteSaveResult(
          exception = Some("""Could not find group with name "unknown-group"""")
        )
      )

      val saveResult3 = config.monitorUpdater.upload(
        "user",
        "unknown-group",
        "route-name",
        1L,
        Day(2022, 8, 11),
        "filename",
        null
      )
      saveResult3 should equal(
        MonitorRouteSaveResult(
          exception = Some("""Could not find group with name "unknown-group""""
          )
        )
      )
    }
  }
}
