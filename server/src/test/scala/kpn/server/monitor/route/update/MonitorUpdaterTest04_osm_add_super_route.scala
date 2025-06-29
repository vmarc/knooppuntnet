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

class MonitorUpdaterTest04_osm_add_super_route extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  private val log = new MockLog()

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("add superroute osm reference") {

    val referenceTimestamp = Timestamp(2022, 8, 11)

    withDatabase() { database =>

      val configuration = MonitorUpdaterTestSupport.configuration(database)
      setupLoadStructure(configuration)
      setupLoadTopLevel(configuration, referenceTimestamp)

      val group = newMonitorGroup("group")
      configuration.monitorGroupRepository.saveGroup(group)

      Time.set(Timestamp(2022, 8, 11, 12, 0, 0))

      val reporter = new MonitorUpdateReporterMock()
      configuration.monitorRouteUpdateExecutor.execute(
        MonitorUpdateContext(
          "user",
          reporter,
          MonitorRouteUpdate(
            action = MonitorAction.add,
            groupName = group.name,
            routeName = "route-name",
            referenceType = MonitorReferenceType.osm,
            description = Some("route-description"),
            comment = Some("route-comment"),
            relationId = Some(1),
            referenceTimestamp = Some(referenceTimestamp),
          )
        )
      )

      database.monitorRoutes.countDocuments(log) should equal(1)
      database.monitorRouteReferences.countDocuments(log) should equal(2)
      database.monitorRouteStates.countDocuments(log) should equal(2)

      val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
      assertEqual(
        route.copy(analysisDuration = None),
        MonitorRoute(
          route._id,
          groupId = group._id,
          name = "route-name",
          description = "route-description",
          comment = Some("route-comment"),
          relationId = Some(1),
          user = "user",
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          symbol = None,
          analysisTimestamp = Some(Timestamp(2022, 8, 11, 12, 0, 0)),
          analysisDuration = None,
          referenceType = MonitorReferenceType.osm,
          referenceTimestamp = Some(referenceTimestamp),
          referenceFilename = None,
          referenceDistance = 274,
          deviationDistance = 0,
          deviationCount = 0,
          osmSegmentCount = 1,
          relation = Some(
            newMonitorRouteRelation(
              relationId = 1,
              name = "main-relation",
              happy = true,
              relations = Seq(
                newMonitorRouteRelation(
                  relationId = 11,
                  name = "sub-relation-1",
                  happy = true,
                ),
                newMonitorRouteRelation(
                  relationId = 12,
                  name = "sub-relation-2",
                  happy = true,
                )
              )
            )
          ),
          happy = true,
        )
      )

      val reference1 = configuration.monitorRouteRepository.routeReference(route._id, Some(1))
      reference1 should equal(None)

      val reference11 = configuration.monitorRouteRepository.routeReference(route._id, Some(11)).get
      assertEqual(
        reference11,
        MonitorRouteReference(
          reference11._id,
          routeId = route._id,
          relationId = Some(11),
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          user = "user",
          referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
          referenceType = MonitorReferenceType.osm,
          referenceTimestamp = Timestamp(2022, 8, 11),
          referenceDistance = 181,
          referenceSegmentCount = 1,
          referenceFilename = None,
          referenceGeoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}]}"""
        )
      )

      val reference12 = configuration.monitorRouteRepository.routeReference(route._id, Some(12)).get
      assertEqual(
        reference12,
        MonitorRouteReference(
          reference12._id,
          routeId = route._id,
          relationId = Some(12),
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          user = "user",
          referenceBounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
          referenceType = MonitorReferenceType.osm,
          referenceTimestamp = Timestamp(2022, 8, 11),
          referenceDistance = 93,
          referenceSegmentCount = 1,
          referenceFilename = None,
          referenceGeoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4562458,51.4618272],[4.455056,51.4614496]]}]}"""
        )
      )

      val state = configuration.monitorRouteRepository.routeState(route._id, 1)
      state should equal(None)

      val state11 = configuration.monitorRouteRepository.routeState(route._id, 11).get
      assertEqual(
        state11,
        MonitorRouteState(
          state11._id,
          routeId = route._id,
          relationId = 11,
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
          matchesGeometry = Some("""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4553911,51.4633666],[4.4562458,51.4618272]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""),
          deviations = Seq.empty,
        )
      )

      val state12 = configuration.monitorRouteRepository.routeState(route._id, 12).get
      assertEqual(
        state12,
        MonitorRouteState(
          state12._id,
          routeId = route._id,
          relationId = 12,
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          // TODO redesign cleanup - bounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
          matchesGeometry = Some("""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4562458,51.4618272],[4.455056,51.4614496]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""),
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

  private def setupLoadTopLevel(configuration: MonitorUpdaterConfiguration, referenceTimestamp: Timestamp): Unit = {

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
        members = Seq(
          newMember(MemberType.Relation, 11),
          newMember(MemberType.Relation, 12),
        )
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

    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(referenceTimestamp), 1).returns(Some(mainRelation))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(referenceTimestamp), 11).returns(Some(subRelation1))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(referenceTimestamp), 12).returns(Some(subRelation2))

    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 1).returns(Some(mainRelation))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 11).returns(Some(subRelation1))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 12).returns(Some(subRelation2))
  }
}
