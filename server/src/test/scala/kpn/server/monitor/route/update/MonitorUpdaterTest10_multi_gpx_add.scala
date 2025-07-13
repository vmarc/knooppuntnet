package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorMessage
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.core.common.Time
import kpn.core.doc.SuperSegment
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newBaseRouteSegment
import kpn.core.test.TestObjects.newBaseRouteSegmentElement
import kpn.core.test.TestObjects.newMonitorGroup
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.test.TestObjects.newRouteRelation
import kpn.core.test.TestObjects.newRouteSummary
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorState

class MonitorUpdaterTest10_multi_gpx_add extends MonitorUpdateTest {

  private var MainrelationId: Long = _
  private var subRoute11: MonitorTestRoute = _
  private var subRoute12: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    MainrelationId = TestSuperRoute.MainRelationId
    subRoute11 = TestSuperRoute.subRoute11
    subRoute12 = TestSuperRoute.subRoute12
  }

  test("add route with gpx references per sub-relation") {

    val (group, routeAddReporter) = setup()

    executeAddRoute(group, routeAddReporter)
    val route = verifyAdd(group, routeAddReporter)

    val gpxUpload1Reporter = executeGpxUpload1(group)
    val reference11 = verifyGpxUpload1(group, route, gpxUpload1Reporter)

    val gpxUpload2Reporter = executeGpxUpload2(group)
    verifyGpxUpload2(group, route, reference11, gpxUpload2Reporter)
  }

  private def executeAddRoute(group: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    val route = MonitorRouteUpdate(
      action = MonitorAction.add,
      groupName = group.name,
      routeName = "route-name",
      referenceType = MonitorReferenceType.multiGpx,
      description = Some("route-description"),
      comment = Some("route-comment"),
      relationId = Some(MainrelationId),
    )

    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user1",
        reporter,
        route
      )
    )
  }

  private def executeGpxUpload1(group: MonitorGroup) = {
    Time.set(GpxUpload1Timestamp)
    val reporter = new MonitorUpdateReporterMock()
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user2",
        reporter,
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
    reporter
  }

  private def executeGpxUpload2(group: MonitorGroup) = {
    Time.set(GpxUpload2Timestamp)
    val uploadGpxReporter2 = new MonitorUpdateReporterMock()
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user3",
        uploadGpxReporter2,
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
    uploadGpxReporter2
  }

  private def verifyGpxUpload2(group: MonitorGroup, route: MonitorRoute, reference11: MonitorReference, uploadGpxReporter2: MonitorUpdateReporterMock): Unit = {
    verifyGpxUpload2_messages(uploadGpxReporter2.messages)
    verifyDocumentCounts(1, 2, 2)
    verifyGpxUpload2_route(group, route)
    verifyNoReference(route, None)
    assertEqual(
      configuration.monitorRouteRepository.reference(route._id, Some(11)),
      Some(reference11)
    )
    verifyGpxUpload2_reference(route)
    verifyGpxUpload2_state12(route)
  }

  private def verifyGpxUpload1(group: MonitorGroup, route: MonitorRoute, reporter: MonitorUpdateReporterMock): MonitorReference = {
    verifyDocumentCounts(1, 1, 1)
    verifyGpxUpload1_route(group, route)
    val reference11 = verifyReference11(route)
    verifyNoReference(route, None)
    verifyNoReference(route, Some(subRoute12.relationId))

    verifyGpxUploadState11(route)
    verifyNoState(route, subRoute12.relationId)

    verifyGpxUpload1_messages(reporter.messages)
    reference11
  }

  private def verifyAdd(group: MonitorGroup, routeAddReporter: MonitorUpdateReporterMock): MonitorRoute = {
    verifyDocumentCounts(1, 0, 0)
    val route = verifyAdd_route(group)
    verifyAdd_messages(routeAddReporter.messages)
    route
  }

  private def verifyAdd_route(group: MonitorGroup): MonitorRoute = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      MonitorRoute(
        _id = route._id,
        groupId = group._id,
        name = "route-name",
        description = "route-description",
        comment = Some("route-comment"),
        relationId = Some(MainrelationId),
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
        relation = None,
        happy = false,
      )
    )
    route
  }

  private def verifyGpxUpload1_route(group: MonitorGroup, route: MonitorRoute): Unit = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      route.copy(
        analysisTimestamp = Some(GpxUpload1Timestamp),
        analysisDuration = None,
        referenceDistance = subRoute11.meters,
        relation = None,
        happy = false,
      )
    )
  }

  private def verifyReference11(route: MonitorRoute) = {
    val reference = configuration.monitorRouteRepository.reference(route._id, Some(subRoute11.relationId)).get
    assertEqual(
      reference,
      MonitorReference(
        reference._id,
        routeId = route._id,
        relationId = Some(subRoute11.relationId),
        timestamp = GpxUpload1Timestamp,
        user = "user2",
        referenceBounds = subRoute11.bounds,
        referenceType = MonitorReferenceType.gpx, // the route reference type is "multi-gpx", but the invidual reference is "gpx"
        referenceTimestamp = ReferenceTimestamp1,
        referenceDistance = subRoute11.meters,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename-1"),
        referenceLines = subRoute11.lines
      )
    )
    reference
  }

  private def verifyGpxUploadState11(route: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.state(route._id, 11).get
    assertEqual(
      state,
      MonitorState(
        state._id,
        routeId = route._id,
        relationId = subRoute11.relationId,
        timestamp = GpxUpload1Timestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        deviations = Seq.empty,
        matchesDistance = subRoute11.meters,
        matchesLines = subRoute11.lines, // TODO redesign - this cannot be correct if distance is 181
      )
    )
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

  private def verifyGpxUpload2_reference(route: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.reference(route._id, Some(subRoute12.relationId)).get
    assertEqual(
      reference,
      MonitorReference(
        reference._id,
        routeId = route._id,
        relationId = Some(subRoute12.relationId),
        timestamp = GpxUpload2Timestamp,
        user = "user3",
        referenceBounds = subRoute12.bounds,
        referenceType = MonitorReferenceType.gpx, // the route reference type is "multi-gpx", but the invidual reference is "gpx"
        referenceTimestamp = ReferenceTimestamp2,
        referenceDistance = subRoute12.meters,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename-2"),
        referenceLines = subRoute12.lines
      )
    )
  }

  private def verifyGpxUpload2_state12(route: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.state(route._id, subRoute12.relationId).get
    assertEqual(
      state,
      MonitorState(
        state._id,
        routeId = route._id,
        relationId = subRoute12.relationId,
        timestamp = GpxUpload2Timestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
        deviations = Seq.empty,
        matchesDistance = subRoute12.meters,
        matchesLines = subRoute12.lines
      )
    )
  }

  private def verifyAdd_messages(messages: Seq[MonitorMessage]): Unit = {
    assertEqual(
      messages,
      Seq(
        message(
          add("save"),
          active("save")
        ),
        message(
          done("save")
        )
      )
    )
  }

  private def verifyGpxUpload1_messages(messages: Seq[MonitorMessage]): Unit = {
    assertEqual(
      messages,
      Seq(
        message(
          add("upload"),
          add("save"),
          active("upload")
        ),
        message(
          active("save")
        ),
        message(
          done("save")
        )
      )
    )
  }

  private def verifyGpxUpload2_messages(messages: Seq[MonitorMessage]): Unit = {
    assertEqual(
      messages,
      Seq(
        message(
          add("upload"),
          add("save"),
          active("upload"),
        ),
        message(
          active("save")
        ),
        message(
          done("save")
        )
      )
    )
  }

  private def setup(): (MonitorGroup, MonitorUpdateReporterMock) = {

    setupBaseRouteDoc1()
    setupBaseRouteDoc11()
    setupBaseRouteDoc12()
    setupRouteDoc1()

    val group = newMonitorGroup("group")
    configuration.monitorGroupRepository.saveGroup(group)

    Time.set(CurrentTimestamp)
    val routeAddReporter = new MonitorUpdateReporterMock()
    (group, routeAddReporter)
  }

  private def setupBaseRouteDoc1(): Unit = {
    configuration.routeRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(MainrelationId),
        subRelationTree = Some(
          newRouteRelation(
            relationId = MainrelationId,
            name = "main-relation",
            relations = Seq(
              newRouteRelation(
                relationId = subRoute11.relationId,
                name = "sub-relation-1",
              ),
              newRouteRelation(
                relationId = subRoute12.relationId,
                name = "sub-relation-2",
              )
            )
          )
        )
      )
    )
  }

  private def setupBaseRouteDoc11(): Unit = {
    configuration.routeRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(subRoute11.relationId),
        segments = Seq(
          newBaseRouteSegment(1)
        ),
        segmentElements = Seq(
          newBaseRouteSegmentElement(
            segmentId = 1,
            segmentElementId = 1,
            coordinates = subRoute11.coordinateString
          )
        )
      )
    )
  }

  private def setupBaseRouteDoc12(): Unit = {
    configuration.routeRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(subRoute12.relationId),
        segments = Seq(
          newBaseRouteSegment(1)
        ),
        segmentElements = Seq(
          newBaseRouteSegmentElement(
            segmentId = 1,
            segmentElementId = 1,
            coordinates = subRoute12.coordinateString
          )
        )
      )
    )
  }

  private def setupRouteDoc1(): Unit = {
    configuration.routeRepository.saveRoute(
      newRouteDoc(
        newRouteSummary(
          MainrelationId,
          name = "route-name"
        ),
        superDistance = subRoute11.meters + subRoute12.meters,
        routeIds = Seq(MainrelationId, subRoute11.relationId, subRoute12.relationId),
        superSegments = Seq(
          SuperSegment(
            Seq.empty
          )
        )
      )
    )
  }
}
