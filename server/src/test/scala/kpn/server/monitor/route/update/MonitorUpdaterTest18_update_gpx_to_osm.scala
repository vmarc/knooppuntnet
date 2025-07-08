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

class MonitorUpdaterTest18_update_gpx_to_osm extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
  }

  test("add route with gpx reference, and change to reference type osm afterwards") {

    val (group, gpx, reporter) = setup()

    executeAddRouteWithGpxReference(group, gpx, reporter)
    val (addedRoute, addedReference, addedState) = verifyAdd(group)

    executeUpdateRouteWithOsmReference(group, reporter)
    verifyUpdate(group, addedRoute, addedReference, addedState)
  }

  private def executeAddRouteWithGpxReference(group: MonitorGroup, gpx: String, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user1",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.add,
          groupName = group.name,
          routeName = "route-name",
          referenceType = MonitorReferenceType.gpx,
          description = Some("route-description"),
          comment = Some("route-comment"),
          relationId = Some(1),
          referenceTimestamp = Some(ReferenceTimestamp1),
          referenceFilename = Some("filename"),
          referenceGpx = Some(gpx)
        )
      )
    )
  }

  private def executeUpdateRouteWithOsmReference(group: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    Time.set(UpdateTimestamp)

    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user2",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.update,
          groupName = group.name,
          routeName = "route-name",
          referenceType = MonitorReferenceType.osm,
          referenceNow = Some(true),
          description = Some("route-description"),
          comment = Some("route-comment"),
          relationId = Some(1),
        )
      )
    )
  }

  private def verifyAdd(group: MonitorGroup) = {
    verifyDocumentCounts()
    val route = verifyAddedRoute(group)
    val reference = verifyAddedReference(route)
    val state = verifyAddedState(route)
    (route, reference, state)
  }

  private def verifyUpdate(group: MonitorGroup, addedRoute: MonitorRoute, addedReference: MonitorRouteReference, addedState: MonitorRouteState): Unit = {
    verifyDocumentCounts()
    val updatedRoute = verifyUpdatedRoute(group, addedRoute)
    verifyUpdatedReference(addedRoute, addedReference, updatedRoute)
    verifyUpdatedState(addedRoute, addedState, updatedRoute)
  }

  private def verifyDocumentCounts(): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(1)
    database.monitorRouteStates.countDocuments() should equal(1)
  }

  private def verifyAddedRoute(group: MonitorGroup) = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      MonitorRoute(
        route._id,
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
        referenceType = MonitorReferenceType.gpx,
        referenceTimestamp = Some(ReferenceTimestamp1),
        referenceFilename = Some("filename"),
        referenceDistance = 181,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 1,
        osmDistance = 181,
        relation = None,
        happy = true
      )
    )
    route
  }

  private def verifyAddedReference(addedRoute: MonitorRoute) = {
    val reference = configuration.monitorRouteRepository.routeReference(addedRoute._id, Some(1)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        reference._id,
        routeId = addedRoute._id,
        relationId = Some(1),
        timestamp = CurrentTimestamp,
        user = "user1",
        referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        referenceType = MonitorReferenceType.gpx,
        referenceTimestamp = ReferenceTimestamp1,
        referenceDistance = 181,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename"),
        referenceGeoJson = route1.geoJson
      )
    )
    reference
  }

  private def verifyAddedState(addedRoute: MonitorRoute) = {
    val state = configuration.monitorRouteRepository.routeState(addedRoute._id, 1).get
    assertEqual(
      state,
      MonitorRouteState(
        state._id,
        routeId = addedRoute._id,
        relationId = 1,
        timestamp = CurrentTimestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        matchesDistance = 181,
        matchesGeometry = Some(route1.multiLinestringGeoJson),
        deviations = Seq.empty,
      )
    )
    state
  }

  private def verifyUpdatedRoute(group: MonitorGroup, addedRoute: MonitorRoute) = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      MonitorRoute(
        addedRoute._id,
        groupId = group._id,
        name = "route-name",
        description = "route-description",
        comment = Some("route-comment"),
        relationId = Some(1),
        user = "user2",
        timestamp = UpdateTimestamp,
        symbol = None,
        analysisTimestamp = Some(UpdateTimestamp),
        analysisDuration = None,
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = Some(UpdateTimestamp),
        referenceFilename = None,
        referenceDistance = 181,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 1,
        osmDistance = 181,
        relation = None,
        happy = true
      )
    )
    route
  }

  private def verifyUpdatedReference(addedRoute: MonitorRoute, addedReference: MonitorRouteReference, updatedRoute: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(updatedRoute._id, Some(1)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        addedReference._id,
        routeId = addedRoute._id,
        relationId = Some(1),
        timestamp = UpdateTimestamp,
        user = "user2",
        referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = UpdateTimestamp,
        referenceDistance = 181,
        referenceSegmentCount = 1,
        referenceFilename = None,
        referenceGeoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}]}"""
      )
    )
  }

  private def verifyUpdatedState(addedRoute: MonitorRoute, addedState: MonitorRouteState, updatedRoute: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.routeState(updatedRoute._id, 1).get
    assertEqual(
      state,
      MonitorRouteState(
        addedState._id,
        routeId = addedRoute._id,
        relationId = 1,
        timestamp = UpdateTimestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        matchesDistance = 181,
        matchesGeometry = Some(route1.multiLinestringGeoJson),
        deviations = Seq.empty,
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
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(
      Some(UpdateTimestamp)
      , 1
    ).returns(Some(relation))
  }
}
