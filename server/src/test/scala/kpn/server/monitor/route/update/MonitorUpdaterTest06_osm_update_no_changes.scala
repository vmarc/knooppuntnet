package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState

class MonitorUpdaterTest06_osm_update_no_changes extends MonitorUpdateTest {

  test("route update - no changes") {

    val (group, route, reference, state, reporter) = setup()

    executeMonitorUpdate(group, reporter)

    verifyDocumentCounts()
    verifyRouteNotChanged(group, route)
    verifyReferenceNotChanged(route, reference)
    verifyStateNotChanged(route, state)
    verifyReporterMessages(reporter)
  }

  private def executeMonitorUpdate(group: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.update,
          groupName = group.name,
          routeName = "route",
          description = Some(""),
          relationId = Some(1),
          referenceType = MonitorReferenceType.osm,
          referenceTimestamp = Some(Timestamp(2022, 8, 11)),
        )
      )
    )
  }

  private def verifyDocumentCounts(): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(1)
    database.monitorRouteStates.countDocuments() should equal(1)
  }

  private def verifyRouteNotChanged(group: MonitorGroup, route: MonitorRoute): Unit = {
    val updatedRoute = configuration.monitorRouteRepository.routeByName(group._id, "route").get
    updatedRoute.copy(analysisTimestamp = None, analysisDuration = None) should equal(route)
  }

  private def verifyReferenceNotChanged(route: MonitorRoute, reference: MonitorRouteReference): Unit = {
    val updatedReference = configuration.monitorRouteRepository.routeReference(route._id, Some(1)).get
    updatedReference should equal(reference)
  }

  private def verifyStateNotChanged(route: MonitorRoute, state: MonitorRouteState): Unit = {
    val updatedState = configuration.monitorRouteRepository.routeState(route._id, 1).get
    updatedState should equal(state)
  }

  private def verifyReporterMessages(reporter: MonitorUpdateReporterMock): Unit = {
    assertEqual(
      reporter.messages,
      Seq(
        message(
          command("step-add", "prepare"),
          command("step-add", "analyze-route-structure"),
          command("step-active", "prepare")
        ),
        message(
          command("step-active", "analyze-route-structure")
        ),
        message(
          command("step-active", "save")
        ),
        message(
          command("step-done", "save"))
      )
    )
  }

  private def setup() = {

    Time.set(Timestamp(2023, 1, 1))

    val group = newMonitorGroup("group")
    val route = setupRoute(group)
    val reference = setupReference(route)
    val state = setupState(route)

    configuration.monitorGroupRepository.saveGroup(group)
    configuration.monitorRouteRepository.saveRoute(route)
    configuration.monitorRouteRepository.saveRouteReference(reference)
    configuration.monitorRouteRepository.saveRouteState(state)

    Time.set(Timestamp(2023, 1, 2))
    val reporter = new MonitorUpdateReporterMock()
    (group, route, reference, state, reporter)
  }

  private def setupRoute(group: MonitorGroup): MonitorRoute = {
    newMonitorRoute(
      group._id,
      name = "route",
      relationId = Some(1),
      user = "user",
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = Some(Timestamp(2022, 8, 11)),
      referenceFilename = None,
    )
  }

  private def setupReference(route: MonitorRoute): MonitorRouteReference = {
    newMonitorRouteReference(
      routeId = route._id,
      relationId = Some(1),
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = Timestamp(2022, 8, 11),
    )
  }

  private def setupState(route: MonitorRoute): MonitorRouteState = {
    newMonitorRouteState(
      route._id,
      1,
      timestamp = Timestamp(2022, 8, 11),
    )
  }
}
