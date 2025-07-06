package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Timestamp

class MonitorUpdaterTest17_route_not_found extends MonitorUpdateTest {

  test("update/upload - route not found") {

    val (reporter) = setup()

    executeMonitorUpdate(reporter)

    verifyReporterMessages(reporter)
  }

  private def executeMonitorUpdate(reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.update,
          groupName = "group-name",
          routeName = "unknown-route-name",
          description = Some("description"),
          comment = Some("comment"),
          relationId = Some(1),
          referenceType = MonitorReferenceType.osm,
          referenceTimestamp = Some(Timestamp(2022, 8, 11)),
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
          add("analyze-route-structure"),
          active("prepare")
        ),
        MonitorRouteUpdateStatusMessage(
          exception = Some("""Could not find route with name "unknown-route-name" in group "group-name"""")
        )
      )
    )
  }

  private def setup() = {

    val group = newMonitorGroup("group-name")
    configuration.monitorGroupRepository.saveGroup(group)

    val reporter = new MonitorUpdateReporterMock()
    (reporter)
  }
}
