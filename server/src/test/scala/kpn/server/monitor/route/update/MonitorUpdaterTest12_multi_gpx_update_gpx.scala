package kpn.server.monitor.route.update

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteSegment
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

class MonitorUpdaterTest12_multi_gpx_update_gpx extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  private val log = new MockLog()

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("gpx reference per subrelation - update subrelation gpx reference") {

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
      configuration.monitorRouteUpdateExecutor.execute(
        MonitorUpdateContext(
          "user1",
          new MonitorUpdateReporterMock(),
          routeAdd
        )
      )

      database.monitorRoutes.countDocuments(log) should equal(1)
      database.monitorRouteReferences.countDocuments(log) should equal(0)
      database.monitorRouteStates.countDocuments(log) should equal(1)

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
          osmWayCount = 1,
          osmDistance = 181,
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
              osmDistanceSubRelations = 181,
              gaps = None,
              happy = false,
              relations = Seq(
                MonitorRouteRelation(
                  relationId = 11,
                  name = "sub-relation",
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
                  gaps = Some("start-end"),
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

      Time.set(Timestamp(2022, 8, 12, 12, 0, 0))
      val uploadGpx1 = MonitorRouteUpdate(
        action = "gpx-upload",
        groupName = group.name,
        routeName = "route-name",
        referenceType = "multi-gpx",
        relationId = Some(11),
        referenceTimestamp = Some(Timestamp(2022, 8, 1, 0, 0, 0)),
        referenceFilename = Some("filename-1"),
        referenceGpx = Some(gpx)
      )

      val uploadGpxReporter1 = new MonitorUpdateReporterMock()
      configuration.monitorRouteUpdateExecutor.execute(
        MonitorUpdateContext(
          "user2",
          uploadGpxReporter1,
          uploadGpx1
        )
      )

      database.monitorRoutes.countDocuments(log) should equal(1)
      database.monitorRouteReferences.countDocuments(log) should equal(1)
      database.monitorRouteStates.countDocuments(log) should equal(1)

      val routeUpdated1 = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
      routeUpdated1.copy(analysisDuration = None).shouldMatchTo(
        route.copy(
          analysisTimestamp = Some(Timestamp(2022, 8, 12, 12, 0, 0)),
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
                )
              )
            )
          },
          happy = true
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

      val updatedState11 = configuration.monitorRouteRepository.routeState(route._id, 11).get
      updatedState11.shouldMatchTo(
        state11.copy(
          timestamp = Timestamp(2022, 8, 12, 12, 0, 0),
          matchesGeometry = Some("""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4553911,51.4633666],[4.4562458,51.4618272]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""),
          happy = true,
        )
      )

      Time.set(Timestamp(2022, 8, 13, 12, 0, 0))

      val uploadGpx2 = MonitorRouteUpdate(
        action = "gpx-upload",
        groupName = group.name,
        routeName = "route-name",
        referenceType = "multi-gpx",
        relationId = Some(11),
        referenceTimestamp = Some(Timestamp(2022, 8, 2, 0, 0, 0)),
        referenceFilename = Some("filename-2"),
        referenceGpx = Some(gpx)
      )

      val uploadGpxReporter2 = new MonitorUpdateReporterMock()
      configuration.monitorRouteUpdateExecutor.execute(
        MonitorUpdateContext(
          "user3",
          uploadGpxReporter2,
          uploadGpx2
        )
      )

      database.monitorRoutes.countDocuments(log) should equal(1)
      database.monitorRouteReferences.countDocuments(log) should equal(1)
      database.monitorRouteStates.countDocuments(log) should equal(1)

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
                  referenceTimestamp = Some(Timestamp(2022, 8, 2)),
                  referenceFilename = Some("filename-2"),
                  referenceDistance = 181,
                  osmWayCount = 1,
                  osmDistance = 181,
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
      configuration.monitorRouteRepository.routeReference(route._id, Some(11)).shouldMatchTo(
        Some(
          reference11.copy(
            timestamp = Timestamp(2022, 8, 13, 12, 0, 0),
            user = "user3",
            referenceTimestamp = Timestamp(2022, 8, 2, 0, 0, 0),
            referenceFilename = Some("filename-2")
          )
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
        )
      )
      .relation(
        11,
        tags = Tags.from(
          "name" -> "sub-relation"
        ),
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
          "name" -> "main-relation"
        ),
      )
      .relation(
        11,
        tags = Tags.from(
          "name" -> "sub-relation"
        ),
        members = Seq(
          newMember("way", 101),
        )
      )

    val data = new DataBuilder(overpassData.rawData).data
    val mainRelation = data.relations(1)
    val subRelation = data.relations(11)

    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 1).returns(Some(mainRelation))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 11).returns(Some(subRelation))
  }
}
