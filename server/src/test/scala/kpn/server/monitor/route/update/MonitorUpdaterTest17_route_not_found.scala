package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage

class MonitorUpdaterTest17_route_not_found extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
  }

  test("update/upload - route not found") {

    val reporter = setup()

    executeUpdate(reporter)

    verifyReporterMessages(reporter)
  }

  private def executeUpdate(reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.update,
          groupName = "group-name",
          routeName = "unknown-route-name",
          description = Some("description"),
          comment = Some("comment"),
          relationId = Some(route1.relationId),
          referenceType = MonitorReferenceType.osm,
          referenceTimestamp = Some(ReferenceTimestamp1),
        )
      )
    )
  }

  private def verifyReporterMessages(reporter: MonitorUpdateReporterMock): Unit = {
    assertEqual(
      reporter.messages,
      Seq(
        message(
          add("prepare"),
          active("prepare")
        ),
        MonitorRouteUpdateStatusMessage(
          exception = Some("""Could not find route with name "unknown-route-name" in group "group-name"""")
        )
      )
    )
  }

  private def setup(): MonitorUpdateReporterMock = {

    val group = newMonitorGroup("group-name")
    configuration.monitorGroupRepository.saveGroup(group)

    new MonitorUpdateReporterMock()
  }
}
