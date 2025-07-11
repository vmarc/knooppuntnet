package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.core.common.Time
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute

class MonitorUpdaterTest14_update_group_error extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
  }

  test("route update - move to group that does not exist") {

    val (group1, route, state, reference, reporter) = setup()

    executeUpdate(reporter)

    verifyDocumentCounts(1, 1, 1)

    val updatedRoute = configuration.monitorRouteRepository.routeByName(group1._id, "route").get
    val updatedState = configuration.monitorRouteRepository.routeState(route._id, 1).get
    val updatedReference = configuration.monitorRouteRepository.routeReference(route._id, Some(1)).get

    updatedRoute should equal(route)
    updatedState should equal(state)
    updatedReference should equal(reference)

    verifyReporterMessages(reporter)
  }

  private def executeUpdate(reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.update,
          groupName = "group1",
          newGroupName = Some("group2"), // <-- changed, but there is no group2
          routeName = "route",
          referenceType = MonitorReferenceType.osm,
          description = Some(""),
          comment = None,
          relationId = Some(route1.relationId),
          referenceTimestamp = Some(ReferenceTimestamp1),
        )
      )
    )
  }

  private def setup() = {

    val group = newMonitorGroup("group1")
    val route = setupRoute(group)
    val reference = setupReference(route)
    val state = setupState(route)

    configuration.monitorGroupRepository.saveGroup(group)
    configuration.monitorRouteRepository.saveRoute(route)
    configuration.monitorRouteRepository.saveRouteReference(reference)
    configuration.monitorRouteRepository.saveRouteState(state)

    Time.set(UpdateTimestamp)
    val reporter = new MonitorUpdateReporterMock()
    (group, route, state, reference, reporter)
  }

  private def setupRoute(group: MonitorGroup) = {
    newMonitorRoute(
      group._id,
      name = "route",
      relationId = Some(route1.relationId),
      timestamp = CurrentTimestamp,
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = Some(ReferenceTimestamp1),
      referenceFilename = None,
    )
  }

  private def setupReference(route: MonitorRoute) = {
    newMonitorRouteReference(
      routeId = route._id,
      relationId = Some(route1.relationId),
      timestamp = CurrentTimestamp,
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = ReferenceTimestamp1,
    )
  }

  private def setupState(route: MonitorRoute) = {
    newMonitorRouteState(
      routeId = route._id,
      relationId = route1.relationId,
      timestamp = CurrentTimestamp,
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
          exception = Some("""Could not find group with name "group2"""")
        )
      )
    )
  }
}
