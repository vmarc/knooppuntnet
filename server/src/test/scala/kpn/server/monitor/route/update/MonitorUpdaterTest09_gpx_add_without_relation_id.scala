package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.api.common.data.MemberType
import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.custom.Tags
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.test.OverpassData
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState

class MonitorUpdaterTest09_gpx_add_without_relation_id extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
  }

  test("add non-super route with single gpx reference, but initially with relationId unknown") {

    val (group, gpx, reporter) = setup()

    val update = executeAddWithoutRelationId(group, gpx, reporter)

    verifyDocumentCounts()
    val route = verifyRoute(group)
    verifyNoReference(route)
    verifyNoState(route)
    val reference = verifyReference(route)
    verifyReporterMessagesAdd(reporter)

    executeMonitorUpdateWithRelationId(update)

    verifyUpdatedReporterMessages(reporter)
    verifyUpdatedDocumentCounts()
    verifyUpdatedRoute(group, route)
    verifyUpdatedReference(route, reference)
    verifyUpdatedState(route)
  }

  private def executeAddWithoutRelationId(group: MonitorGroup, gpx: String, reporter: MonitorUpdateReporterMock): MonitorRouteUpdate = {

    val update = MonitorRouteUpdate(
      action = MonitorAction.add,
      groupName = group.name,
      routeName = "route-name",
      referenceType = MonitorReferenceType.gpx,
      description = Some("route-description"),
      comment = Some("route-comment"),
      relationId = None, // <-- no relationId yet
      referenceTimestamp = Some(ReferenceTimestamp1),
      referenceFilename = Some("filename"),
      referenceGpx = Some(gpx)
    )

    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user1",
        reporter,
        update
      )
    )
    update
  }

  private def executeMonitorUpdateWithRelationId(update: MonitorRouteUpdate): Unit = {

    Time.set(UpdateTimestamp)

    val update2 = update.copy(
      action = MonitorAction.update,
      relationId = Some(1),
      referenceGpx = None
    )

    val reporter = new MonitorUpdateReporterMock()
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user2",
        reporter,
        update2
      )
    )
  }

  private def verifyUpdatedDocumentCounts(): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(1)
    database.monitorRouteStates.countDocuments() should equal(1)
  }

  private def verifyDocumentCounts(): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(1)
    database.monitorRouteStates.countDocuments() should equal(0)
  }

  private def verifyNoState(route: MonitorRoute): Unit = {
    configuration.monitorRouteRepository.routeState(route._id, 1) should equal(None)
  }

  private def verifyNoReference(route: MonitorRoute): Unit = {
    configuration.monitorRouteRepository.routeReference(route._id, Some(1)) should equal(None)
  }

  private def verifyRoute(group: MonitorGroup): MonitorRoute = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      MonitorRoute(
        route._id,
        groupId = group._id,
        name = "route-name",
        description = "route-description",
        comment = Some("route-comment"),
        relationId = None, // <-- no relationId yet
        user = "user1",
        timestamp = CurrentTimestamp,
        symbol = None,
        analysisTimestamp = Some(CurrentTimestamp),
        analysisDuration = None,
        referenceType = MonitorReferenceType.gpx,
        referenceTimestamp = Some(ReferenceTimestamp1),
        referenceFilename = Some("filename"),
        referenceDistance = 181,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 0,
        osmDistance = 0,
        happy = false,
        relation = None
      )
    )
    route
  }

  private def verifyReference(route: MonitorRoute): MonitorRouteReference = {
    val reference = configuration.monitorRouteRepository.routeReference(route._id, None).get
    assertEqual(
      reference,
      MonitorRouteReference(
        reference._id,
        routeId = route._id,
        relationId = None, // <-- not filled in
        timestamp = CurrentTimestamp,
        user = "user1",
        referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        referenceType = MonitorReferenceType.gpx,
        referenceTimestamp = ReferenceTimestamp1,
        referenceDistance = 181,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename"),
        referenceGeoJson = Some(route1.geoJson)
      )
    )
    reference
  }

  private def verifyUpdatedRoute(group: MonitorGroup, route: MonitorRoute): Unit = {
    val updatedRoute = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      updatedRoute.copy(analysisDuration = None),
      route.copy(
        relationId = Some(1),
        user = "user2",
        timestamp = UpdateTimestamp,
        analysisTimestamp = Some(UpdateTimestamp),
        analysisDuration = None,
        referenceTimestamp = Some(ReferenceTimestamp1),
        referenceFilename = Some("filename"),
        referenceDistance = 181,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 1,
        relation = Some(
          newMonitorRouteRelation(
            relationId = 1,
            name = "route-name",
            happy = true,
          )
        ),
        happy = true
      )
    )
  }

  private def verifyUpdatedReference(route: MonitorRoute, reference: MonitorRouteReference): Unit = {
    val updatedReference = configuration.monitorRouteRepository.routeReference(route._id, Some(1)).get
    assertEqual(
      updatedReference,
      MonitorRouteReference(
        reference._id,
        routeId = route._id,
        relationId = Some(1), // <-- filled in
        timestamp = CurrentTimestamp, // <-- date that reference was added, not latest change by "user2"
        user = "user1", // <-- not "user2" who provided the relationId, the reference was still added by "user1"
        referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        referenceType = MonitorReferenceType.gpx,
        referenceTimestamp = ReferenceTimestamp1,
        referenceDistance = 181,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename"),
        referenceGeoJson = Some(route1.geoJson)
      )
    )
  }

  private def verifyUpdatedState(route: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.routeState(route._id, 1).get
    assertEqual(
      state,
      MonitorRouteState(
        state._id,
        routeId = route._id,
        relationId = 1,
        timestamp = UpdateTimestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        matchesDistance = 181,
        matchesGeometry = Some(route1.multiLinestringGeoJson),
        deviations = Seq.empty,
      )
    )
  }

  private def verifyReporterMessagesAdd(reporter: MonitorUpdateReporterMock): Unit = {
    assertEqual(
      reporter.messages,
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
          add("load-gpx"),
          add("analyze"),
          active("load-gpx")
        ),
        message(
          active("analyze")
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

  private def verifyUpdatedReporterMessages(reporter: MonitorUpdateReporterMock): Unit = {
    assertEqual(
      reporter.messages,
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
          add("load-gpx"),
          add("analyze"),
          active("load-gpx")
        ),
        message(
          active("analyze")
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

  private def setup() = {

    setupLoadStructure()
    setupLoadRelation()
    setupBaseRouteDoc()

    val group = newMonitorGroup("group")
    configuration.monitorGroupRepository.saveGroup(group)

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

    Time.set(CurrentTimestamp)
    val reporter = new MonitorUpdateReporterMock()
    (group, gpx, reporter)
  }

  private def setupBaseRouteDoc(): Unit = {
    configuration.routeRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(1),
        segments = Seq(
          newBaseRouteSegment(1)
        ),
        segmentElements = Seq(
          newBaseRouteSegmentElement(
            segmentId = 1,
            segmentElementId = 1,
            coordinates = "[[4.4553911, 51.4633666],[4.4562458,51.4618272]]"
          )
        )
      )
    )
  }

  private def setupLoadStructure(): Unit = {
    val overpassData = OverpassData()
      .relation(
        1,
        tags = Tags.from(
          "name" -> "route-name"
        )
      )
    setupRouteStructure(Some(ReferenceTimestamp1), overpassData, 1)
  }

  private def setupLoadRelation(): Unit = {

    val overpassData = OverpassData()
      .node(1001, latitude = "51.4633666", longitude = "4.4553911")
      .node(1002, latitude = "51.4618272", longitude = "4.4562458")
      .way(101, 1001, 1002)
      .relation(
        1,
        tags = Tags.from(
          "name" -> "route-name"
        ),
        members = Seq(
          newMember(MemberType.Way, 101),
        )
      )

    val relation = new DataBuilder(overpassData.rawData).data.relations(1)
    (configuration.monitorRouteRelationRepository.load _).when(None, 1).returns(Some(relation))
  }
}
