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
import kpn.core.util.UnitTest
import kpn.database.base.Database
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest12_multi_gpx_update_gpx extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  private val ReferenceTimestamp = Timestamp(2022, 8, 1)
  private val CurrentTimestamp = Timestamp(2022, 8, 11, 12, 0, 0)
  private val UpdateTimestamp = Timestamp(2022, 8, 12, 12, 0, 0)

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("gpx reference per subrelation - update subrelation gpx reference") {

    withDatabase() { database =>

      val (configuration, group) = setup(database)

      executeAddRoute(configuration, group)
      val (route, state11) = verifyAddRoute(database, configuration, group)

      val gpx = executeUploadGpx1(configuration, group)
      val reference11 = verifyGpxUpload1(database, configuration, group, route, state11)

      executeGpxUpload2(configuration, group, gpx)
      verifyGpxUpload2(database, configuration, group, route, reference11)
    }
  }

  private def executeAddRoute(configuration: MonitorUpdaterConfiguration, group: MonitorGroup): Unit = {
    val routeAdd = MonitorRouteUpdate(
      action = MonitorAction.add,
      groupName = group.name,
      routeName = "route-name",
      referenceType = MonitorReferenceType.multiGpx,
      description = Some("route-description"),
      comment = Some("route-comment"),
      relationId = Some(1),
    )

    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user1",
        new MonitorUpdateReporterMock(),
        routeAdd
      )
    )
  }

  private def executeUploadGpx1(configuration: MonitorUpdaterConfiguration, group: MonitorGroup): String = {
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

    Time.set(UpdateTimestamp)
    val uploadGpx1 = MonitorRouteUpdate(
      action = MonitorAction.gpxUpload,
      groupName = group.name,
      routeName = "route-name",
      referenceType = MonitorReferenceType.multiGpx,
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
    gpx
  }

  private def executeGpxUpload2(configuration: MonitorUpdaterConfiguration, group: MonitorGroup, gpx: String): Unit = {
    Time.set(Timestamp(2022, 8, 13, 12, 0, 0))

    val uploadGpx2 = MonitorRouteUpdate(
      action = MonitorAction.gpxUpload,
      groupName = group.name,
      routeName = "route-name",
      referenceType = MonitorReferenceType.multiGpx,
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
  }

  private def verifyGpxUpload1(database: Database, configuration: MonitorUpdaterConfiguration, group: MonitorGroup, route: MonitorRoute, state11: MonitorRouteState): MonitorRouteReference = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(1)
    database.monitorRouteStates.countDocuments() should equal(1)

    verifyUpdatedRoute1(configuration, group, route)

    val reference11 = verifyReference11(configuration, route)

    configuration.monitorRouteRepository.routeReference(route._id, Some(1)) should equal(None)

    assertUpdatedState11(configuration, route, state11)
    reference11
  }

  private def verifyAddRoute(database: Database, configuration: MonitorUpdaterConfiguration, group: MonitorGroup): (MonitorRoute, MonitorRouteState) = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(0)
    database.monitorRouteStates.countDocuments() should equal(1)

    val route = verifyRoute(configuration, group)

    configuration.monitorRouteRepository.routeReference(route._id, Some(1)) should equal(None)
    configuration.monitorRouteRepository.routeReference(route._id, Some(11)) should equal(None)

    configuration.monitorRouteRepository.routeState(route._id, 1) should equal(None)
    val state11 = verifyState11(configuration, route)
    (route, state11)
  }

  private def verifyRoute(configuration: MonitorUpdaterConfiguration, group: MonitorGroup): MonitorRoute = {
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
        timestamp = CurrentTimestamp,
        symbol = None,
        analysisTimestamp = Some(CurrentTimestamp),
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
                name = "sub-relation",
              )
            )
          )
        ),
        happy = false,
      )
    )
    route
  }

  private def verifyState11(configuration: MonitorUpdaterConfiguration, route: MonitorRoute): MonitorRouteState = {
    val state = configuration.monitorRouteRepository.routeState(route._id, 11).get
    assertEqual(
      state,
      MonitorRouteState(
        state._id,
        routeId = route._id,
        relationId = 11,
        timestamp = CurrentTimestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        matchesGeometry = None,
        deviations = Seq.empty,
      )
    )
    state
  }

  private def verifyUpdatedRoute1(configuration: MonitorUpdaterConfiguration, group: MonitorGroup, route: MonitorRoute): Unit = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      route.copy(
        analysisTimestamp = Some(UpdateTimestamp),
        analysisDuration = None,
        referenceDistance = 181,
        relation = route.relation.map { relation =>
          relation.copy(
            happy = true,
            relations = Seq(
              relation.relations.head.copy(
                referenceTimestamp = Some(ReferenceTimestamp),
                referenceFilename = Some("filename-1"),
                referenceDistance = 181,
                happy = true,
              )
            )
          )
        },
        happy = true
      )
    )
  }

  private def verifyReference11(configuration: MonitorUpdaterConfiguration, route: MonitorRoute): MonitorRouteReference = {
    val reference = configuration.monitorRouteRepository.routeReference(route._id, Some(11)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        reference._id,
        routeId = route._id,
        relationId = Some(11),
        timestamp = UpdateTimestamp,
        user = "user2",
        referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        referenceType = MonitorReferenceType.gpx, // the route reference type is "multi-gpx", but the invidual reference is "gpx"
        referenceTimestamp = ReferenceTimestamp,
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
        timestamp = UpdateTimestamp,
        matchesGeometry = Some("""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4553911,51.4633666],[4.4562458,51.4618272]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""),
      )
    )
  }

  private def verifyUpdatedRoute2(configuration: MonitorUpdaterConfiguration, group: MonitorGroup, route: MonitorRoute): Unit = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      route.copy(
        analysisTimestamp = Some(Timestamp(2022, 8, 13, 12, 0, 0)),
        analysisDuration = None,
        referenceDistance = 181,
        relation = route.relation.map { relation =>
          relation.copy(
            happy = true,
            relations = Seq(
              relation.relations.head.copy(
                referenceTimestamp = Some(Timestamp(2022, 8, 2)),
                referenceFilename = Some("filename-2"),
                referenceDistance = 181,
                happy = true,
              )
            )
          )
        },
        happy = true
      )
    )
  }

  private def assertUpdatedReference11(configuration: MonitorUpdaterConfiguration, route: MonitorRoute, reference11: MonitorRouteReference): Unit = {
    assertEqual(
      configuration.monitorRouteRepository.routeReference(route._id, Some(11)),
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

  private def verifyGpxUpload2(database: Database, configuration: MonitorUpdaterConfiguration, group: MonitorGroup, route: MonitorRoute, reference11: MonitorRouteReference): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(1)
    database.monitorRouteStates.countDocuments() should equal(1)

    verifyUpdatedRoute2(configuration, group, route)

    configuration.monitorRouteRepository.routeReference(route._id, Some(1)) should equal(None)
    assertUpdatedReference11(configuration, route, reference11)
  }

  private def setup(database: Database) = {

    val configuration = MonitorUpdaterTestSupport.configuration(database)
    setupLoadStructure(configuration)
    setupLoadTopLevel(configuration)

    val group = newMonitorGroup("group")
    configuration.monitorGroupRepository.saveGroup(group)

    Time.set(CurrentTimestamp)

    (configuration, group)
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
          newMember(MemberType.Way, 101),
        )
      )

    val data = new DataBuilder(overpassData.rawData).data
    val mainRelation = data.relations(1)
    val subRelation = data.relations(11)

    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 1).returns(Some(mainRelation))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 11).returns(Some(subRelation))
  }
}
