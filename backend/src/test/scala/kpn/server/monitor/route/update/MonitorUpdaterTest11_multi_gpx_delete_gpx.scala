package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.custom.Timestamp
import kpn.api.time.Time
import kpn.core.test.TestObjects.newMonitorGroup
import kpn.core.test.TestObjects.newMonitorReference
import kpn.core.test.TestObjects.newMonitorRoute
import kpn.core.test.TestObjects.newMonitorState
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorState

class MonitorUpdaterTest11_multi_gpx_delete_gpx extends MonitorUpdateTest {

  test("add route with gpx references per subrelation - delete subrelation gpx reference") {

    val (group, route, reference11, reference112, state11, state111, state112, reporter) = setup()
    verifyDocumentCounts(1, 3, 3)

    executeGpxDelete(group, reporter)

    verifyDocumentCounts(1, 2, 2)

    val updatedRoute = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get

    updatedRoute.referenceDistance should equal(200)
    updatedRoute.deviationCount should equal(0)
    updatedRoute.deviationDistance should equal(0)

    configuration.monitorRouteRepository.reference(route._id, Some(11)) should equal(Some(reference11))
    configuration.monitorRouteRepository.state(route._id, 11) should equal(Some(state11))

    configuration.monitorRouteRepository.reference(route._id, Some(111)) should equal(None)
    configuration.monitorRouteRepository.state(route._id, 111) should equal(None)

    configuration.monitorRouteRepository.reference(route._id, Some(112)) should equal(Some(reference112))
    configuration.monitorRouteRepository.state(route._id, 112) should equal(Some(state112))
  }

  private def executeGpxDelete(group: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.gpxDelete,
          groupName = group.name,
          routeName = "route-name",
          referenceType = MonitorReferenceType.multiGpx,
          relationId = Some(111),
        )
      )
    )
  }

  private def setup(): (
    MonitorGroup,
      MonitorRoute,
      MonitorReference,
      MonitorReference,
      MonitorState,
      MonitorState,
      MonitorState,
      MonitorUpdateReporterMock
    ) = {

    val group = newMonitorGroup("group")
    val route = setupRoute(group)
    val reference11 = setupReference11(route)
    val reference111 = setupReference111(route)
    val reference112 = setupReference112(route)

    val state11 = setupState11(route)
    val state111 = setupState111(route)
    val state112 = setupState112(route)

    configuration.monitorGroupRepository.saveGroup(group)
    configuration.monitorRouteRepository.saveRoute(route)
    configuration.monitorRouteRepository.saveReference(reference11)
    configuration.monitorRouteRepository.saveReference(reference111)
    configuration.monitorRouteRepository.saveReference(reference112)
    configuration.monitorRouteRepository.saveState(state11)
    configuration.monitorRouteRepository.saveState(state111)
    configuration.monitorRouteRepository.saveState(state112)

    Time.set(CurrentTimestamp)
    val reporter = new MonitorUpdateReporterMock()
    (group, route, reference11, reference112, state11, state111, state112, reporter)
  }

  private def setupRoute(group: MonitorGroup): MonitorRoute = {
    newMonitorRoute(
      group._id,
      name = "route-name",
      relationId = Some(1),
      user = "user",
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = Some(Timestamp(2022, 8, 11)),
      referenceFilename = None,
      referenceDistance = 300,
      deviationCount = 1,
      deviationDistance = 50
    )
  }

  private def setupReference11(route: MonitorRoute): MonitorReference = {
    newMonitorReference(
      routeId = route._id,
      relationId = Some(11),
      referenceType = MonitorReferenceType.gpx,
      referenceTimestamp = Timestamp(2022, 8, 11),
      distance = 100,
      filename = Some("filename-11"),
    )
  }

  private def setupReference111(route: MonitorRoute): MonitorReference = {
    newMonitorReference(
      routeId = route._id,
      relationId = Some(111),
      referenceType = MonitorReferenceType.gpx,
      referenceTimestamp = Timestamp(2022, 8, 11),
      distance = 100,
      filename = Some("filename-111"),
    )
  }

  private def setupReference112(route: MonitorRoute): MonitorReference = {
    newMonitorReference(
      routeId = route._id,
      relationId = Some(112),
      referenceType = MonitorReferenceType.gpx,
      referenceTimestamp = Timestamp(2022, 8, 11),
      distance = 100,
      filename = Some("filename-112"),
    )
  }

  private def setupState11(route: MonitorRoute): MonitorState = {
    newMonitorState(
      routeId = route._id,
      relationId = 11,
      timestamp = Timestamp(2022, 8, 11),
      matchesDistance = 100
    )
  }

  private def setupState111(route: MonitorRoute): MonitorState = {
    newMonitorState(
      routeId = route._id,
      relationId = 111,
      timestamp = Timestamp(2022, 8, 11),
      deviations = Seq(
        MonitorRouteDeviation(
          id = 1,
          meters = 50,
          distance = 12,
          bounds = Bounds(1, 1, 1, 1),
          lines = Seq.empty
        )
      ),
      matchesDistance = 100,
      matchesLines = Seq("matches"),
    )
  }

  private def setupState112(route: MonitorRoute): MonitorState = {
    newMonitorState(
      routeId = route._id,
      relationId = 112,
      timestamp = Timestamp(2022, 8, 11),
      matchesDistance = 100,
    )
  }
}
