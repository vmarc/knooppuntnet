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

class MonitorUpdaterTest08_gpx_add extends MonitorUpdateTest {

  test("add non-super route with single gpx reference") {

    val (group, gpx, reporter) = setup()

    executeMonitorUpdate(group, gpx, reporter)

    verifyDocumentCounts()
    val route = verifyRoute(group)
    verifyReference(route)
    verifyState(route)
    verifyReporterMessages(reporter)
  }

  private def executeMonitorUpdate(group: MonitorGroup, gpx: String, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user",
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

  private def verifyDocumentCounts(): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(1)
    database.monitorRouteStates.countDocuments() should equal(1)
  }

  private def verifyRoute(group: MonitorGroup): MonitorRoute = {
    val monitorRoute = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      monitorRoute.copy(analysisDuration = None),
      MonitorRoute(
        monitorRoute._id,
        groupId = group._id,
        name = "route-name",
        description = "route-description",
        comment = Some("route-comment"),
        relationId = Some(1),
        user = "user",
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
    monitorRoute
  }

  private def verifyReference(monitorRoute: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(monitorRoute._id, Some(1)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        reference._id,
        routeId = monitorRoute._id,
        relationId = Some(1),
        timestamp = CurrentTimestamp,
        user = "user",
        referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        referenceType = MonitorReferenceType.gpx,
        referenceTimestamp = ReferenceTimestamp1,
        referenceDistance = 181,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename"),
        referenceGeoJson = sameRouteGeometryWithLineString
      )
    )
  }

  private def verifyState(monitorRoute: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.routeState(monitorRoute._id, 1).get
    assertEqual(
      state,
      MonitorRouteState(
        state._id,
        routeId = monitorRoute._id,
        relationId = 1,
        timestamp = CurrentTimestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        matchesDistance = 181,
        matchesGeometry = Some(routeGeometry),
        deviations = Seq.empty,
      )
    )
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

  private def setupLoadStructure(): Unit = {
    val overpassData = OverpassData()
      .relation(
        1,
        tags = Tags.from(
          "name" -> "route-name"
        )
      )
    setupRouteStructure(overpassData, 1)
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

  private def verifyReporterMessages(reporter: MonitorUpdateReporterMock): Unit = {
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
}
