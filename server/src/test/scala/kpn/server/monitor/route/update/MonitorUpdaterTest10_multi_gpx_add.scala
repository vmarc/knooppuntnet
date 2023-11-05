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
import kpn.core.util.MockLog
import kpn.core.util.UnitTest
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteOsmSegment
import kpn.server.monitor.domain.MonitorRouteOsmSegmentElement
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest10_multi_gpx_add extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  private val log = new MockLog()

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("add route with gpx references per sub-relation") {

    withDatabase() { database =>

      val configuration = MonitorUpdaterTestSupport.configuration(database)
      setupLoadStructure(configuration)
      setupLoadTopLevel(configuration)

      val group = newMonitorGroup("group")
      configuration.monitorGroupRepository.saveGroup(group)

      val routeAdd = MonitorRouteUpdate(
        action = "add",
        groupName = group.name,
        routeName = "route-name",
        referenceType = "multi-gpx",
        description = Some("route-description"),
        comment = Some("route-comment"),
        relationId = Some(1),
      )

      Time.set(Timestamp(2022, 8, 11, 12, 0, 0))
      val routeAddReporter = new MonitorUpdateReporterMock()
      configuration.monitorRouteUpdateExecutor.execute(
        MonitorUpdateContext(
          "user1",
          routeAddReporter,
          routeAdd
        )
      )

      assertRouteAddMessages(routeAddReporter.messages)

      database.monitorRoutes.countDocuments(log) should equal(1)
      database.monitorRouteReferences.countDocuments(log) should equal(0)
      database.monitorRouteStates.countDocuments(log) should equal(2)

      val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
      route.copy(analysisDuration = None).shouldMatchTo(
        MonitorRoute(
          _id = route._id,
          groupId = group._id,
          name = "route-name",
          description = "route-description",
          comment = Some("route-comment"),
          relationId = Some(1),
          user = "user1",
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          symbol = None,
          analysisTimestamp = Some(Timestamp(2022, 8, 11, 12, 0, 0)),
          analysisDuration = None,
          referenceType = "multi-gpx",
          referenceTimestamp = None,
          referenceDistance = 0,
          referenceFilename = None,
          deviationDistance = 0,
          deviationCount = 0,
          osmWayCount = 2,
          osmDistance = 274,
          osmSegmentCount = 1,
          osmSegments = Seq(
            MonitorRouteOsmSegment(
              Seq(
                MonitorRouteOsmSegmentElement(
                  relationId = 11,
                  segmentId = 1,
                  meters = 181,
                  bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
                  reversed = false
                ),
                MonitorRouteOsmSegmentElement(
                  relationId = 12,
                  segmentId = 1,
                  meters = 93,
                  bounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
                  reversed = false
                )
              )
            )
          ),
          relation = Some(
            MonitorRouteRelation(
              relationId = 1,
              name = "main-relation",
              role = None,
              survey = None,
              symbol = None,
              referenceTimestamp = None,
              referenceFilename = None,
              referenceDistance = 0,
              deviationDistance = 0,
              deviationCount = 0,
              osmWayCount = 0,
              osmSegmentCount = 0,
              osmDistance = 0,
              osmDistanceSubRelations = 274,
              gaps = None,
              happy = false,
              relations = Seq(
                MonitorRouteRelation(
                  relationId = 11,
                  name = "sub-relation-1",
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
                  osmDistance = 181,
                  osmDistanceSubRelations = 0,
                  gaps = Some("start"),
                  happy = false,
                  relations = Seq.empty
                ),
                MonitorRouteRelation(
                  relationId = 12,
                  name = "sub-relation-2",
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
                  osmDistance = 93,
                  osmDistanceSubRelations = 0,
                  gaps = Some("end"),
                  happy = false,
                  relations = Seq.empty
                )
              )
            )
          ),
          happy = false,
        )
      )

      configuration.monitorRouteRepository.routeReference(route._id, Some(1)) should equal(None)
      configuration.monitorRouteRepository.routeReference(route._id, Some(11)) should equal(None)
      configuration.monitorRouteRepository.routeReference(route._id, Some(12)) should equal(None)

      configuration.monitorRouteRepository.routeState(route._id, 1) should equal(None)
      val state11 = configuration.monitorRouteRepository.routeState(route._id, 11).get
      state11.shouldMatchTo(
        MonitorRouteState(
          state11._id,
          routeId = route._id,
          relationId = 11,
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          wayCount = 1,
          startNodeId = Some(1001),
          endNodeId = Some(1002),
          osmDistance = 181,
          bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
          osmSegments = Seq(
            MonitorRouteSegment(
              id = 1,
              startNodeId = 1001,
              endNodeId = 1002,
              meters = 181,
              bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
              geoJson = """{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
            ),
          ),
          matchesGeometry = None,
          deviations = Seq.empty,
          happy = false
        )
      )

      val state12 = configuration.monitorRouteRepository.routeState(route._id, 12).get
      state12.shouldMatchTo(
        MonitorRouteState(
          state12._id,
          routeId = route._id,
          relationId = 12,
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          wayCount = 1,
          startNodeId = Some(1002),
          endNodeId = Some(1003),
          osmDistance = 93,
          bounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
          osmSegments = Seq(
            MonitorRouteSegment(
              id = 1,
              startNodeId = 1002,
              endNodeId = 1003,
              meters = 93,
              bounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
              geoJson = """{"type":"LineString","coordinates":[[4.4562458,51.4618272],[4.455056,51.4614496]],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
            ),
          ),
          matchesGeometry = None,
          deviations = Seq.empty,
          happy = false
        )
      )

      val gpx1 =
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

      Time.set(Timestamp(2022, 8, 12, 12, 0, 0))
      val uploadGpx1 = MonitorRouteUpdate(
        action = "gpx-upload",
        groupName = group.name,
        routeName = "route-name",
        referenceType = "multi-gpx",
        relationId = Some(11),
        referenceTimestamp = Some(Timestamp(2022, 8, 1, 0, 0, 0)),
        referenceFilename = Some("filename-1"),
        referenceGpx = Some(gpx1)
      )

      val uploadGpxReporter1 = new MonitorUpdateReporterMock()
      configuration.monitorRouteUpdateExecutor.execute(
        MonitorUpdateContext(
          "user2",
          uploadGpxReporter1,
          uploadGpx1
        )
      )

      assertUploadGpx1Messages(uploadGpxReporter1.messages)

      database.monitorRoutes.countDocuments(log) should equal(1)
      database.monitorRouteReferences.countDocuments(log) should equal(1)
      database.monitorRouteStates.countDocuments(log) should equal(2)

      val routeUpdated1 = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
      routeUpdated1.copy(analysisDuration = None).shouldMatchTo(
        route.copy(
          analysisTimestamp = Some(Timestamp(2022, 8, 12, 12, 0, 0)),
          analysisDuration = None,
          relation = route.relation.map { relation =>
            relation.copy(
              relations = Seq(
                relation.relations.head.copy(
                  referenceTimestamp = Some(Timestamp(2022, 8, 1)),
                  referenceFilename = Some("filename-1"),
                  referenceDistance = 181,
                  osmWayCount = 1,
                  osmDistance = 181,
                  osmSegmentCount = 1,
                  happy = true,
                ),
                relation.relations(1)
              )
            )
          },
          happy = false,
        )
      )

      val reference11 = configuration.monitorRouteRepository.routeReference(route._id, Some(11)).get
      reference11.shouldMatchTo(
        MonitorRouteReference(
          reference11._id,
          routeId = route._id,
          relationId = Some(11),
          timestamp = Timestamp(2022, 8, 12, 12, 0, 0),
          user = "user2",
          referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
          referenceType = "gpx", // the route reference type is "multi-gpx", but the invidual reference is "gpx"
          referenceTimestamp = Timestamp(2022, 8, 1),
          referenceDistance = 181,
          referenceSegmentCount = 1,
          referenceFilename = Some("filename-1"),
          referenceGeoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
        )
      )

      configuration.monitorRouteRepository.routeReference(route._id, Some(1)) should equal(None)
      configuration.monitorRouteRepository.routeReference(route._id, Some(12)) should equal(None)

      val updatedState11 = configuration.monitorRouteRepository.routeState(route._id, 11).get
      updatedState11.shouldMatchTo(
        state11.copy(
          timestamp = Timestamp(2022, 8, 12, 12, 0, 0),
          matchesGeometry = Some("""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4553911,51.4633666],[4.4562458,51.4618272]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""),
          happy = true,
        )
      )

      configuration.monitorRouteRepository.routeState(route._id, 12).get.shouldMatchTo(state12)

      val gpx2 =
        """
          |<gpx>
          |  <trk>
          |    <trkseg>
          |      <trkpt lat="51.4618272" lon="4.4562458"></trkpt>
          |      <trkpt lat="51.4614496" lon="4.4550560"></trkpt>
          |    </trkseg>
          |  </trk>
          |</gpx>
          |""".stripMargin

      Time.set(Timestamp(2022, 8, 13, 12, 0, 0))

      val uploadGpx2 = MonitorRouteUpdate(
        action = "gpx-upload",
        groupName = group.name,
        routeName = "route-name",
        referenceType = "multi-gpx",
        relationId = Some(12),
        referenceTimestamp = Some(Timestamp(2022, 8, 2, 0, 0, 0)),
        referenceFilename = Some("filename-2"),
        referenceGpx = Some(gpx2)
      )

      val uploadGpxReporter2 = new MonitorUpdateReporterMock()
      configuration.monitorRouteUpdateExecutor.execute(
        MonitorUpdateContext(
          "user3",
          uploadGpxReporter2,
          uploadGpx2
        )
      )

      assertUploadGpx2Messages(uploadGpxReporter2.messages)

      database.monitorRoutes.countDocuments(log) should equal(1)
      database.monitorRouteReferences.countDocuments(log) should equal(2)
      database.monitorRouteStates.countDocuments(log) should equal(2)

      val routeUpdated2 = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
      routeUpdated2.copy(analysisDuration = None).shouldMatchTo(
        route.copy(
          analysisTimestamp = Some(Timestamp(2022, 8, 13, 12, 0, 0)),
          analysisDuration = None,
          relation = route.relation.map { relation =>
            relation.copy(
              happy = true,
              relations = Seq(
                relation.relations.head.copy(
                  referenceTimestamp = Some(Timestamp(2022, 8, 1)),
                  referenceFilename = Some("filename-1"),
                  referenceDistance = 181,
                  osmWayCount = 1,
                  osmDistance = 181,
                  osmSegmentCount = 1,
                  happy = true,
                ),
                relation.relations(1).copy(
                  referenceTimestamp = Some(Timestamp(2022, 8, 2)),
                  referenceFilename = Some("filename-2"),
                  referenceDistance = 93,
                  osmWayCount = 1,
                  osmDistance = 93,
                  osmSegmentCount = 1,
                  happy = true,
                )
              )
            )
          },
          happy = true
        )
      )

      configuration.monitorRouteRepository.routeReference(route._id, Some(1)) should equal(None)
      configuration.monitorRouteRepository.routeReference(route._id, Some(11)).shouldMatchTo(Some(reference11))

      val reference2 = configuration.monitorRouteRepository.routeReference(route._id, Some(12)).get
      reference2.shouldMatchTo(
        MonitorRouteReference(
          reference2._id,
          routeId = route._id,
          relationId = Some(12),
          timestamp = Timestamp(2022, 8, 13, 12, 0, 0),
          user = "user3",
          referenceBounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
          referenceType = "gpx", // the route reference type is "multi-gpx", but the invidual reference is "gpx"
          referenceTimestamp = Timestamp(2022, 8, 2),
          referenceDistance = 93,
          referenceSegmentCount = 1,
          referenceFilename = Some("filename-2"),
          referenceGeoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4562458,51.4618272],[4.455056,51.4614496]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
        )
      )

      val updatedState12 = configuration.monitorRouteRepository.routeState(route._id, 12).get
      updatedState12.shouldMatchTo(
        state12.copy(
          timestamp = Timestamp(2022, 8, 13, 12, 0, 0),
          matchesGeometry = Some("""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4562458,51.4618272],[4.455056,51.4614496]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""),
          happy = true,
        )
      )
    }
  }

  private def setupLoadStructure(configuration: MonitorUpdaterConfiguration): Unit = {

    val overpassData = OverpassData()
      .relation(
        1,
        tags = Tags.from(
          "name" -> "main-relation"
        ),
        members = Seq(
          newMember("relation", 11),
          newMember("relation", 12)
        )
      )
      .relation(
        11,
        tags = Tags.from(
          "name" -> "sub-relation-1"
        ),
      )
      .relation(
        12,
        tags = Tags.from(
          "name" -> "sub-relation-2"
        ),
      )

    setupRouteStructure(configuration, overpassData, 1)
  }

  private def setupLoadTopLevel(configuration: MonitorUpdaterConfiguration): Unit = {

    val overpassData = OverpassData()
      .node(1001, latitude = "51.4633666", longitude = "4.4553911")
      .node(1002, latitude = "51.4618272", longitude = "4.4562458")
      .node(1003, latitude = "51.4614496", longitude = "4.4550560")
      .way(101, 1001, 1002)
      .way(102, 1002, 1003)
      .relation(
        1,
        tags = Tags.from(
          "name" -> "main-relation"
        ),
      )
      .relation(
        11,
        tags = Tags.from(
          "name" -> "sub-relation-1"
        ),
        members = Seq(
          newMember("way", 101),
        )
      )
      .relation(
        12,
        tags = Tags.from(
          "name" -> "sub-relation-2"
        ),
        members = Seq(
          newMember("way", 102),
        )
      )

    val data = new DataBuilder(overpassData.rawData).data
    val mainRelation = data.relations(1)
    val subRelation1 = data.relations(11)
    val subRelation2 = data.relations(12)

    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 1).returns(Some(mainRelation))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 11).returns(Some(subRelation1))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 12).returns(Some(subRelation2))
  }

  private def assertRouteAddMessages(messages: Seq[MonitorRouteUpdateStatusMessage]): Unit = {
    messages.shouldMatchTo(
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
            MonitorRouteUpdateStatusCommand("step-add", "11", Some("1/3 sub-relation-1")),
            MonitorRouteUpdateStatusCommand("step-add", "12", Some("2/3 sub-relation-2")),
            MonitorRouteUpdateStatusCommand("step-add", "1", Some("3/3 main-relation")),
            MonitorRouteUpdateStatusCommand("step-add", "save")
          )
        ),
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-active", "11")
          )
        ),
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-active", "12")
          )
        ),
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-active", "1")
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

  private def assertUploadGpx1Messages(messages: Seq[MonitorRouteUpdateStatusMessage]): Unit = {
    messages.shouldMatchTo(
      Seq(
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-add", "upload"),
            MonitorRouteUpdateStatusCommand("step-add", "save"),
            MonitorRouteUpdateStatusCommand("step-active", "upload")
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

  private def assertUploadGpx2Messages(messages: Seq[MonitorRouteUpdateStatusMessage]): Unit = {
    messages.shouldMatchTo(
      Seq(
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-add", "upload"),
            MonitorRouteUpdateStatusCommand("step-add", "save"),
            MonitorRouteUpdateStatusCommand("step-active", "upload"),
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
