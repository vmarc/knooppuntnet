package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.core.common.Time
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference

class MonitorUpdaterTest12_multi_gpx_update_gpx extends MonitorUpdateTest {

  test("gpx reference per subrelation - update subrelation gpx reference") {

    val group = setup()

    executeAdd(group)
    val route = verifyAdd(group)

    executeGpxUpload1(group)
    verifyGpxUpload1(group, route)

    executeGpxUpload2(group)
    verifyGpxUpload2(group, route)
  }

  private def executeAdd(group: MonitorGroup): Unit = {
    Time.set(CurrentTimestamp)
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user1",
        new MonitorUpdateReporterMock(),
        MonitorRouteUpdate(
          action = MonitorAction.add,
          groupName = group.name,
          routeName = "route-name",
          referenceType = MonitorReferenceType.multiGpx,
          description = Some("route-description"),
          comment = Some("route-comment"),
          relationId = Some(TestSuperRoute.MainRelationId),
        )
      )
    )
  }

  private def executeGpxUpload1(group: MonitorGroup): Unit = {
    Time.set(GpxUpload1Timestamp)
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user2",
        new MonitorUpdateReporterMock(),
        MonitorRouteUpdate(
          action = MonitorAction.gpxUpload,
          groupName = group.name,
          routeName = "route-name",
          referenceType = MonitorReferenceType.multiGpx,
          relationId = Some(TestSuperRoute.SubRelationId1),
          referenceTimestamp = Some(ReferenceTimestamp1),
          referenceFilename = Some("filename-1"),
          referenceGpx = Some(TestSuperRoute.gpx1)
        )
      )
    )
  }

  private def executeGpxUpload2(group: MonitorGroup): Unit = {
    Time.set(GpxUpload2Timestamp)
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user3",
        new MonitorUpdateReporterMock(),
        MonitorRouteUpdate(
          action = MonitorAction.gpxUpload,
          groupName = group.name,
          routeName = "route-name",
          referenceType = MonitorReferenceType.multiGpx,
          relationId = Some(TestSuperRoute.SubRelationId2),
          referenceTimestamp = Some(ReferenceTimestamp2),
          referenceFilename = Some("filename-2"),
          referenceGpx = Some(TestSuperRoute.gpx2)
        )
      )
    )
  }

  private def verifyAdd(group: MonitorGroup): MonitorRoute = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(0)
    database.monitorRouteStates.countDocuments() should equal(0)
    verifyAdd_route(group)
  }

  private def verifyAdd_route(group: MonitorGroup): MonitorRoute = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route,
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
        osmDistance = TestSuperRoute.SubRelationDistance1 + TestSuperRoute.SubRelationDistance2,
        relation = None,
        happy = false,
      )
    )
    route
  }

  private def verifyGpxUpload1(group: MonitorGroup, route: MonitorRoute): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(1)
    database.monitorRouteStates.countDocuments() should equal(1)

    verifyGpxUpload1_route(group, route)
    verifyGpxUpload1_reference11(route)
    configuration.monitorRouteRepository.routeReference(route._id, Some(1)) should equal(None)
    verifyGpxUpload1_state11(route)
  }

  private def verifyGpxUpload1_route(group: MonitorGroup, route: MonitorRoute): Unit = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      route.copy(
        analysisTimestamp = Some(GpxUpload1Timestamp),
        analysisDuration = None,
        referenceDistance = TestSuperRoute.SubRelationDistance1,
        happy = false
      )
    )
  }

  private def verifyGpxUpload1_reference11(route: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(route._id, Some(11)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        reference._id,
        routeId = route._id,
        relationId = Some(TestSuperRoute.SubRelationId1),
        timestamp = GpxUpload1Timestamp,
        user = "user2",
        referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        referenceType = MonitorReferenceType.gpx, // the route reference type is "multi-gpx", but the invidual reference is "gpx"
        referenceTimestamp = ReferenceTimestamp1,
        referenceDistance = TestSuperRoute.SubRelationDistance1,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename-1"),
        referenceGeoJson = sameRouteGeometryWithLineString
      )
    )
  }

  private def verifyGpxUpload1_state11(route: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.routeState(route._id, 11).get
    assertEqual(
      state,
      newMonitorRouteState(
        state._id,
        route._id,
        TestSuperRoute.SubRelationId1,
        GpxUpload1Timestamp,
        matchesDistance = TestSuperRoute.SubRelationDistance1,
        matchesGeometry = Some(routeGeometry)
      )
    )
  }

  private def verifyGpxUpload2(group: MonitorGroup, route: MonitorRoute): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(2)
    database.monitorRouteStates.countDocuments() should equal(2)

    verifyGpxUpload2_route(group, route)
    configuration.monitorRouteRepository.routeReference(route._id, Some(1)) should equal(None)
    verifyGpxUpload1_reference11(route)
    verifyGpxUpload1_state11(route)
    verifyGpxUpload2_reference12(route)
    verifyGpxUpload2_state12(route)
  }

  private def verifyGpxUpload2_route(group: MonitorGroup, route: MonitorRoute): Unit = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      route.copy(
        analysisTimestamp = Some(GpxUpload2Timestamp),
        analysisDuration = None,
        referenceDistance = TestSuperRoute.SubRelationDistance1 + TestSuperRoute.SubRelationDistance2,
        relation = None,
        happy = true
      )
    )
  }

  private def verifyGpxUpload2_reference12(route: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(route._id, Some(TestSuperRoute.SubRelationId2)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        reference._id,
        routeId = route._id,
        relationId = Some(TestSuperRoute.SubRelationId2),
        timestamp = GpxUpload2Timestamp,
        user = "user3",
        referenceBounds = Bounds(
          51.4614496,
          4.455056,
          51.4618272,
          4.4562458
        ),
        referenceType = MonitorReferenceType.gpx, // the route reference type is "multi-gpx", but the invidual reference is "gpx"
        referenceTimestamp = ReferenceTimestamp2,
        referenceDistance = TestSuperRoute.SubRelationDistance2,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename-2"),
        referenceGeoJson = subroute12Geometry
      )
    )
  }

  private def verifyGpxUpload2_state12(route: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.routeState(route._id, TestSuperRoute.SubRelationId2).get
    assertEqual(
      state,
      newMonitorRouteState(
        state._id,
        route._id,
        TestSuperRoute.SubRelationId2,
        GpxUpload2Timestamp,
        matchesDistance = TestSuperRoute.SubRelationDistance2,
        matchesGeometry = Some(sameSubroute12Geometry)
      )
    )
  }

  private def setup(): MonitorGroup = {
    setupSuperRoute()
    val group = newMonitorGroup("group")
    configuration.monitorGroupRepository.saveGroup(group)
    group
  }
}
