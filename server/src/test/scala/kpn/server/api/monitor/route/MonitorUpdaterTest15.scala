package kpn.server.api.monitor.route

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.custom.Day
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import org.scalamock.scalatest.MockFactory
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest15 extends UnitTest with BeforeAndAfterEach with SharedTestObjects with MockFactory {

  override def beforeEach(): Unit = {
    Time.set(Timestamp(2023, 1, 1))
  }

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("update/upload - route not found") {

    withDatabase() { database =>

      val config = new MonitorUpdaterConfiguration(database)

      val properties = MonitorRouteProperties(
        groupName = "group-name",
        name = "route-name",
        description = "description",
        comment = Some("comment"),
        relationId = Some(1L),
        referenceType = "osm",
        referenceDay = Some(Day(2022, 8, Some(11))),
        referenceFileChanged = false,
        referenceFilename = None
      )

      val group = newMonitorGroup("group-name")
      config.monitorGroupRepository.saveGroup(group)

      val saveResult1 = config.monitorUpdater.update("user", "group-name", "unknown-route-name", properties)
      saveResult1 should equal(
        MonitorRouteSaveResult(
          exception = Some("""Could not find route with name "unknown-route-name" in group "group-name"""")
        )
      )

      val saveResult2 = config.monitorUpdater.upload("user", "group-name", "unknown-route-name", 1L, Day(2022, 8, 11), "filename", null)
      saveResult2 should equal(
        MonitorRouteSaveResult(
          exception = Some("""Could not find route with name "unknown-route-name" in group "group-name"""")
        )
      )
    }
  }
}
