package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.time.Time
import kpn.core.test.TestObjects.newMonitorGroup
import kpn.core.test.TestObjects.newMonitorReference
import kpn.core.test.TestObjects.newMonitorRoute
import kpn.core.test.TestObjects.newMonitorState
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorState

class MonitorUpdaterTest06_osm_update_no_changes extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
  }

  test("route update - no changes") {

    val (group, route, reference, state, reporter) = setup()

    executeMonitorUpdate(group, reporter)

    verifyDocumentCounts(1, 1, 1)
    verifyRouteNotChanged(group, route)
    verifyReferenceNotChanged(route, reference)
    verifyStateNotChanged(route, state)
    verifyReporterMessages(reporter)
  }

  private def executeMonitorUpdate(group: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.update,
          groupName = group.name,
          routeName = "route",
          description = Some(""),
          relationId = Some(route1.relationId),
          referenceType = MonitorReferenceType.osm,
          referenceTimestamp = Some(ReferenceTimestamp1),
        )
      )
    )
  }

  private def verifyRouteNotChanged(group: MonitorGroup, route: MonitorRoute): Unit = {
    val updatedRoute = configuration.monitorRouteRepository.routeByName(group._id, "route").get
    assertEqual(
      route,
      updatedRoute
    )
  }

  private def verifyReferenceNotChanged(route: MonitorRoute, reference: MonitorReference): Unit = {
    val updatedReference = configuration.monitorRouteRepository.reference(route._id, Some(route1.relationId)).get
    updatedReference should equal(reference)
  }

  private def verifyStateNotChanged(route: MonitorRoute, state: MonitorState): Unit = {
    val updatedState = configuration.monitorRouteRepository.state(route._id, route1.relationId).get
    updatedState should equal(state)
  }

  private def verifyReporterMessages(reporter: MonitorUpdateReporterMock): Unit = {
    assertEqual(
      reporter.messages,
      Seq(
        message(
          add("prepare"),
          active("prepare")
        ),
        message(
          done("prepare"))
      )
    )
  }

  private def setup(): (
    MonitorGroup,
      MonitorRoute,
      MonitorReference,
      MonitorState,
      MonitorUpdateReporterMock
    ) = {

    Time.set(CurrentTimestamp)

    val group = newMonitorGroup("group")
    val route = setupRoute(group)
    val reference = setupReference(route)
    val state = setupState(route)

    configuration.monitorGroupRepository.saveGroup(group)
    configuration.monitorRouteRepository.saveRoute(route)
    configuration.monitorRouteRepository.saveReference(reference)
    configuration.monitorRouteRepository.saveState(state)

    Time.set(UpdateTimestamp)
    val reporter = new MonitorUpdateReporterMock()
    (group, route, reference, state, reporter)
  }

  private def setupRoute(group: MonitorGroup): MonitorRoute = {
    newMonitorRoute(
      group._id,
      name = "route",
      relationId = Some(route1.relationId),
      user = "user",
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = Some(ReferenceTimestamp1),
      referenceFilename = None,
    )
  }

  private def setupReference(route: MonitorRoute): MonitorReference = {
    newMonitorReference(
      routeId = route._id,
      relationId = Some(route1.relationId),
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = ReferenceTimestamp1
    )
  }

  private def setupState(route: MonitorRoute): MonitorState = {
    newMonitorState(
      routeId = route._id,
      relationId = route1.relationId,
      timestamp = CurrentTimestamp,
    )
  }
}
