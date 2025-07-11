package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Timestamp
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute

class MonitorUpdaterTest15_add_error extends MonitorUpdateTest {

  test("cannot add route that already exists") {

    val (route, reporter) = setup()

    executeMonitorUpdate(reporter)

    verifyDocumentCounts(1, 0, 0)

    verifyReporterMessages(route, reporter)
  }

  private def executeMonitorUpdate(reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.add,
          groupName = "group-name",
          routeName = "route-name",
          referenceType = MonitorReferenceType.osm,
          description = Some("description"),
          comment = Some("comment"),
          relationId = Some(1),
          referenceTimestamp = Some(Timestamp(2022, 8, 11)),
        )
      )
    )
  }

  private def verifyReporterMessages(route: MonitorRoute, reporter: MonitorUpdateReporterMock): Unit = {
    assertEqual(
      reporter.messages,
      Seq(
        message(
          add("prepare"),
          add("analyze-route-structure"),
          active("prepare")
        ),
        MonitorRouteUpdateStatusMessage(
          exception = Some(s"""Could not add route with name "route-name": already exists (_id=${route._id.oid}) in group with name "group-name"""")
        )
      )
    )
  }

  private def setup() = {

    val group = newMonitorGroup("group-name")
    val route = setupRoute(group)

    configuration.monitorGroupRepository.saveGroup(group)
    configuration.monitorRouteRepository.saveRoute(route)

    val reporter = new MonitorUpdateReporterMock()
    (route, reporter)
  }

  private def setupRoute(group: MonitorGroup): MonitorRoute = {
    newMonitorRoute(
      group._id,
      name = "route-name",
      relationId = None,
      user = "user",
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = Some(Timestamp(2022, 8, 11)),
      referenceFilename = None,
    )
  }
}
