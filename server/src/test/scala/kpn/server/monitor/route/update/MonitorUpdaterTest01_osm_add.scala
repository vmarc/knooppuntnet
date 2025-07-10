package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.api.common.data.MemberType
import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.custom.Tags
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.doc.SuperSegment
import kpn.core.test.OverpassData
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState

class MonitorUpdaterTest01_osm_add extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
  }

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
      MonitorUpdateArgs(
        "user",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.add,
          groupName = group.name,
          routeName = "route-name",
          referenceType = MonitorReferenceType.osm,
          description = Some("route-description"),
          comment = Some("route-comment"),
          relationId = Some(route1.relationId),
          referenceTimestamp = Some(ReferenceTimestamp1),
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
    val monitorRoute = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      monitorRoute.copy(analysisDuration = None),
      MonitorRoute(
        _id = monitorRoute._id,
        groupId = group._id,
        name = "route-name",
        description = "route-description",
        comment = Some("route-comment"),
        relationId = Some(route1.relationId),
        user = "user",
        timestamp = CurrentTimestamp,
        symbol = None,
        analysisTimestamp = Some(CurrentTimestamp),
        analysisDuration = None,
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = Some(ReferenceTimestamp1),
        referenceFilename = None,
        referenceDistance = route1.meters,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 1,
        osmDistance = route1.meters,
        relation = None,
        happy = true
      )
    )
    monitorRoute
  }

  private def verifyRouteReference(monitorRoute: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(monitorRoute._id, Some(1)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        _id = reference._id,
        routeId = monitorRoute._id,
        relationId = Some(route1.relationId),
        timestamp = CurrentTimestamp,
        user = "user",
        referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = ReferenceTimestamp1,
        referenceDistance = route1.meters,
        referenceSegmentCount = 1,
        referenceFilename = None,
        referenceLines = route1.lines
      )
    )
  }

  private def verifyRouteState(monitorRoute: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.routeState(monitorRoute._id, route1.relationId).get
    assertEqual(
      state,
      MonitorRouteState(
        state._id,
        routeId = monitorRoute._id,
        relationId = route1.relationId,
        timestamp = CurrentTimestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        deviations = Seq.empty,
        matchesDistance = route1.meters,
        matchesLines = route1.lines,
      )
    )
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
          add("1", Some("1/1 route-name")),
          add("save")
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

  private def setup() = {
    setupLoadStructure()
    setupLoadTopLevel()
    setupBaseRouteDoc()
    setupRouteDoc()

    val group = newMonitorGroup("group")
    configuration.monitorGroupRepository.saveGroup(group)
    val reporter = new MonitorUpdateReporterMock()

    Time.set(CurrentTimestamp)
    (group, reporter)
  }

  private def setupBaseRouteDoc(): Unit = {
    configuration.routeRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(route1.relationId),
        segments = Seq(
          newBaseRouteSegment(1)
        ),
        segmentElements = Seq(
          newBaseRouteSegmentElement(
            segmentId = 1,
            segmentElementId = 1,
            coordinates = route1.coordinateString
          )
        )
      )
    )
  }

  private def setupLoadStructure(): Unit = {
    val overpassData = OverpassData()
      .relation(
        route1.relationId,
        tags = Tags.from(
          "name" -> "route-name"
        ),
      )
    setupRouteStructure(Some(ReferenceTimestamp1), overpassData, 1)
  }

  private def setupLoadTopLevel(): Unit = {
    val overpassData = OverpassData()
      .node(1001, latitude = route1.lat1, longitude = route1.lon1)
      .node(1002, latitude = route1.lat2, longitude = route1.lon2)
      .way(101, 1001, 1002)
      .relation(
        route1.relationId,
        tags = Tags.from(
          "name" -> "route-name"
        ),
        members = Seq(
          newMember(MemberType.Way, 101),
        )
      )
    val relation = new DataBuilder(overpassData.rawData).data.relations(route1.relationId)
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, route1.relationId).returns(Some(relation))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(ReferenceTimestamp1), route1.relationId).returns(Some(relation))
  }

  private def setupRouteDoc(): Unit = {
    configuration.routeRepository.saveRoute(
      newRouteDoc(
        newRouteSummary(
          route1.relationId,
          name = "route-name"
        ),
        superDistance = route1.meters,
        routeIds = Seq(route1.relationId),
        superSegments = Seq(
          SuperSegment(
            Seq.empty
          )
        )
      )
    )
  }
}
