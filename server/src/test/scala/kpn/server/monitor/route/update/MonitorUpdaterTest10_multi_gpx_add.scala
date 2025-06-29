package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.api.common.data.MemberType
import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.test.OverpassData
import kpn.core.test.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.MockLog
import kpn.core.util.UnitTest
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
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
        action = MonitorAction.add,
        groupName = group.name,
        routeName = "route-name",
        referenceType = MonitorReferenceType.multiGpx,
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

      val route = assertRoute(configuration, group)

      configuration.monitorRouteRepository.routeReference(route._id, Some(1)) should equal(None)
      configuration.monitorRouteRepository.routeReference(route._id, Some(11)) should equal(None)
      configuration.monitorRouteRepository.routeReference(route._id, Some(12)) should equal(None)

      configuration.monitorRouteRepository.routeState(route._id, 1) should equal(None)

      val state11 = assertState11(configuration, route)
      val state12 = assertState12(configuration, route)

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
        action = MonitorAction.gpxUpload,
        groupName = group.name,
        routeName = "route-name",
        referenceType = MonitorReferenceType.multiGpx,
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

      assertUpdatedRoute1(configuration, group, route)

      val reference11 = assertReference11(configuration, route)

      configuration.monitorRouteRepository.routeReference(route._id, Some(1)) should equal(None)
      configuration.monitorRouteRepository.routeReference(route._id, Some(12)) should equal(None)

      assertUpdatedState11(configuration, route, state11)

      assertEqual(
        configuration.monitorRouteRepository.routeState(route._id, 12),
        Some(state12)
      )

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
        action = MonitorAction.gpxUpload,
        groupName = group.name,
        routeName = "route-name",
        referenceType = MonitorReferenceType.multiGpx,
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

      assertUpdatedRoute2(configuration, group, route)

      configuration.monitorRouteRepository.routeReference(route._id, Some(1)) should equal(None)
      assertEqual(
        configuration.monitorRouteRepository.routeReference(route._id, Some(11)),
        Some(reference11)
      )

      assertReference2(configuration, route)
      assertUpdatedState12(configuration, route, state12)
    }
  }

  private def assertRoute(configuration: MonitorUpdaterConfiguration, group: MonitorGroup) = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
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
        referenceType = MonitorReferenceType.multiGpx,
        referenceTimestamp = None,
        referenceDistance = 0,
        referenceFilename = None,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 1,
        relation = Some(
          newMonitorRouteRelation(
            relationId = 1,
            name = "main-relation",
            relations = Seq(
              newMonitorRouteRelation(
                relationId = 11,
                name = "sub-relation-1",
              ),
              newMonitorRouteRelation(
                relationId = 12,
                name = "sub-relation-2",
              )
            )
          )
        ),
        happy = false,
      )
    )
    route
  }

  private def assertState11(configuration: MonitorUpdaterConfiguration, route: MonitorRoute) = {
    val state = configuration.monitorRouteRepository.routeState(route._id, 11).get
    assertEqual(
      state,
      MonitorRouteState(
        state._id,
        routeId = route._id,
        relationId = 11,
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        matchesGeometry = None,
        deviations = Seq.empty,
      )
    )
    state
  }

  private def assertState12(configuration: MonitorUpdaterConfiguration, route: MonitorRoute) = {
    val state = configuration.monitorRouteRepository.routeState(route._id, 12).get
    assertEqual(
      state,
      MonitorRouteState(
        state._id,
        routeId = route._id,
        relationId = 12,
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        // TODO redesign cleanup - bounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
        matchesGeometry = None,
        deviations = Seq.empty,
      )
    )
    state
  }

  private def assertUpdatedRoute1(configuration: MonitorUpdaterConfiguration, group: MonitorGroup, route: MonitorRoute): Unit = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      route.copy(
        analysisTimestamp = Some(Timestamp(2022, 8, 12, 12, 0, 0)),
        analysisDuration = None,
        referenceDistance = 181,
        relation = route.relation.map { relation =>
          relation.copy(
            relations = Seq(
              relation.relations.head.copy(
                referenceTimestamp = Some(Timestamp(2022, 8, 1)),
                referenceFilename = Some("filename-1"),
                referenceDistance = 181,
                happy = true,
              ),
              relation.relations(1)
            )
          )
        },
        happy = false,
      )
    )
  }

  private def assertReference11(configuration: MonitorUpdaterConfiguration, route: MonitorRoute) = {
    val reference = configuration.monitorRouteRepository.routeReference(route._id, Some(11)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        reference._id,
        routeId = route._id,
        relationId = Some(11),
        timestamp = Timestamp(2022, 8, 12, 12, 0, 0),
        user = "user2",
        referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        referenceType = MonitorReferenceType.gpx, // the route reference type is "multi-gpx", but the invidual reference is "gpx"
        referenceTimestamp = Timestamp(2022, 8, 1),
        referenceDistance = 181,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename-1"),
        referenceGeoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
      )
    )
    reference
  }

  private def assertUpdatedState11(configuration: MonitorUpdaterConfiguration, route: MonitorRoute, state11: MonitorRouteState): Unit = {
    val state = configuration.monitorRouteRepository.routeState(route._id, 11).get
    assertEqual(
      state,
      state11.copy(
        timestamp = Timestamp(2022, 8, 12, 12, 0, 0),
        matchesGeometry = Some("""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4553911,51.4633666],[4.4562458,51.4618272]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""),
      )
    )
  }

  private def assertUpdatedRoute2(configuration: MonitorUpdaterConfiguration, group: MonitorGroup, route: MonitorRoute): Unit = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      route.copy(
        analysisTimestamp = Some(Timestamp(2022, 8, 13, 12, 0, 0)),
        analysisDuration = None,
        referenceDistance = 274,
        relation = route.relation.map { relation =>
          relation.copy(
            happy = true,
            relations = Seq(
              relation.relations.head.copy(
                referenceTimestamp = Some(Timestamp(2022, 8, 1)),
                referenceFilename = Some("filename-1"),
                referenceDistance = 181,
                happy = true,
              ),
              relation.relations(1).copy(
                referenceTimestamp = Some(Timestamp(2022, 8, 2)),
                referenceFilename = Some("filename-2"),
                referenceDistance = 93,
                happy = true,
              )
            )
          )
        },
        happy = true
      )
    )
  }

  private def assertReference2(configuration: MonitorUpdaterConfiguration, route: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(route._id, Some(12)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        reference._id,
        routeId = route._id,
        relationId = Some(12),
        timestamp = Timestamp(2022, 8, 13, 12, 0, 0),
        user = "user3",
        referenceBounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
        referenceType = MonitorReferenceType.gpx, // the route reference type is "multi-gpx", but the invidual reference is "gpx"
        referenceTimestamp = Timestamp(2022, 8, 2),
        referenceDistance = 93,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename-2"),
        referenceGeoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4562458,51.4618272],[4.455056,51.4614496]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
      )
    )
  }

  private def assertUpdatedState12(configuration: MonitorUpdaterConfiguration, route: MonitorRoute, state12: MonitorRouteState): Unit = {
    val state = configuration.monitorRouteRepository.routeState(route._id, 12).get
    assertEqual(
      state,
      state12.copy(
        timestamp = Timestamp(2022, 8, 13, 12, 0, 0),
        matchesGeometry = Some("""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4562458,51.4618272],[4.455056,51.4614496]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""),
      )
    )
  }

  private def setupLoadStructure(configuration: MonitorUpdaterConfiguration): Unit = {

    val overpassData = OverpassData()
      .relation(
        1,
        tags = Tags.from(
          "name" -> "main-relation"
        ),
        members = Seq(
          newMember(MemberType.Relation, 11),
          newMember(MemberType.Relation, 12)
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
          newMember(MemberType.Way, 101),
        )
      )
      .relation(
        12,
        tags = Tags.from(
          "name" -> "sub-relation-2"
        ),
        members = Seq(
          newMember(MemberType.Way, 102),
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
    assertEqual(
      messages,
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
    assertEqual(
      messages,
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
    assertEqual(
      messages,
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
