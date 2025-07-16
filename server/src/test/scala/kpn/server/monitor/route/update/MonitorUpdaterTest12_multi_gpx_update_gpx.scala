package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.core.common.Time
import kpn.core.test.TestObjects.newMonitorGroup
import kpn.core.test.TestObjects.newMonitorState
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorRoute

class MonitorUpdaterTest12_multi_gpx_update_gpx extends MonitorUpdateTest {

  private var subRoute11: MonitorTestRoute = _
  private var subRoute12: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    subRoute11 = TestSuperRoute.subRoute11
    subRoute12 = TestSuperRoute.subRoute12
  }

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
      MonitorUpdateArgs(
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
      MonitorUpdateArgs(
        "user2",
        new MonitorUpdateReporterMock(),
        MonitorRouteUpdate(
          action = MonitorAction.gpxUpload,
          groupName = group.name,
          routeName = "route-name",
          referenceType = MonitorReferenceType.multiGpx,
          relationId = Some(subRoute11.relationId),
          referenceTimestamp = Some(ReferenceTimestamp1),
          referenceFilename = Some("filename-1"),
          referenceGpx = Some(subRoute11.gpx)
        )
      )
    )
  }

  private def executeGpxUpload2(group: MonitorGroup): Unit = {
    Time.set(GpxUpload2Timestamp)
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user3",
        new MonitorUpdateReporterMock(),
        MonitorRouteUpdate(
          action = MonitorAction.gpxUpload,
          groupName = group.name,
          routeName = "route-name",
          referenceType = MonitorReferenceType.multiGpx,
          relationId = Some(subRoute12.relationId),
          referenceTimestamp = Some(ReferenceTimestamp2),
          referenceFilename = Some("filename-2"),
          referenceGpx = Some(subRoute12.gpx)
        )
      )
    )
  }

  private def verifyAdd(group: MonitorGroup): MonitorRoute = {
    verifyDocumentCounts(1, 0, 0)
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
        osmDistance = subRoute11.meters + subRoute12.meters,
        happy = false,
      )
    )
    route
  }

  private def verifyGpxUpload1(group: MonitorGroup, route: MonitorRoute): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorReferences.countDocuments() should equal(1)
    database.monitorStates.countDocuments() should equal(1)

    verifyGpxUpload1_route(group, route)
    verifyGpxUpload1_reference11(route)
    configuration.monitorRouteRepository.reference(route._id, Some(1)) should equal(None)
    verifyGpxUpload1_state11(route)
  }

  private def verifyGpxUpload1_route(group: MonitorGroup, route: MonitorRoute): Unit = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      route.copy(
        analysisTimestamp = Some(GpxUpload1Timestamp),
        analysisDuration = None,
        referenceDistance = subRoute11.meters,
        happy = false
      )
    )
  }

  private def verifyGpxUpload1_reference11(route: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.reference(route._id, Some(11)).get
    assertEqual(
      reference,
      MonitorReference(
        reference._id,
        routeId = route._id,
        relationId = Some(subRoute11.relationId),
        timestamp = GpxUpload1Timestamp,
        user = "user2",
        referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        referenceType = MonitorReferenceType.gpx, // the route reference type is "multi-gpx", but the invidual reference is "gpx"
        referenceTimestamp = ReferenceTimestamp1,
        referenceDistance = subRoute11.meters,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename-1"),
        referenceLines = subRoute11.lines,
        tiles = subRoute11.referenceTiles
      )
    )
  }

  private def verifyGpxUpload1_state11(route: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.state(route._id, 11).get
    assertEqual(
      state,
      newMonitorState(
        state._id,
        route._id,
        subRoute11.relationId,
        GpxUpload1Timestamp,
        matchesDistance = subRoute11.meters,
        matchesLines = subRoute11.lines
      )
    )
    val stateTiles = configuration.monitorRouteRepository.stateTiles(route._id, subRoute11.relationId)
    assertEqual(
      stateTiles.map(MonitorStateTileInfo.from),
      subRoute11.stateTiles
    )
  }

  private def verifyGpxUpload2(group: MonitorGroup, route: MonitorRoute): Unit = {
    verifyDocumentCounts(1, 2, 2)

    verifyGpxUpload2_route(group, route)
    configuration.monitorRouteRepository.reference(route._id, Some(1)) should equal(None)
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
        referenceDistance = subRoute11.meters + subRoute12.meters,
        happy = true
      )
    )
  }

  private def verifyGpxUpload2_reference12(route: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.reference(route._id, Some(subRoute12.relationId)).get
    assertEqual(
      reference,
      MonitorReference(
        reference._id,
        routeId = route._id,
        relationId = Some(subRoute12.relationId),
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
        referenceDistance = subRoute12.meters,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename-2"),
        referenceLines = subRoute12.lines,
        tiles = subRoute12.referenceTiles
      )
    )
  }

  private def verifyGpxUpload2_state12(route: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.state(route._id, subRoute12.relationId).get
    assertEqual(
      state,
      newMonitorState(
        state._id,
        route._id,
        subRoute12.relationId,
        GpxUpload2Timestamp,
        matchesDistance = subRoute12.meters,
        matchesLines = subRoute12.lines
      )
    )
    val stateTiles = configuration.monitorRouteRepository.stateTiles(route._id, subRoute12.relationId)
    assertEqual(
      stateTiles.map(MonitorStateTileInfo.from),
      subRoute12.stateTiles
    )
  }

  private def setup(): MonitorGroup = {
    setupSuperRoute()
    val group = newMonitorGroup("group")
    configuration.monitorGroupRepository.saveGroup(group)
    group
  }
}
