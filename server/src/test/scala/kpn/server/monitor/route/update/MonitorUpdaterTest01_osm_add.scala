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

class MonitorUpdaterTest01_osm_add extends MonitorUpdateTest {

  test("add non-super route with osm reference") {

    val (group, reporter) = setup()

    executeAdd(group, reporter)

    verifyDocumentCounts()
    val monitorRoute = verifyMonitorRoute(group)
    verifyRouteReference(monitorRoute)
    verifyRouteState(monitorRoute)
    verifyReporterMessages(reporter)
  }

  private def executeAdd(group: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.add,
          groupName = group.name,
          routeName = "route-name",
          referenceType = MonitorReferenceType.osm,
          description = Some("route-description"),
          comment = Some("route-comment"),
          relationId = Some(1),
          referenceTimestamp = Some(ReferenceTimestamp),
        )
      )
    )
  }

  private def verifyDocumentCounts(): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(1)
    database.monitorRouteStates.countDocuments() should equal(1)
  }

  private def verifyMonitorRoute(group: MonitorGroup): MonitorRoute = {
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
        user = "user",
        timestamp = CurrentTimestamp,
        symbol = None,
        analysisTimestamp = Some(CurrentTimestamp),
        analysisDuration = None,
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = Some(ReferenceTimestamp),
        referenceFilename = None,
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
    route
  }

  private def verifyRouteReference(monitorRoute: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(monitorRoute._id, Some(1)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        _id = reference._id,
        routeId = monitorRoute._id,
        relationId = Some(1),
        timestamp = CurrentTimestamp,
        user = "user",
        referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = ReferenceTimestamp,
        referenceDistance = 181,
        referenceSegmentCount = 1,
        referenceFilename = None,
        referenceGeoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}]}"""
      )
    )
  }

  private def verifyRouteState(monitorRoute: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.routeState(monitorRoute._id, 1).get
    assertEqual(
      state,
      MonitorRouteState(
        state._id,
        routeId = monitorRoute._id,
        relationId = 1,
        timestamp = CurrentTimestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        matchesGeometry = Some("""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4553911,51.4633666],[4.4562458,51.4618272]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""),
        deviations = Seq.empty,
      )
    )
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
          command("step-add", "1", Some("1/1 route-name")),
          command("step-add", "save")
        ),
        message(
          command("step-active", "1")
        ),
        message(
          command("step-active", "save")
        ),
        message(
          command("step-done", "save")
        )
      )
    )
  }

  private def setup() = {
    setupLoadStructure()
    setupLoadTopLevel()
    setupBaseRouteDoc()

    val group = newMonitorGroup("group")
    configuration.monitorGroupRepository.saveGroup(group)
    val reporter = new MonitorUpdateReporterMock()

    Time.set(CurrentTimestamp)
    (group, reporter)
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
        ),
      )
    setupRouteStructure(overpassData, 1)
  }

  private def setupLoadTopLevel(): Unit = {
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
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 1).returns(Some(relation))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(ReferenceTimestamp), 1).returns(Some(relation))
  }
}
