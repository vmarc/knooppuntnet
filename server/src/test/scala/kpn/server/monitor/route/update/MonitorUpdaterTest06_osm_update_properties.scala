package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.MockLog
import kpn.core.util.UnitTest
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest06_osm_update_properties extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  private val log = new MockLog()

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("update name, description and comment (state and reference unchanged, no analysis)") {

    withDatabase() { database =>
      val configuration = MonitorUpdaterTestSupport.configuration(database)
      val group = newMonitorGroup("group")
      val route = MonitorRoute(
        ObjectId(),
        groupId = group._id,
        name = "route-name",
        description = "route-description",
        comment = Some("route-comment"),
        relationId = Some(1),
        user = "user1",
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        symbol = None,
        referenceType = "osm",
        referenceTimestamp = Some(Timestamp(2022, 8, 1)),
        referenceFilename = None,
        referenceDistance = 196,
        deviationDistance = 0,
        deviationCount = 0,
        osmWayCount = 1,
        osmDistance = 196,
        osmSegmentCount = 1,
        happy = true,
        osmSegments = Seq.empty,
        relation = Some(
          MonitorRouteRelation(
            relationId = 1,
            name = "route-name",
            role = None,
            survey = None,
            symbol = None,
            referenceTimestamp = None,
            referenceFilename = None,
            referenceDistance = 0,
            deviationDistance = 0,
            deviationCount = 0,
            osmWayCount = 1,
            osmSegmentCount = 1,
            osmDistance = 196,
            osmDistanceSubRelations = 196,
            gaps = None,
            happy = true,
            relations = Seq.empty
          )
        )
      )

      val state = MonitorRouteState(
        ObjectId(),
        routeId = route._id,
        relationId = 1,
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        wayCount = 1,
        startNodeId = None,
        endNodeId = None,
        osmDistance = 196,
        bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        osmSegments = Seq(
          MonitorRouteSegment(
            1,
            1001,
            1002,
            196,
            Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
            """{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
          )
        ),
        matchesGeometry = Some("""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4553911,51.4633666],[4.4562458,51.4618272]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""),
        deviations = Seq.empty,
        happy = true
      )

      val reference = MonitorRouteReference(
        ObjectId(),
        routeId = route._id,
        relationId = Some(1),
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        user = "user1",
        referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        referenceType = "osm",
        referenceTimestamp = Timestamp(2022, 8, 1),
        referenceDistance = 196,
        referenceSegmentCount = 1,
        referenceFilename = None,
        referenceGeoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}]}"""
      )

      configuration.monitorGroupRepository.saveGroup(group)
      configuration.monitorRouteRepository.saveRoute(route)
      configuration.monitorRouteRepository.saveRouteState(state)
      configuration.monitorRouteRepository.saveRouteReference(reference)

      Time.set(Timestamp(2022, 8, 12, 12, 0, 0))
      val reporter = new MonitorUpdateReporterMock()
      configuration.monitorRouteUpdateExecutor.execute(
        MonitorUpdateContext(
          "user2",
          reporter,
          MonitorRouteUpdate(
            action = "update",
            groupName = group.name,
            routeName = "route-name",
            referenceType = "osm",
            description = Some("description-changed"), // <-- changed
            comment = Some("comment-changed"), // <-- changed
            relationId = Some(1),
            referenceTimestamp = Some(Timestamp(2022, 8, 1)),
            newRouteName = Some("route-name-changed") // <-- changed
          )
        )
      )

      assertMessages(reporter)

      database.monitorRoutes.countDocuments(log) should equal(1)
      database.monitorRouteReferences.countDocuments(log) should equal(1)
      database.monitorRouteStates.countDocuments(log) should equal(1)

      val updatedRoute = configuration.monitorRouteRepository.routeByName(group._id, "route-name-changed").get
      val updatedState = configuration.monitorRouteRepository.routeState(route._id, 1).get
      val updatedReference = configuration.monitorRouteRepository.routeReference(route._id, Some(1)).get

      updatedRoute.shouldMatchTo(
        route.copy(
          name = "route-name-changed",
          description = "description-changed",
          comment = Some("comment-changed"),
          user = "user2",
          timestamp = Timestamp(2022, 8, 12, 12, 0, 0)
        )
      )

      updatedState should equal(state) // no change
      updatedReference should equal(reference) // no change
    }
  }

  private def assertMessages(reporter: MonitorUpdateReporterMock): Unit = {
    // not analyzed, no errors
    reporter.messages.shouldMatchTo(
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
}
