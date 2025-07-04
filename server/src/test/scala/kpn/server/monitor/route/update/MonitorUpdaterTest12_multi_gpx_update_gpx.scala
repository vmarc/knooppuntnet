package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState

class MonitorUpdaterTest12_multi_gpx_update_gpx extends MonitorUpdateTest {

  test("gpx reference per subrelation - update subrelation gpx reference") {

    val group = setup()

    executeAddRoute(group)
    val route = verifyAddRoute(group)

    executeUploadGpx1(group)
    verifyGpxUpload1(group, route)

    executeGpxUpload2(group)
    verifyGpxUpload2(group, route)
  }

  private def executeAddRoute(group: MonitorGroup): Unit = {
    val routeAdd = MonitorRouteUpdate(
      action = MonitorAction.add,
      groupName = group.name,
      routeName = "route-name",
      referenceType = MonitorReferenceType.multiGpx,
      description = Some("route-description"),
      comment = Some("route-comment"),
      relationId = Some(1),
    )

    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user1",
        new MonitorUpdateReporterMock(),
        routeAdd
      )
    )
  }

  private def executeUploadGpx1(group: MonitorGroup): Unit = {

    Time.set(GpxUpload1Timestamp)

    val uploadGpx1 = MonitorRouteUpdate(
      action = MonitorAction.gpxUpload,
      groupName = group.name,
      routeName = "route-name",
      referenceType = MonitorReferenceType.multiGpx,
      relationId = Some(11),
      referenceTimestamp = Some(ReferenceTimestamp),
      referenceFilename = Some("filename-1"),
      referenceGpx = Some(TestSuperRoute.gpx1)
    )

    val uploadGpxReporter1 = new MonitorUpdateReporterMock()
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user2",
        uploadGpxReporter1,
        uploadGpx1
      )
    )
  }

  private def executeGpxUpload2(group: MonitorGroup): Unit = {
    Time.set(GpxUpload2Timestamp)

    val uploadGpx2 = MonitorRouteUpdate(
      action = MonitorAction.gpxUpload,
      groupName = group.name,
      routeName = "route-name",
      referenceType = MonitorReferenceType.multiGpx,
      relationId = Some(12),
      referenceTimestamp = Some(Timestamp(2022, 8, 2, 0, 0, 0)),
      referenceFilename = Some("filename-2"),
      referenceGpx = Some(TestSuperRoute.gpx2)
    )

    val uploadGpxReporter2 = new MonitorUpdateReporterMock()
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user3",
        uploadGpxReporter2,
        uploadGpx2
      )
    )
  }

  private def verifyGpxUpload1(group: MonitorGroup, route: MonitorRoute): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(1)
    database.monitorRouteStates.countDocuments() should equal(1)

    verifyUpdatedRoute1(group, route)

    verifyReference11(route)

    configuration.monitorRouteRepository.routeReference(route._id, Some(1)) should equal(None)

    assertUpdatedState11(route)
  }

  private def verifyAddRoute(group: MonitorGroup): MonitorRoute = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(0)
    database.monitorRouteStates.countDocuments() should equal(0)
    verifyRoute(group)
  }

  private def verifyRoute(group: MonitorGroup): MonitorRoute = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      MonitorRoute(
        _id = route._id,
        groupId = group._id,
        name = "route-name",
        description = "route-description",
        comment = Some("route-comment"),
        relationId = Some(1),
        user = "user1",
        timestamp = CurrentTimestamp,
        symbol = None,
        analysisTimestamp = Some(CurrentTimestamp),
        analysisDuration = None,
        referenceType = MonitorReferenceType.multiGpx,
        referenceTimestamp = None,
        referenceDistance = 0,
        referenceFilename = None,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 1,
        osmDistance = 181 + 93,
        relation = None,
        happy = false,
      )
    )
    route
  }

  private def verifyState11(route: MonitorRoute): MonitorRouteState = {
    val state = configuration.monitorRouteRepository.routeState(route._id, 11).get
    assertEqual(
      state,
      MonitorRouteState(
        state._id,
        routeId = route._id,
        relationId = 11,
        timestamp = CurrentTimestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        matchesDistance = 181,
        matchesGeometry = None,
        deviations = Seq.empty,
      )
    )
    state
  }

  private def verifyUpdatedRoute1(group: MonitorGroup, route: MonitorRoute): Unit = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      route.copy(
        analysisTimestamp = Some(GpxUpload1Timestamp),
        analysisDuration = None,
        referenceDistance = 181,
        happy = false
      )
    )
  }

  private def verifyReference11(route: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(route._id, Some(11)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        reference._id,
        routeId = route._id,
        relationId = Some(11),
        timestamp = GpxUpload1Timestamp,
        user = "user2",
        referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        referenceType = MonitorReferenceType.gpx, // the route reference type is "multi-gpx", but the invidual reference is "gpx"
        referenceTimestamp = ReferenceTimestamp,
        referenceDistance = 181,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename-1"),
        referenceGeoJson = sameRouteGeometryWithLineString
      )
    )
  }

  private def assertUpdatedState11(route: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.routeState(route._id, 11).get
    assertEqual(
      state,
      newMonitorRouteState(
        state._id,
        route._id,
        11,
        GpxUpload1Timestamp,
        matchesDistance = 181,
        matchesGeometry = Some(routeGeometry)
      )
    )
  }

  private def verifyUpdatedRoute2(group: MonitorGroup, route: MonitorRoute): Unit = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      route.copy(
        analysisTimestamp = Some(GpxUpload2Timestamp),
        analysisDuration = None,
        referenceDistance = 274,
        relation = None,
        happy = true
      )
    )
  }

  private def assertUpdatedReference11(route: MonitorRoute, reference11: MonitorRouteReference): Unit = {
    assertEqual(
      configuration.monitorRouteRepository.routeReference(route._id, Some(11)),
      Some(
        reference11.copy(
          timestamp = GpxUpload2Timestamp,
          user = "user3",
          referenceTimestamp = Timestamp(2022, 8, 2, 0, 0, 0),
          referenceFilename = Some("filename-2")
        )
      )
    )
  }

  private def verifyGpxUpload2(group: MonitorGroup, route: MonitorRoute): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(2)
    database.monitorRouteStates.countDocuments() should equal(2)

    verifyUpdatedRoute2(group, route)

    configuration.monitorRouteRepository.routeReference(route._id, Some(1)) should equal(None)
    assertUpdatedReference12(route)
    assertUpdatedState12(route)
  }

  private def assertUpdatedReference12(route: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(route._id, Some(12)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        reference._id,
        routeId = route._id,
        relationId = Some(12),
        timestamp = GpxUpload2Timestamp,
        user = "user3",
        referenceBounds = Bounds(
          51.4614496,
          4.455056,
          51.4618272,
          4.4562458
        ),
        referenceType = MonitorReferenceType.gpx, // the route reference type is "multi-gpx", but the invidual reference is "gpx"
        referenceTimestamp = Timestamp(2022, 8, 2),
        referenceDistance = 93,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename-2"),
        referenceGeoJson = subroute12Geometry
      )
    )
  }

  private def assertUpdatedState12(route: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.routeState(route._id, 12).get
    assertEqual(
      state,
      newMonitorRouteState(
        state._id,
        route._id,
        12,
        GpxUpload2Timestamp,
        matchesDistance = 93,
        matchesGeometry = Some(sameSubroute12Geometry)
      )
    )
  }

  private def setup(): MonitorGroup = {

    setupSuperRoute()

    val group = newMonitorGroup("group")
    configuration.monitorGroupRepository.saveGroup(group)

    Time.set(CurrentTimestamp)

    group
  }
}
