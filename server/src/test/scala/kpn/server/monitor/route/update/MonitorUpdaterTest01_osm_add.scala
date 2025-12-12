package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.test.TestObjects.newMonitorGroup
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorSegment
import kpn.server.monitor.domain.MonitorState

class MonitorUpdaterTest01_osm_add extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
  }

  test("add non-super route with osm reference") {

    val (group, reporter) = setup()

    executeAdd(group, reporter)

    verifyDocumentCounts(1, 1, 1)
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
        relationIds = Seq(route1.relationId),
        bounds = Some(route1.bounds),
        happy = true
      )
    )
    monitorRoute
  }

  private def verifyRouteReference(monitorRoute: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.reference(monitorRoute._id, Some(1)).get
    assertEqual(
      reference,
      MonitorReference(
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
        referenceLines = route1.lines,
        tiles = route1.referenceTiles
      )
    )
  }

  private def verifyRouteState(monitorRoute: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.state(monitorRoute._id, route1.relationId).get
    assertEqual(
      state,
      MonitorState(
        state._id,
        routeId = monitorRoute._id,
        relationId = route1.relationId,
        timestamp = CurrentTimestamp,
        deviations = Seq.empty,
        matchesDistance = route1.meters,
        matchesLines = route1.lines,
        segments = Seq(MonitorSegment(1, 1, route1.lines.head))
      )
    )
    val stateTiles = configuration.monitorRouteRepository.stateTiles(monitorRoute._id, route1.relationId)
    assertEqual(
      stateTiles.map(MonitorStateTileInfo.from),
      route1.stateTiles
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

  private def setup(): (MonitorGroup, MonitorUpdateReporterMock) = {
    setupLoadStructure()
    setupLoadTopLevel()
    configuration.routeRepository.saveBaseRoute(route1.baseRouteDoc)
    configuration.routeRepository.saveRoute(route1.routeDoc)

    val group = newMonitorGroup("group")
    configuration.monitorGroupRepository.saveGroup(group)
    val reporter = new MonitorUpdateReporterMock()

    Time.set(CurrentTimestamp)
    (group, reporter)
  }

  private def setupLoadStructure(): Unit = {
    val monitorRouteRelation = route1.overpassStructure
    (monitorRouteStructureLoader.load _).returnsWith(Some(monitorRouteRelation))
  }

  private def setupLoadTopLevel(): Unit = {
    val relation = route1.overpassTopLevel
    (monitorRouteRelationRepository.loadTopLevel _).returns {
      case (timestamp: Option[Timestamp], relationId: Long) =>
        Option.when(relationId == route1.relationId) {
          relation
        }
    }
  }
}
