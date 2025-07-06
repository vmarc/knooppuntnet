package kpn.server.monitor.route.update

import kpn.api.common.data.MemberType
import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.test.OverpassData
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute

class MonitorUpdaterTest05_osm_update extends MonitorUpdateTest {

  test("route with osm reference - delete previous reference if route not found at referenceTimestamp") {

    val (group, route, reporter) = setup()

    executeMonitorUpdate(group, reporter)

    verifyDocumentCounts()
    verifyRoute(group, route)
    verifyReferenceDeleted(route)
    verifyStateDeleted(route)
    verifyReporterMessages(reporter)
  }

  private def executeMonitorUpdate(group: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.update,
          groupName = group.name,
          routeName = "route",
          referenceType = MonitorReferenceType.osm,
          description = Some("route description"),
          relationId = Some(1),
          referenceTimestamp = Some(ReferenceTimestamp1),
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
        relationId = Some(1),
        user = "user",
        timestamp = CurrentTimestamp,
        symbol = None,
        analysisTimestamp = Some(CurrentTimestamp),
        analysisDuration = None,
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = Some(ReferenceTimestamp1),
        referenceFilename = None,
        referenceDistance = 0,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 0,
        osmDistance = 181,
        relation = None,
        happy = false
      )
    )
  }

  private def verifyDocumentCounts(): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(0)
    database.monitorRouteStates.countDocuments() should equal(0)
  }

  private def verifyReferenceDeleted(route: MonitorRoute): Unit = {
    configuration.monitorRouteRepository.routeReference(route._id, Some(1)) should equal(None)
  }

  private def verifyStateDeleted(route: MonitorRoute): Unit = {
    configuration.monitorRouteRepository.routeState(route._id, 1) should equal(None)
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
          add("1", Some("1/1 route")),
          add("save")
        ),
        message(
          active("1")
        ),
        MonitorRouteUpdateStatusMessage(
          errors = Some(Seq("Could not load relation 1 at 2022-08-01 00:00:00"))
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

    setupStructureLoader()
    setupLoadTopLevel()

    val group = newMonitorGroup("group")
    configuration.monitorGroupRepository.saveGroup(group)

    val route = newMonitorRoute(
      group._id,
      name = "route",
      relationId = Some(1),
      user = "user",
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = Some(Timestamp(2022, 8, 11)),
      referenceFilename = None,
      relation = Some(
        newMonitorRouteRelation(
          relationId = 1,
          name = "route"
        )
      )
    )
    val reference = newMonitorRouteReference(
      routeId = route._id,
      relationId = Some(1),
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = Timestamp(2022, 8, 11),
    )
    val state = newMonitorRouteState(
      routeId = route._id,
      relationId = 1,
      timestamp = Timestamp(2022, 8, 11),
    )

    configuration.monitorGroupRepository.saveGroup(group)
    configuration.monitorRouteRepository.saveRoute(route)
    configuration.monitorRouteRepository.saveRouteReference(reference)
    configuration.monitorRouteRepository.saveRouteState(state)

    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(1)
    database.monitorRouteStates.countDocuments() should equal(1)

    Time.set(CurrentTimestamp)
    val reporter = new MonitorUpdateReporterMock()
    (group, route, reporter)
  }

  private def setupStructureLoader(): Unit = {

    val overpassData = OverpassData()
      .relation(
        1,
        tags = Tags.from(
          "name" -> "route"
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
          "name" -> "route"
        ),
        members = Seq(
          newMember(MemberType.Way, 101),
        )
      )

    val relation = new DataBuilder(overpassData.rawData).data.relations(1)
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 1).returns(Some(relation))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(ReferenceTimestamp1), 1).returns(None)
  }
}
