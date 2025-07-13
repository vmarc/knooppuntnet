package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorMessage
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.core.common.Time
import kpn.core.doc.SuperSegment
import kpn.core.test.TestObjects.newMonitorGroup
import kpn.core.test.TestObjects.newMonitorReference
import kpn.core.test.TestObjects.newMonitorRoute
import kpn.core.test.TestObjects.newMonitorState
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.test.TestObjects.newRouteSummary
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute

class MonitorUpdaterTest05_osm_update extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
  }

  test("route with osm reference - delete previous reference if route not found at referenceTimestamp") {

    val (group, route, reporter) = setup()

    executeMonitorUpdate(group, reporter)

    verifyDocumentCounts(1, 0, 0)
    verifyRoute(group, route)
    verifyReporterMessages(reporter)
  }

  private def executeMonitorUpdate(group: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.update,
          groupName = group.name,
          routeName = "route",
          referenceType = MonitorReferenceType.osm,
          description = Some("route description"),
          relationId = Some(route1.relationId),
          referenceTimestamp = Some(ReferenceTimestamp2),
        )
      )
    )
  }

  private def verifyRoute(group: MonitorGroup, route: MonitorRoute): Unit = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route").get
    assertEqual(
      route.copy(analysisDuration = None),
      MonitorRoute(
        _id = route._id,
        groupId = group._id,
        name = "route",
        description = "route description",
        comment = None,
        relationId = Some(route1.relationId),
        user = "user",
        timestamp = CurrentTimestamp,
        symbol = None,
        analysisTimestamp = Some(CurrentTimestamp),
        analysisDuration = None,
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = Some(ReferenceTimestamp2),
        referenceFilename = None,
        referenceDistance = 0,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 1,
        osmDistance = route1.meters,
        happy = false
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
          add("1", Some("1/1 route-name")),
          add("save")
        ),
        message(
          active("1")
        ),
        MonitorMessage(
          errors = Some(Seq("Could not load relation 1 at 2022-08-02 00:00:00"))
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

    setupStructureLoader()
    setupLoadTopLevel()
    setupRouteDoc()

    val group = newMonitorGroup("group")
    configuration.monitorGroupRepository.saveGroup(group)

    val route = newMonitorRoute(
      group._id,
      name = "route",
      relationId = Some(route1.relationId),
      user = "user",
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = Some(ReferenceTimestamp1),
      referenceFilename = None
    )
    val reference = newMonitorReference(
      routeId = route._id,
      relationId = Some(route1.relationId),
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = ReferenceTimestamp1
    )
    val state = newMonitorState(
      routeId = route._id,
      relationId = route1.relationId,
      timestamp = ReferenceTimestamp1
    )

    configuration.monitorGroupRepository.saveGroup(group)
    configuration.monitorRouteRepository.saveRoute(route)
    configuration.monitorRouteRepository.saveReference(reference)
    configuration.monitorRouteRepository.saveState(state)

    verifyDocumentCounts(1, 1, 1)

    Time.set(CurrentTimestamp)
    val reporter = new MonitorUpdateReporterMock()
    (group, route, reporter)
  }

  private def setupStructureLoader(): Unit = {
    val monitorRouteRelation = route1.overpassStructure
    (configuration.monitorRouteStructureLoader.load _).when(Some(ReferenceTimestamp2), route1.relationId).returns(Some(monitorRouteRelation))
  }

  private def setupLoadTopLevel(): Unit = {
    val relation = route1.overpassTopLevel
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(ReferenceTimestamp2), route1.relationId).returns(None)
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
