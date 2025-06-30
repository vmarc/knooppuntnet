package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.test.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.database.base.Database
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest07_osm_update_properties extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  private val ReferenceTimestamp = Timestamp(2022, 8, 1)
  private val CurrentTimestamp = Timestamp(2022, 8, 11, 12, 0, 0)
  private val UpdateTimestamp = Timestamp(2022, 8, 12, 12, 0, 0)

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("update name, description and comment (state and reference unchanged, no analysis)") {

    withDatabase() { database =>
      val (configuration, group, route, state, reference, reporter) = setup(database)

      executeMonitorUpdate(configuration, group, reporter)

      verifyDocumentCounts(database)
      verifyRoute(configuration, group, route)
      verifyReference(configuration, route, reference)
      verifyState(configuration, route, state)
      verifyReporterMessages(reporter)
    }
  }

  private def executeMonitorUpdate(configuration: MonitorUpdaterConfiguration, group: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user2",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.update,
          groupName = group.name,
          routeName = "route-name",
          referenceType = MonitorReferenceType.osm,
          description = Some("description-changed"), // <-- changed
          comment = Some("comment-changed"), // <-- changed
          relationId = Some(1),
          referenceTimestamp = Some(ReferenceTimestamp),
          newRouteName = Some("route-name-changed") // <-- changed
        )
      )
    )
  }

  private def verifyDocumentCounts(database: Database): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(1)
    database.monitorRouteStates.countDocuments() should equal(1)
  }

  private def verifyRoute(configuration: MonitorUpdaterConfiguration, group: MonitorGroup, route: MonitorRoute): Unit = {
    val updatedRoute = configuration.monitorRouteRepository.routeByName(group._id, "route-name-changed").get
    assertEqual(
      updatedRoute.copy(analysisTimestamp = None, analysisDuration = None),
      route.copy(
        name = "route-name-changed",
        description = "description-changed",
        comment = Some("comment-changed"),
        user = "user2",
        timestamp = UpdateTimestamp
      )
    )
  }

  private def verifyReference(configuration: MonitorUpdaterConfiguration, route: MonitorRoute, reference: MonitorRouteReference): Unit = {
    val updatedReference = configuration.monitorRouteRepository.routeReference(route._id, Some(1)).get
    updatedReference should equal(reference) // no change
  }

  private def verifyState(configuration: MonitorUpdaterConfiguration, route: MonitorRoute, state: MonitorRouteState): Unit = {
    val updatedState = configuration.monitorRouteRepository.routeState(route._id, 1).get
    updatedState should equal(state) // no change
  }

  private def verifyReporterMessages(reporter: MonitorUpdateReporterMock): Unit = {
    // not analyzed, no errors
    assertEqual(
      reporter.messages,
      Seq(
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-add", "prepare"),
            MonitorRouteUpdateStatusCommand("step-add", "analyze-route-structure"),
            MonitorRouteUpdateStatusCommand("step-active", "prepare")
          )
        ),
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-active", "analyze-route-structure")
          )
        ),
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-active", "save")
          )
        ),
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-done", "save")
          )
        )
      )
    )
  }

  private def setup(database: Database) = {
    val configuration = MonitorUpdaterTestSupport.configuration(database)

    val group = newMonitorGroup("group")
    val route = setupRoute(group)
    val state = setupState(route)
    val reference = setupReference(route)

    configuration.monitorGroupRepository.saveGroup(group)
    configuration.monitorRouteRepository.saveRoute(route)
    configuration.monitorRouteRepository.saveRouteState(state)
    configuration.monitorRouteRepository.saveRouteReference(reference)

    Time.set(UpdateTimestamp)
    val reporter = new MonitorUpdateReporterMock()
    (configuration, group, route, state, reference, reporter)
  }

  private def setupRoute(group: MonitorGroup) = {
    MonitorRoute(
      ObjectId(),
      groupId = group._id,
      name = "route-name",
      description = "route-description",
      comment = Some("route-comment"),
      relationId = Some(1),
      user = "user1",
      timestamp = CurrentTimestamp,
      symbol = None,
      analysisTimestamp = None,
      analysisDuration = None,
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = Some(ReferenceTimestamp),
      referenceFilename = None,
      referenceDistance = 196,
      deviationDistance = 0,
      deviationCount = 0,
      osmSegmentCount = 1,
      happy = true,
      relation = Some(
        newMonitorRouteRelation(
          relationId = 1,
          name = "route-name",
          happy = true,
        )
      )
    )
  }

  private def setupState(route: MonitorRoute) = {
    MonitorRouteState(
      ObjectId(),
      routeId = route._id,
      relationId = 1,
      timestamp = CurrentTimestamp,
      // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
      matchesGeometry = Some("""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4553911,51.4633666],[4.4562458,51.4618272]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""),
      deviations = Seq.empty,
    )
  }

  private def setupReference(route: MonitorRoute) = {
    MonitorRouteReference(
      ObjectId(),
      routeId = route._id,
      relationId = Some(1),
      timestamp = CurrentTimestamp,
      user = "user1",
      referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = ReferenceTimestamp,
      referenceDistance = 196,
      referenceSegmentCount = 1,
      referenceFilename = None,
      referenceGeoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}]}"""
    )
  }
}
