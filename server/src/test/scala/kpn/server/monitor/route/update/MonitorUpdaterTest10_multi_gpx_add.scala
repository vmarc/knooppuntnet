package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.api.common.data.MemberType
import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.test.OverpassData
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState

class MonitorUpdaterTest10_multi_gpx_add extends MonitorUpdateTest {

  test("add route with gpx references per sub-relation") {

    val (group, routeAddReporter) = setup()

    executeAddRoute(group, routeAddReporter)
    val (route, state11, state12) = verifyAddRoute(group, routeAddReporter)

    val gpxUpload1Reporter = executeGpxUpload1(group)
    val reference11 = verifyGpxUpload1(group, route, state11, state12, gpxUpload1Reporter)

    val gpxUpload2Reporter = executeGpxUpload2(group)
    verifyGpxUpload2(group, route, state12, reference11, gpxUpload2Reporter)
  }

  private def executeAddRoute(group: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    val route = MonitorRouteUpdate(
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
        reporter,
        route
      )
    )
  }

  private def executeGpxUpload1(group: MonitorGroup) = {
    val gpx =
      """
        |<gpx>
        |  <trk>
        |    <trkseg>
        |      <trkpt lat="51.4633666" lon="4.4553911"></trkpt>
        |      <trkpt lat="51.4618272" lon="4.4562458"></trkpt>
        |    </trkseg>
        |  </trk>
        |</gpx>
        |""".stripMargin

    Time.set(GpxUpload1Timestamp)
    val update = MonitorRouteUpdate(
      action = MonitorAction.gpxUpload,
      groupName = group.name,
      routeName = "route-name",
      referenceType = MonitorReferenceType.multiGpx,
      relationId = Some(11),
      referenceTimestamp = Some(Timestamp(2022, 8, 1, 0, 0, 0)),
      referenceFilename = Some("filename-1"),
      referenceGpx = Some(gpx)
    )

    val reporter = new MonitorUpdateReporterMock()
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user2",
        reporter,
        update
      )
    )
    reporter
  }

  private def executeGpxUpload2(group: MonitorGroup) = {
    val gpx =
      """
        |<gpx>
        |  <trk>
        |    <trkseg>
        |      <trkpt lat="51.4618272" lon="4.4562458"></trkpt>
        |      <trkpt lat="51.4614496" lon="4.4550560"></trkpt>
        |    </trkseg>
        |  </trk>
        |</gpx>
        |""".stripMargin

    Time.set(GpxUpload2Timestamp)

    val uploadGpx2 = MonitorRouteUpdate(
      action = MonitorAction.gpxUpload,
      groupName = group.name,
      routeName = "route-name",
      referenceType = MonitorReferenceType.multiGpx,
      relationId = Some(12),
      referenceTimestamp = Some(Timestamp(2022, 8, 2, 0, 0, 0)),
      referenceFilename = Some("filename-2"),
      referenceGpx = Some(gpx)
    )

    val uploadGpxReporter2 = new MonitorUpdateReporterMock()
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user3",
        uploadGpxReporter2,
        uploadGpx2
      )
    )
    uploadGpxReporter2
  }

  private def verifyGpxUpload2(group: MonitorGroup, route: MonitorRoute, state12: MonitorRouteState, reference11: MonitorRouteReference, uploadGpxReporter2: MonitorUpdateReporterMock): Unit = {
    verifyGpxUpload2Messages(uploadGpxReporter2.messages)
    verifyGpxUpload2DocumentCounts()
    verifyGpxUpload2Route(group, route)
    verifyNoReference(route)
    assertEqual(
      configuration.monitorRouteRepository.routeReference(route._id, Some(11)),
      Some(reference11)
    )
    verifyGpxUpload2Reference(route)
    verifyGpxUpload2State12(route, state12)
  }

  private def verifyGpxUpload2DocumentCounts(): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(2)
    database.monitorRouteStates.countDocuments() should equal(2)
  }

  private def verifyGpxUpload1(group: MonitorGroup, route: MonitorRoute, state11: MonitorRouteState, state12: MonitorRouteState, reporter: MonitorUpdateReporterMock) = {
    verifyGpxUpload1DocumentCounts()
    verifyGpxUpload1Route(group, route)
    val reference11 = verifyReference11(route)
    verifyNoReference(route)
    verifyNoReference12(route)

    verifyGpxUploadState11(route, state11)

    assertEqual(
      configuration.monitorRouteRepository.routeState(route._id, 12),
      Some(state12)
    )
    verifyGpxUpload1ReporterMessages(reporter.messages)
    reference11
  }

  private def verifyAddRoute(group: MonitorGroup, routeAddReporter: MonitorUpdateReporterMock) = {
    verifyRouteAddMessages(routeAddReporter.messages)
    verifyDocumentCounts()
    val route = verifyRoute(group)
    verifyNoReference(route)
    verifyNoReference11(route)
    verifyNoReference12(route)
    verifyNoState1(route)
    val state11 = verifyState11(route)
    val state12 = verifyState12(route)
    (route, state11, state12)
  }

  private def verifyGpxUpload1DocumentCounts(): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(1)
    database.monitorRouteStates.countDocuments() should equal(2)
  }

  private def verifyNoState1(route: MonitorRoute) = {
    configuration.monitorRouteRepository.routeState(route._id, 1) should equal(None)
  }

  private def verifyNoReference(route: MonitorRoute) = {
    configuration.monitorRouteRepository.routeReference(route._id, Some(1)) should equal(None)
  }

  private def verifyNoReference11(route: MonitorRoute) = {
    configuration.monitorRouteRepository.routeReference(route._id, Some(11)) should equal(None)
  }

  private def verifyNoReference12(route: MonitorRoute) = {
    configuration.monitorRouteRepository.routeReference(route._id, Some(12)) should equal(None)
  }

  private def verifyDocumentCounts() = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(0)
    database.monitorRouteStates.countDocuments() should equal(2)
  }

  private def verifyRoute(group: MonitorGroup) = {
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
        osmDistance = 181,
        relation = None,
        happy = false,
      )
    )
    route
  }

  private def verifyState11(route: MonitorRoute) = {
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

  private def verifyState12(route: MonitorRoute) = {
    val state = configuration.monitorRouteRepository.routeState(route._id, 12).get
    assertEqual(
      state,
      MonitorRouteState(
        state._id,
        routeId = route._id,
        relationId = 12,
        timestamp = CurrentTimestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
        matchesDistance = 181,
        matchesGeometry = None,
        deviations = Seq.empty,
      )
    )
    state
  }

  private def verifyGpxUpload1Route(group: MonitorGroup, route: MonitorRoute): Unit = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      route.copy(
        analysisTimestamp = Some(GpxUpload1Timestamp),
        analysisDuration = None,
        referenceDistance = 181,
        relation = route.relation.map { relation =>
          relation.copy(
            relations = Seq(
              relation.relations.head.copy(
                referenceTimestamp = Some(ReferenceTimestamp1),
                referenceFilename = Some("filename-1"),
                referenceDistance = 181,
                happy = true,
              ),
              relation.relations(1)
            )
          )
        },
        happy = false,
      )
    )
  }

  private def verifyReference11(route: MonitorRoute) = {
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
        referenceTimestamp = ReferenceTimestamp1,
        referenceDistance = 181,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename-1"),
        referenceGeoJson = sameRouteGeometryWithLineString
      )
    )
    reference
  }

  private def verifyGpxUploadState11(route: MonitorRoute, state11: MonitorRouteState): Unit = {
    val state = configuration.monitorRouteRepository.routeState(route._id, 11).get
    assertEqual(
      state,
      state11.copy(
        timestamp = GpxUpload1Timestamp,
        matchesGeometry = Some(routeGeometry),
      )
    )
  }

  private def verifyGpxUpload2Route(group: MonitorGroup, route: MonitorRoute): Unit = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      route.copy(
        analysisTimestamp = Some(GpxUpload2Timestamp),
        analysisDuration = None,
        referenceDistance = 274,
        relation = route.relation.map { relation =>
          relation.copy(
            happy = true,
            relations = Seq(
              relation.relations.head.copy(
                referenceTimestamp = Some(ReferenceTimestamp1),
                referenceFilename = Some("filename-1"),
                referenceDistance = 181,
                happy = true,
              ),
              relation.relations(1).copy(
                referenceTimestamp = Some(Timestamp(2022, 8, 2)),
                referenceFilename = Some("filename-2"),
                referenceDistance = 93,
                happy = true,
              )
            )
          )
        },
        happy = true
      )
    )
  }

  private def verifyGpxUpload2Reference(route: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(route._id, Some(12)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        reference._id,
        routeId = route._id,
        relationId = Some(12),
        timestamp = GpxUpload2Timestamp,
        user = "user3",
        referenceBounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
        referenceType = MonitorReferenceType.gpx, // the route reference type is "multi-gpx", but the invidual reference is "gpx"
        referenceTimestamp = Timestamp(2022, 8, 2),
        referenceDistance = 93,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename-2"),
        referenceGeoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4562458,51.4618272],[4.455056,51.4614496]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
      )
    )
  }

  private def verifyGpxUpload2State12(route: MonitorRoute, state12: MonitorRouteState): Unit = {
    val state = configuration.monitorRouteRepository.routeState(route._id, 12).get
    assertEqual(
      state,
      state12.copy(
        timestamp = GpxUpload2Timestamp,
        matchesGeometry = Some("""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4562458,51.4618272],[4.455056,51.4614496]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""),
      )
    )
  }

  private def setup() = {

    setupLoadStructure()
    setupLoadTopLevel()
    setupBaseRouteDoc1()
    setupBaseRouteDoc11()
    setupBaseRouteDoc12()

    val group = newMonitorGroup("group")
    configuration.monitorGroupRepository.saveGroup(group)

    Time.set(CurrentTimestamp)
    val routeAddReporter = new MonitorUpdateReporterMock()
    (group, routeAddReporter)
  }

  private def setupBaseRouteDoc1(): Unit = {
    configuration.routeRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(1),
        subRelationTree = Some(
          newRouteRelation(
            relationId = 1,
            name = "main-relation",
            relations = Seq(
              newRouteRelation(
                relationId = 11,
                name = "sub-relation-1",
              ),
              newRouteRelation(
                relationId = 12,
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
        newRouteSummary(11),
        segments = Seq(
          newBaseRouteSegment(1)
        ),
        segmentElements = Seq(
          newBaseRouteSegmentElement(
            segmentId = 1,
            segmentElementId = 1,
            coordinates = "[[4.4553911, 51.4633666],[4.4562458,51.4618272]]"
          )
        ),
      )
    )
  }

  private def setupBaseRouteDoc12(): Unit = {
    configuration.routeRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(12),
        segments = Seq(
          newBaseRouteSegment(1)
        ),
        segmentElements = Seq(
          newBaseRouteSegmentElement(
            segmentId = 1,
            segmentElementId = 1,
            coordinates = "[[4.4562458,51.4618272],[4.4550560,51.4614496]]"
          )
        ),
      )
    )
  }

  private def setupLoadStructure(): Unit = {

    val overpassData = OverpassData()
      .relation(
        1,
        tags = Tags.from(
          "name" -> "main-relation"
        ),
        members = Seq(
          newMember(MemberType.Relation, 11),
          newMember(MemberType.Relation, 12)
        )
      )
      .relation(
        11,
        tags = Tags.from(
          "name" -> "sub-relation-1"
        ),
      )
      .relation(
        12,
        tags = Tags.from(
          "name" -> "sub-relation-2"
        ),
      )

    setupRouteStructure(overpassData, 1)
  }

  private def setupLoadTopLevel(): Unit = {

    val overpassData = OverpassData()
      .node(1001, latitude = "51.4633666", longitude = "4.4553911")
      .node(1002, latitude = "51.4618272", longitude = "4.4562458")
      .node(1003, latitude = "51.4614496", longitude = "4.4550560")
      .way(101, 1001, 1002)
      .way(102, 1002, 1003)
      .relation(
        1,
        tags = Tags.from(
          "name" -> "main-relation"
        ),
      )
      .relation(
        11,
        tags = Tags.from(
          "name" -> "sub-relation-1"
        ),
        members = Seq(
          newMember(MemberType.Way, 101),
        )
      )
      .relation(
        12,
        tags = Tags.from(
          "name" -> "sub-relation-2"
        ),
        members = Seq(
          newMember(MemberType.Way, 102),
        )
      )

    val data = new DataBuilder(overpassData.rawData).data
    val mainRelation = data.relations(1)
    val subRelation1 = data.relations(11)
    val subRelation2 = data.relations(12)

    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 1).returns(Some(mainRelation))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 11).returns(Some(subRelation1))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 12).returns(Some(subRelation2))
  }

  private def verifyRouteAddMessages(messages: Seq[MonitorRouteUpdateStatusMessage]): Unit = {
    assertEqual(
      messages,
      Seq(
        message(
          add("prepare"),
          add("analyze-route-structure"),
          active("prepare")
        ),
        message(
          active("analyze-route-structure")
        ),
        message(
          add("11", Some("1/3 sub-relation-1")),
          add("12", Some("2/3 sub-relation-2")),
          add("1", Some("3/3 main-relation")),
          add("save")
        ),
        message(
          active("11")
        ),
        message(
          active("12")
        ),
        message(
          active("1")
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

  private def verifyGpxUpload1ReporterMessages(messages: Seq[MonitorRouteUpdateStatusMessage]): Unit = {
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

  private def verifyGpxUpload2Messages(messages: Seq[MonitorRouteUpdateStatusMessage]): Unit = {
    assertEqual(
      messages,
      Seq(
        message(
          MonitorRouteUpdateStatusCommand("step-add", "upload"),
          MonitorRouteUpdateStatusCommand("step-add", "save"),
          add("upload"),
        ),
        message(
          add("save")
        ),
        message(
          done("save")
        )
      )
    )
  }
}
