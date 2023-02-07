package kpn.server.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.custom.Day
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest11 extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

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
        referenceType = "osm",
        referenceDay = Some(Day(2022, 8, 1)),
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
            referenceDay = None,
            referenceFilename = None,
            referenceDistance = 0,
            deviationDistance = 0,
            deviationCount = 0,
            osmWayCount = 1,
            osmDistance = 196,
            osmSegmentCount = 1,
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
        relationId = 1,
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        user = "user1",
        bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        referenceType = "osm",
        referenceDay = Day(2022, 8, 1),
        distance = 196,
        segmentCount = 1,
        filename = None,
        geoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}]}"""
      )

      configuration.monitorGroupRepository.saveGroup(group)
      configuration.monitorRouteRepository.saveRoute(route)
      configuration.monitorRouteRepository.saveRouteState(state)
      configuration.monitorRouteRepository.saveRouteReference(reference)

      val properties = MonitorRouteProperties(
        groupName = group.name,
        name = "route-name-changed", // <-- changed
        description = "description-changed", // <-- changed
        comment = Some("comment-changed"), // <-- changed
        relationId = Some(1),
        referenceType = "osm",
        referenceDay = Some(Day(2022, 8, Some(1))),
        referenceFileChanged = false,
        referenceFilename = None
      )

      Time.set(Timestamp(2022, 8, 12, 12, 0, 0))
      val saveResult = configuration.monitorUpdater.update("user2", group.name, route.name, properties)
      saveResult should equal(MonitorRouteSaveResult()) // not analyzed, no errors

      val updatedRoute = configuration.monitorRouteRepository.routeByName(group._id, "route-name-changed").get
      val updatedState = configuration.monitorRouteRepository.routeState(route._id, 1).get
      val updatedReference = configuration.monitorRouteRepository.routeRelationReference(route._id, 1).get

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
}
