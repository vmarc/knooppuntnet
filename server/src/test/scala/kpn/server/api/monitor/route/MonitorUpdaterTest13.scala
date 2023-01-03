package kpn.server.api.monitor.route

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.custom.Day
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import org.scalamock.scalatest.MockFactory
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest13 extends UnitTest with BeforeAndAfterEach with SharedTestObjects with MockFactory {

  test("cannot add route that already exists") {

    withDatabase() { database =>

      val config = new MonitorUpdaterConfiguration(database)

      val group = newMonitorGroup("group-name")
      val route = newMonitorRoute(
        group._id,
        name = "route-name",
        relationId = None,
        user = "user",
        referenceType = "osm",
        referenceDay = Some(Day(2022, 8, Some(11))),
        referenceFilename = None,
      )

      config.monitorGroupRepository.saveGroup(group)
      config.monitorRouteRepository.saveRoute(route)

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

      val saveResult = config.monitorUpdater.add("user", "group-name", properties)
      saveResult should equal(
        MonitorRouteSaveResult(
          exception = Some(s"""Could not add route with name "route-name": already exists (_id=${route._id.oid}) in group with name "group-name"""")
        )
      )
    }
  }
}
