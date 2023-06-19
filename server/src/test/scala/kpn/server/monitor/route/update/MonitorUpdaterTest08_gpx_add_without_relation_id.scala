package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteOsmSegment
import kpn.server.monitor.domain.MonitorRouteOsmSegmentElement
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest08_gpx_add_without_relation_id extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("add non-super route with single gpx reference, but initially with relationId unknown") {

    withDatabase() { database =>

      val configuration = MonitorUpdaterTestSupport.configuration(database)
      setupLoadStructure(configuration)
      setupLoadTopLevel(configuration)

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

      val update = MonitorRouteUpdate(
        action = "add",
        groupName = group.name,
        routeName = "route-name",
        referenceType = "gpx",
        description = Some("route-description"),
        comment = Some("route-comment"),
        relationId = None, // <-- no relationId yet
        referenceTimestamp = Some(Timestamp(2022, 8, 1)),
        referenceFilename = Some("filename"),
        referenceGpx = Some(gpx)
      )

      Time.set(Timestamp(2022, 8, 11, 12, 0, 0))
      val reporter = new MonitorUpdateReporterMock()
      configuration.monitorRouteUpdateExecutor.execute(
        MonitorUpdateContext(
          "user1",
          reporter,
          update
        )
      )

      reporter.messages.shouldMatchTo(
        Seq(
          MonitorRouteUpdateStatusMessage(commands = Seq(MonitorRouteUpdateStatusCommand("step-add", "prepare"),
            MonitorRouteUpdateStatusCommand("step-add", "analyze-route-structure"),
            MonitorRouteUpdateStatusCommand("step-active", "prepare"))),
          MonitorRouteUpdateStatusMessage(commands = Seq(MonitorRouteUpdateStatusCommand("step-active", "analyze-route-structure"))),
          MonitorRouteUpdateStatusMessage(commands = Seq(MonitorRouteUpdateStatusCommand("step-add", "load-gpx"),
            MonitorRouteUpdateStatusCommand("step-add", "analyze"),
            MonitorRouteUpdateStatusCommand("step-active", "load-gpx"))),
          MonitorRouteUpdateStatusMessage(commands = Seq(MonitorRouteUpdateStatusCommand("step-active", "analyze"))),
          MonitorRouteUpdateStatusMessage(commands = Seq(MonitorRouteUpdateStatusCommand("step-active", "save"))),
          MonitorRouteUpdateStatusMessage(commands = Seq(MonitorRouteUpdateStatusCommand("step-done", "save"))))
      )

      val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
      route.shouldMatchTo(
        MonitorRoute(
          route._id,
          groupId = group._id,
          name = "route-name",
          description = "route-description",
          comment = Some("route-comment"),
          relationId = None, // <-- no relationId yet
          user = "user1",
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          referenceType = "gpx",
          referenceTimestamp = Some(Timestamp(2022, 8, 1)),
          referenceFilename = Some("filename"),
          referenceDistance = 196,
          deviationDistance = 0,
          deviationCount = 0,
          osmWayCount = 0,
          osmDistance = 0,
          osmSegmentCount = 0,
          happy = false,
          osmSegments = Seq.empty,
          relation = None
        )
      )

      configuration.monitorRouteRepository.routeRelationReference(route._id, 1) should equal(None)
      configuration.monitorRouteRepository.routeState(route._id, 1) should equal(None)


      Time.set(Timestamp(2022, 8, 12, 12, 0, 0))

      val update2 = update.copy(
        action = "update",
        relationId = Some(1),
        referenceGpx = None
      )

      val reporter2 = new MonitorUpdateReporterMock()
      configuration.monitorRouteUpdateExecutor.execute(
        MonitorUpdateContext(
          "user2",
          reporter2,
          update2
        )
      )
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
              MonitorRouteUpdateStatusCommand("step-add", "load-gpx"),
              MonitorRouteUpdateStatusCommand("step-add", "analyze"),
              MonitorRouteUpdateStatusCommand("step-active", "load-gpx")
            )
          ),
          MonitorRouteUpdateStatusMessage(
            commands = Seq(
              MonitorRouteUpdateStatusCommand("step-active", "analyze")
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

      configuration.monitorRouteRepository.routeByName(group._id, "route-name").shouldMatchTo(
        Some(
          route.copy(
            relationId = Some(1),
            user = "user2",
            timestamp = Timestamp(2022, 8, 12, 12, 0, 0),
            referenceTimestamp = Some(Timestamp(2022, 8, 1)),
            referenceFilename = Some("filename"),
            referenceDistance = 196,
            deviationDistance = 0,
            deviationCount = 0,
            osmWayCount = 1,
            osmDistance = 196,
            osmSegmentCount = 1,
            osmSegments = Seq(
              MonitorRouteOsmSegment(
                Seq(
                  MonitorRouteOsmSegmentElement(
                    relationId = 1,
                    segmentId = 1,
                    meters = 196,
                    bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
                    reversed = false
                  )
                )
              )
            ),
            relation = Some(

              MonitorRouteRelation(
                relationId = 1,
                name = "route-name",
                role = None,
                survey = None,
                referenceTimestamp = None,
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
            ),
            happy = true
          )
        )
      )

      val reference = configuration.monitorRouteRepository.routeRelationReference(route._id, 1).get
      reference.shouldMatchTo(
        MonitorRouteReference(
          reference._id,
          routeId = route._id,
          relationId = Some(1), // <-- filled in
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0), // <-- date that reference was added, not latest change by "user2"
          user = "user1", // <-- not "user2" who provided the relationId, the reference was still added by "user1"
          referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
          referenceType = "gpx",
          referenceTimestamp = Timestamp(2022, 8, 1),
          referenceDistance = 196,
          referenceSegmentCount = 1,
          referenceFilename = Some("filename"),
          referenceGeoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
        )
      )

      val state = configuration.monitorRouteRepository.routeState(route._id, 1).get
      state.shouldMatchTo(
        MonitorRouteState(
          state._id,
          routeId = route._id,
          relationId = 1,
          timestamp = Timestamp(2022, 8, 12, 12, 0, 0),
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
      )
    }
  }

  private def setupLoadStructure(configuration: MonitorUpdaterConfiguration): Unit = {
    val overpassData = OverpassData()
      .relation(
        1,
        tags = Tags.from(
          "name" -> "route-name"
        )
      )
    setupRouteStructure(configuration, overpassData, 1)
  }

  private def setupLoadTopLevel(configuration: MonitorUpdaterConfiguration): Unit = {

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
          newMember("way", 101),
        )
      )

    val relation = new DataBuilder(overpassData.rawData).data.relations(1)
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 1).returns(Some(relation))
  }
}
