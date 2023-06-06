package kpn.server.monitor.route

import kpn.api.common.Bounds
import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatus
import kpn.api.common.monitor.MonitorRouteUpdateStep
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
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest09_multi_gpx_add extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

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
      configuration.monitorUpdater.update("user", routeAdd, routeAddReporter)

      routeAddReporter.statusses.shouldMatchTo(
        Seq(
          MonitorRouteUpdateStatus(
            Seq(
              MonitorRouteUpdateStep("definition", "busy"),
            )
          )
        )
      )

      val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
      route.shouldMatchTo(
        MonitorRoute(
          _id = route._id,
          groupId = group._id,
          name = "route-name",
          description = "route-description",
          comment = Some("route-comment"),
          relationId = Some(1),
          user = "user",
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          referenceType = "multi-gpx",
          referenceTimestamp = None,
          referenceDistance = 0,
          referenceFilename = None,
          deviationDistance = 0,
          deviationCount = 0,
          osmWayCount = 0,
          osmDistance = 0,
          osmSegmentCount = 0,
          happy = false,
          osmSegments = Seq.empty,
          relation = Some(
            MonitorRouteRelation(
              relationId = 1,
              name = "main-relation",
              role = None,
              survey = None,
              referenceTimestamp = None,
              referenceFilename = None,
              referenceDistance = 0,
              deviationDistance = 0,
              deviationCount = 0,
              osmWayCount = 0,
              osmDistance = 0,
              osmSegmentCount = 0,
              happy = false,
              relations = Seq(
                MonitorRouteRelation(
                  relationId = 11,
                  name = "sub-relation-1",
                  role = None,
                  survey = None,
                  referenceTimestamp = None,
                  referenceFilename = None,
                  referenceDistance = 0,
                  deviationDistance = 0,
                  deviationCount = 0,
                  osmWayCount = 0, // TODO should be non-zero, or only filled in after analysis?
                  osmDistance = 0, // TODO should be non-zero, or only filled in after analysis?
                  osmSegmentCount = 0, // TODO should be non-zero, or only filled in after analysis?
                  happy = false,
                  relations = Seq.empty
                ),
                MonitorRouteRelation(
                  relationId = 12,
                  name = "sub-relation-2",
                  role = None,
                  survey = None,
                  referenceTimestamp = None,
                  referenceFilename = None,
                  referenceDistance = 0,
                  deviationDistance = 0,
                  deviationCount = 0,
                  osmWayCount = 0, // TODO should be non-zero, or only filled in after analysis?
                  osmDistance = 0, // TODO should be non-zero, or only filled in after analysis?
                  osmSegmentCount = 0, // TODO should be non-zero, or only filled in after analysis?
                  happy = false,
                  relations = Seq.empty
                )
              )
            )
          )
        )
      )

      configuration.monitorRouteRepository.routeRelationReference(route._id, 1) should equal(None)
      configuration.monitorRouteRepository.routeRelationReference(route._id, 11) should equal(None)
      configuration.monitorRouteRepository.routeRelationReference(route._id, 12) should equal(None)

      configuration.monitorRouteRepository.routeState(route._id, 1) should equal(None)
      configuration.monitorRouteRepository.routeState(route._id, 11) should equal(None)
      configuration.monitorRouteRepository.routeState(route._id, 12) should equal(None)

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
        relationId = Some(1),
        referenceTimestamp = Some(Timestamp(2022, 8, 1, 0, 0, 0)),
        referenceFilename = Some("filename-1"),
        referenceGpx = Some(gpx1)
      )

      val uploadGpxReporter1 = new MonitorUpdateReporterMock()
      configuration.monitorUpdater.update("user", uploadGpx1, uploadGpxReporter1)

      uploadGpxReporter1.statusses.shouldMatchTo(
        Seq(
          MonitorRouteUpdateStatus(
            Seq(
              MonitorRouteUpdateStep("definition", "busy"),
            )
          )
        )
      )


      val routeUpdated1 = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
      routeUpdated1.shouldMatchTo(
        route.copy(
          referenceTimestamp = None,
          referenceFilename = None,
          referenceDistance = 196, // subrelation 11 reference distance
          deviationDistance = 0,
          deviationCount = 0,
          osmWayCount = 1,
          osmDistance = 196,
          osmSegmentCount = 1,
          happy = true,
          osmSegments = Seq(
            MonitorRouteOsmSegment(
              Seq(
                MonitorRouteOsmSegmentElement(
                  relationId = 11,
                  segmentId = 1,
                  meters = 196,
                  bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
                  reversed = false
                )
              )
            )
          ),
          relation = route.relation.map { relation =>
            relation.copy(
              relations = Seq(
                relation.relations.head.copy(
                  referenceTimestamp = Some(Timestamp(2022, 8, 1)),
                  referenceFilename = Some("filename-1"),
                  referenceDistance = 196,
                  osmWayCount = 1,
                  osmDistance = 196,
                  osmSegmentCount = 1,
                  happy = true,
                ),
                relation.relations(1)
              )
            )
          }
        )
      )

      val reference1 = configuration.monitorRouteRepository.routeRelationReference(route._id, 11).get
      reference1.shouldMatchTo(
        MonitorRouteReference(
          reference1._id,
          routeId = route._id,
          relationId = Some(11),
          timestamp = Timestamp(2022, 8, 12, 12, 0, 0),
          user = "user",
          bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
          referenceType = "gpx", // the route reference type is "multi-gpx", but the invidual reference is "gpx"
          referenceTimestamp = Timestamp(2022, 8, 1),
          distance = 196,
          segmentCount = 1,
          filename = Some("filename-1"),
          geoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
        )
      )

      configuration.monitorRouteRepository.routeRelationReference(route._id, 1) should equal(None)
      configuration.monitorRouteRepository.routeRelationReference(route._id, 12) should equal(None)

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
      configuration.monitorUpdater.update("user", uploadGpx2, uploadGpxReporter2)

      uploadGpxReporter2.statusses.shouldMatchTo(
        Seq(
          MonitorRouteUpdateStatus(
            Seq(
              MonitorRouteUpdateStep("definition", "busy"),
            )
          )
        )
      )

      val routeUpdated2 = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
      routeUpdated2.shouldMatchTo(
        route.copy(
          referenceTimestamp = None,
          referenceFilename = None,
          referenceDistance = 335, // sum of subrelation 11 and subrelation 12 reference distances
          deviationDistance = 0,
          deviationCount = 0,
          osmWayCount = 2,
          osmDistance = 335,
          osmSegmentCount = 1,
          happy = true,
          osmSegments = Seq(
            MonitorRouteOsmSegment(
              Seq(
                MonitorRouteOsmSegmentElement(
                  relationId = 11,
                  segmentId = 1,
                  meters = 196,
                  bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
                  reversed = false
                ),
                MonitorRouteOsmSegmentElement(
                  relationId = 12,
                  segmentId = 1,
                  meters = 139,
                  bounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
                  reversed = false
                ),
              )
            )
          ),
          relation = route.relation.map { relation =>
            relation.copy(
              relations = Seq(
                relation.relations.head.copy(
                  referenceTimestamp = Some(Timestamp(2022, 8, 1)),
                  referenceFilename = Some("filename-1"),
                  referenceDistance = 196,
                  osmWayCount = 1,
                  osmDistance = 196,
                  osmSegmentCount = 1,
                  happy = true,
                ),
                relation.relations(1).copy(
                  referenceTimestamp = Some(Timestamp(2022, 8, 2)),
                  referenceFilename = Some("filename-2"),
                  referenceDistance = 139,
                  osmWayCount = 1,
                  osmDistance = 139,
                  osmSegmentCount = 1,
                  happy = true,
                )
              )
            )
          }
        )
      )

      configuration.monitorRouteRepository.routeRelationReference(route._id, 1) should equal(None)
      configuration.monitorRouteRepository.routeRelationReference(route._id, 11).shouldMatchTo(Some(reference1))

      val reference2 = configuration.monitorRouteRepository.routeRelationReference(route._id, 12).get
      reference2.shouldMatchTo(
        MonitorRouteReference(
          reference2._id,
          routeId = route._id,
          relationId = Some(12),
          timestamp = Timestamp(2022, 8, 13, 12, 0, 0),
          user = "user",
          bounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
          referenceType = "gpx", // the route reference type is "multi-gpx", but the invidual reference is "gpx"
          referenceTimestamp = Timestamp(2022, 8, 2),
          distance = 139,
          segmentCount = 1,
          filename = Some("filename-2"),
          geoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4562458,51.4618272],[4.455056,51.4614496]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
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
    val subRelation1 = data.relations(11)
    val subRelation2 = data.relations(12)

    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 11).returns(Some(subRelation1))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 12).returns(Some(subRelation2))
  }
}
