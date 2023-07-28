package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.common.monitor.MonitorRouteUpdate
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

class MonitorUpdaterTest02_osm_add_without_relation_id extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("add route with osm reference, initially with relationId unknown") {

    withDatabase() { database =>

      val configuration = MonitorUpdaterTestSupport.configuration(database)

      val group = newMonitorGroup("group")
      configuration.monitorGroupRepository.saveGroup(group)

      Time.set(Timestamp(2022, 8, 11, 12, 0, 0))

      val update = MonitorRouteUpdate(
        action = "add",
        groupName = group.name,
        routeName = "route-name",
        referenceType = "osm",
        description = Some(""),
        referenceTimestamp = Some(Timestamp(2022, 8, 1)),
      )

      val reporter = new MonitorUpdateReporterMock()
      configuration.monitorRouteUpdateExecutor.execute(
        MonitorUpdateContext(
          "user",
          reporter,
          update
        )
      )

      // TODO replace: saveResult should equal(MonitorRouteSaveResult())

      val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
      route.shouldMatchTo(
        MonitorRoute(
          _id = route._id,
          groupId = group._id,
          name = "route-name",
          description = "",
          comment = None,
          relationId = None, // no relationId yet
          user = "user",
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          symbol = None,
          referenceType = "osm",
          referenceTimestamp = Some(Timestamp(2022, 8, 1)),
          referenceFilename = None,
          referenceDistance = 0,
          deviationDistance = 0,
          deviationCount = 0,
          osmWayCount = 0,
          osmDistance = 0,
          osmSegmentCount = 0,
          happy = false,
          osmSegments = Seq.empty,
          relation = None // route structure not known yet
        )
      )

      configuration.monitorRouteRepository.routeReference(route._id, Some(1)) should equal(None)
      configuration.monitorRouteRepository.routeState(route._id, 1) should equal(None)

      setupLoadStructure(configuration)
      setupLoadTopLevel(configuration)

      val updatedUpdate = update.copy(
        action = "update",
        relationId = Some(1)
      )

      Time.set(Timestamp(2022, 8, 12, 12, 0, 0))

      val reporter2 = new MonitorUpdateReporterMock()
      configuration.monitorRouteUpdateExecutor.execute(
        MonitorUpdateContext(
          "user",
          reporter,
          updatedUpdate
        )
      )

      //      updateSaveResult should equal (
      //        MonitorRouteSaveResult(
      //          analyzed = true,
      //        )
      //        )

      configuration.monitorRouteRepository.routeByName(group._id, "route-name").shouldMatchTo(
        Some(
          MonitorRoute(
            route._id,
            groupId = group._id,
            name = "route-name",
            description = "",
            comment = None,
            relationId = Some(1), // relationId filled in
            user = "user",
            timestamp = Timestamp(2022, 8, 12, 12, 0, 0),
            symbol = None,
            referenceType = "osm",
            referenceTimestamp = Some(Timestamp(2022, 8, 1)),
            referenceFilename = None,
            referenceDistance = 181,
            deviationDistance = 0,
            deviationCount = 0,
            osmWayCount = 1,
            osmDistance = 181,
            osmSegmentCount = 1,
            happy = true,
            osmSegments = Seq(
              MonitorRouteOsmSegment(
                Seq(
                  MonitorRouteOsmSegmentElement(
                    relationId = 1,
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
                osmDistance = 181,
                osmDistanceSubRelations = 0,
                gaps = Some("start-end"),
                happy = true,
                relations = Seq.empty
              )
            )
          )
        )
      )

      val reference = configuration.monitorRouteRepository.routeReference(route._id, Some(1)).get
      reference.shouldMatchTo(
        MonitorRouteReference(
          reference._id,
          routeId = route._id,
          relationId = Some(1),
          timestamp = Timestamp(2022, 8, 12, 12, 0, 0),
          user = "user",
          referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
          referenceType = "osm",
          referenceTimestamp = Timestamp(2022, 8, 1),
          referenceDistance = 181,
          referenceSegmentCount = 1,
          referenceFilename = None,
          referenceGeoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}]}"""
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
          startNodeId = Some(1001),
          endNodeId = Some(1002),
          osmDistance = 181,
          bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
          osmSegments = Seq(
            MonitorRouteSegment(
              1,
              1001,
              1002,
              181,
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
          "name" -> "route-name"
        ),
        members = Seq(
          newMember("way", 101),
        )
      )

    val relation = new DataBuilder(overpassData.rawData).data.relations(1)
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 1).returns(Some(relation))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(Timestamp(2022, 8, 1)), 1).returns(Some(relation))
  }
}
