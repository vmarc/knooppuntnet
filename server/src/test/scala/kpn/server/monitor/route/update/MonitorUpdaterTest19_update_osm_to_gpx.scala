package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.api.common.data.MemberType
import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.test.OverpassData
import kpn.core.test.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.MockLog
import kpn.core.util.UnitTest
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest19_update_osm_to_gpx extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  private val log = new MockLog()

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("add route with osm reference, and change to reference type gpx afterwards") {

    withDatabase() { database =>

      val configuration = MonitorUpdaterTestSupport.configuration(database)
      setupLoadStructure(configuration)
      setupLoadRelation(configuration)

      val group = newMonitorGroup("group")
      configuration.monitorGroupRepository.saveGroup(group)

      Time.set(Timestamp(2022, 8, 11, 12, 0, 0))
      val reporter = new MonitorUpdateReporterMock()
      configuration.monitorRouteUpdateExecutor.execute(
        MonitorUpdateContext(
          "user1",
          reporter,
          MonitorRouteUpdate(
            action = MonitorAction.add,
            groupName = group.name,
            routeName = "route-name",
            referenceType = MonitorReferenceType.osm,
            description = Some("route-description"),
            comment = Some("route-comment"),
            relationId = Some(1),
            referenceTimestamp = Some(Timestamp(2022, 8, 1)),
          )
        )
      )

      database.monitorRoutes.countDocuments(log) should equal(1)
      database.monitorRouteReferences.countDocuments(log) should equal(1)
      database.monitorRouteStates.countDocuments(log) should equal(1)

      val addedRoute = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
      assertEqual(
        addedRoute.copy(analysisDuration = None),
        MonitorRoute(
          addedRoute._id,
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
          referenceType = MonitorReferenceType.osm,
          referenceTimestamp = Some(Timestamp(2022, 8, 1)),
          referenceFilename = None,
          referenceDistance = 181,
          deviationDistance = 0,
          deviationCount = 0,
          osmSegmentCount = 1,
          relation = Some(
            newMonitorRouteRelation(
              relationId = 1,
              name = "route-name",
              happy = true,
            )
          ),
          happy = true
        )
      )

      val addedReference = configuration.monitorRouteRepository.routeReference(addedRoute._id, Some(1)).get
      assertEqual(
        addedReference,
        MonitorRouteReference(
          addedReference._id,
          routeId = addedRoute._id,
          relationId = Some(1),
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          user = "user1",
          referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
          referenceType = MonitorReferenceType.osm,
          referenceTimestamp = Timestamp(2022, 8, 1),
          referenceDistance = 181,
          referenceSegmentCount = 1,
          referenceFilename = None,
          referenceGeoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}]}"""
        )
      )

      val addedState = configuration.monitorRouteRepository.routeState(addedRoute._id, 1).get
      assertEqual(
        addedState,
        MonitorRouteState(
          addedState._id,
          routeId = addedRoute._id,
          relationId = 1,
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
          matchesGeometry = Some("""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4553911,51.4633666],[4.4562458,51.4618272]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""),
          deviations = Seq.empty,
        )
      )

      Time.set(Timestamp(2022, 8, 12, 12, 0, 0))

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

      configuration.monitorRouteUpdateExecutor.execute(
        MonitorUpdateContext(
          "user2",
          reporter,
          MonitorRouteUpdate(
            action = MonitorAction.update,
            groupName = group.name,
            routeName = "route-name",
            referenceType = MonitorReferenceType.gpx,
            referenceTimestamp = Some(Timestamp(2022, 8, 1)),
            description = Some("route-description"),
            comment = Some("route-comment"),
            relationId = Some(1),
            referenceFilename = Some("filename"),
            referenceGpx = Some(gpx)
          )
        )
      )

      database.monitorRoutes.countDocuments(log) should equal(1)
      database.monitorRouteReferences.countDocuments(log) should equal(1)
      database.monitorRouteStates.countDocuments(log) should equal(1)

      val updatedRoute = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
      assertEqual(
        updatedRoute.copy(analysisDuration = None),
        MonitorRoute(
          addedRoute._id,
          groupId = group._id,
          name = "route-name",
          description = "route-description",
          comment = Some("route-comment"),
          relationId = Some(1),
          user = "user2",
          timestamp = Timestamp(2022, 8, 12, 12, 0, 0),
          symbol = None,
          analysisTimestamp = Some(Timestamp(2022, 8, 12, 12, 0, 0)),
          analysisDuration = None,
          referenceType = MonitorReferenceType.gpx,
          referenceTimestamp = Some(Timestamp(2022, 8, 1, 0, 0, 0)),
          referenceFilename = Some("filename"),
          referenceDistance = 181,
          deviationDistance = 0,
          deviationCount = 0,
          osmSegmentCount = 1,
          relation = Some(
            newMonitorRouteRelation(
              relationId = 1,
              name = "route-name",
              happy = true,
            )
          ),
          happy = true
        )
      )

      val updatedReference = configuration.monitorRouteRepository.routeReference(updatedRoute._id, Some(1)).get
      assertEqual(
        updatedReference,
        MonitorRouteReference(
          addedReference._id,
          routeId = addedRoute._id,
          relationId = Some(1),
          timestamp = Timestamp(2022, 8, 12, 12, 0, 0),
          user = "user2",
          referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
          referenceType = MonitorReferenceType.gpx,
          referenceTimestamp = Timestamp(2022, 8, 1, 0, 0, 0),
          referenceDistance = 181,
          referenceSegmentCount = 1,
          referenceFilename = Some("filename"),
          referenceGeoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
        )
      )

      val updatedState = configuration.monitorRouteRepository.routeState(updatedRoute._id, 1).get
      assertEqual(
        updatedState,
        MonitorRouteState(
          addedState._id,
          routeId = addedRoute._id,
          relationId = 1,
          timestamp = Timestamp(2022, 8, 12, 12, 0, 0),
          // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
          matchesGeometry = Some("""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4553911,51.4633666],[4.4562458,51.4618272]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""),
          deviations = Seq.empty,
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

  private def setupLoadRelation(configuration: MonitorUpdaterConfiguration): Unit = {

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
          newMember(MemberType.Way, 101),
        )
      )

    val relation = new DataBuilder(overpassData.rawData).data.relations(1)
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 1).returns(Some(relation))
    (configuration.monitorRouteRelationRepository.load _).when(None, 1).returns(Some(relation))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(
      Some(Timestamp(2022, 8, 1, 0, 0, 0))
      , 1
    ).returns(Some(relation))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(
      Some(Timestamp(2022, 8, 12, 12, 0, 0))
      , 1
    ).returns(Some(relation))
  }
}
