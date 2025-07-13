package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.core.common.Time
import kpn.core.doc.SuperSegment
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newBaseRouteSegment
import kpn.core.test.TestObjects.newBaseRouteSegmentElement
import kpn.core.test.TestObjects.newMonitorGroup
import kpn.core.test.TestObjects.newMonitorReference
import kpn.core.test.TestObjects.newMonitorRoute
import kpn.core.test.TestObjects.newMonitorState
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.test.TestObjects.newRouteSummary
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorState

class MonitorUpdaterTest21_osm_update_relation_id extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _
  private var route2: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
    route2 = MonitorTestData.route2
  }

  test("osm reference, update relation id - delete obsolete reference and state") {

    val (group, route, reporter) = setup()

    executeUpdate(group, reporter)

    verifyDocumentCounts(1, 1, 1)

    verifyUpdatedRoute(group, route)

    configuration.monitorRouteRepository.routeReference(route._id, Some(route1.relationId)) should equal(None)
    configuration.monitorRouteRepository.routeState(route._id, route1.relationId) should equal(None)

    verifyReference2(route)
    verifyState2(route)

    verifyReporterMessages(reporter)
  }

  private def executeUpdate(group: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user2",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.update,
          groupName = group.name,
          routeName = "route",
          referenceType = MonitorReferenceType.osm,
          description = Some("description"),
          relationId = Some(route2.relationId), // new relation id
          referenceTimestamp = Some(ReferenceTimestamp1),
        )
      )
    )
  }

  private def verifyUpdatedRoute(group: MonitorGroup, route: MonitorRoute): Unit = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route").get
    assertEqual(
      route.copy(analysisDuration = None),
      MonitorRoute(
        _id = route._id,
        groupId = group._id,
        name = "route",
        description = "description",
        comment = None,
        relationId = Some(route2.relationId),
        user = "user2",
        timestamp = UpdateTimestamp,
        symbol = None,
        analysisTimestamp = Some(UpdateTimestamp),
        analysisDuration = None,
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = Some(ReferenceTimestamp1),
        referenceFilename = None,
        referenceDistance = route2.meters,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 1,
        osmDistance = route2.meters,
        relation = None,
        happy = true
      )
    )
  }

  private def verifyReference2(route: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(route._id, Some(route2.relationId)).get
    assertEqual(
      reference,
      MonitorReference(
        _id = reference._id,
        routeId = route._id,
        relationId = Some(route2.relationId),
        timestamp = UpdateTimestamp,
        user = "user2",
        referenceBounds = route2.bounds,
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = ReferenceTimestamp1,
        referenceDistance = route2.meters,
        referenceSegmentCount = 1,
        referenceFilename = None,
        referenceLines = route2.lines
      )
    )
  }

  private def verifyState2(route: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.routeState(route._id, route2.relationId).get
    assertEqual(
      state,
      MonitorState(
        state._id,
        routeId = route._id,
        relationId = route2.relationId,
        timestamp = UpdateTimestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        deviations = Seq.empty,
        matchesDistance = route2.meters,
        matchesLines = route2.lines,
      )
    )
  }

  private def verifyReporterMessages(reporter: MonitorUpdateReporterMock): Unit = {
    assertEqual(
      reporter.messages,
      Seq(
        message(
          add("prepare"),
          active("prepare")
        ),
        message(
          add("analyze-route-structure"),
          active("analyze-route-structure")
        ),
        message(
          add("2", Some("1/1 route-name")),
          add("save", None),
        ),
        message(
          active("2")
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

  private def setup(): (MonitorGroup, MonitorRoute, MonitorUpdateReporterMock) = {

    setupLoadStructure()
    setupLoadTopLevel()
    setupBaseRouteDoc()
    setupRouteDoc1()
    setupRouteDoc2()

    val group = newMonitorGroup("group")
    val route = setupRoute(group)
    val reference = setupReference(route)
    val state = setupState(route)

    configuration.monitorGroupRepository.saveGroup(group)
    configuration.monitorRouteRepository.saveRoute(route)
    configuration.monitorRouteRepository.saveRouteReference(reference)
    configuration.monitorRouteRepository.saveRouteState(state)

    Time.set(UpdateTimestamp)
    val reporter = new MonitorUpdateReporterMock()
    (group, route, reporter)
  }

  private def setupRoute(group: MonitorGroup): MonitorRoute = {
    newMonitorRoute(
      group._id,
      name = "route",
      relationId = Some(route1.relationId),
      user = "user1",
      symbol = None,
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = Some(ReferenceTimestamp1),
      referenceFilename = None,
      referenceDistance = 1000,
      deviationDistance = 100,
      deviationCount = 2,
      osmDistance = 1010,
      osmSegmentCount = 1,
      relation = None,
    )
  }

  private def setupReference(route: MonitorRoute): MonitorReference = {
    newMonitorReference(
      routeId = route._id,
      relationId = Some(route1.relationId),
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = ReferenceTimestamp1,
    )
  }

  private def setupState(route: MonitorRoute): MonitorState = {
    newMonitorState(
      routeId = route._id,
      relationId = route1.relationId,
      timestamp = ReferenceTimestamp1,
    )
  }

  private def setupBaseRouteDoc(): Unit = {
    configuration.routeRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(route2.relationId),
        segments = Seq(
          newBaseRouteSegment(1)
        ),
        segmentElements = Seq(
          newBaseRouteSegmentElement(
            segmentId = 1,
            segmentElementId = 1,
            coordinates = route2.coordinateString,
          )
        )
      )
    )
  }

  private def setupLoadStructure(): Unit = {
    val monitorRouteRelation = route2.overpassStructure
    (configuration.monitorRouteStructureLoader.load _).when(Some(ReferenceTimestamp1), route2.relationId).returns(Some(monitorRouteRelation))
  }

  private def setupLoadTopLevel(): Unit = {
    val relation = route2.overpassTopLevel
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(ReferenceTimestamp1), route2.relationId).returns(Some(relation))
  }

  private def setupRouteDoc1(): Unit = {
    configuration.routeRepository.saveRoute(
      newRouteDoc(
        newRouteSummary(
          route1.relationId,
          name = "route-name-1"
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

  private def setupRouteDoc2(): Unit = {
    configuration.routeRepository.saveRoute(
      newRouteDoc(
        newRouteSummary(
          route2.relationId,
          name = "route-name-2"
        ),
        superDistance = route2.meters,
        routeIds = Seq(route2.relationId),
        superSegments = Seq(
          SuperSegment(
            Seq.empty
          )
        )
      )
    )
  }
}
