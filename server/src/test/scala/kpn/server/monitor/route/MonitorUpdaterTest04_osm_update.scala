package kpn.server.monitor.route

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.monitor.MonitorRelationDataBuilder
import kpn.server.monitor.domain.MonitorRoute
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest04_osm_update extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("route with osm reference - delete previous reference if route not found at referenceTimestamp") {

    withDatabase() { database =>

      val configuration = MonitorUpdaterTestSupport.configuration(database)
      setupLoadStructure(configuration)
      setupLoadTopLevel(configuration)

      val group = newMonitorGroup("group")
      configuration.monitorGroupRepository.saveGroup(group)

      val route = newMonitorRoute(
        group._id,
        name = "route",
        relationId = Some(1),
        user = "user",
        referenceType = "osm",
        referenceTimestamp = Some(Timestamp(2022, 8, 11)),
        referenceFilename = None,
        relation = Some(
          newMonitorRouteRelation(
            relationId = 1,
            name = "route"
          )
        )
      )
      val reference = newMonitorRouteReference(
        routeId = route._id,
        relationId = Some(1),
        referenceType = "osm",
        referenceTimestamp = Timestamp(2022, 8, 11),
      )
      val state = newMonitorRouteState(
        route._id,
        1,
        timestamp = Timestamp(2022, 8, 11),
      )

      configuration.monitorGroupRepository.saveGroup(group)
      configuration.monitorRouteRepository.saveRoute(route)
      configuration.monitorRouteRepository.saveRouteReference(reference)
      configuration.monitorRouteRepository.saveRouteState(state)

      val properties = MonitorRouteProperties(
        group.name,
        "route",
        "route",
        None,
        Some(1),
        "osm",
        Some(Timestamp(2022, 8, 1)),
        None,
        referenceFileChanged = false,
      )

      Time.set(Timestamp(2022, 8, 11, 12, 0, 0))
      val saveResult = configuration.monitorUpdater.update("user", group.name, route.name, properties)

      saveResult should equal(
        MonitorRouteSaveResult(
          errors = Seq(
            "Could not load relation 1 at 2022-08-01"
          )
        )
      )

      val updatedRoute = configuration.monitorRouteRepository.routeByName(group._id, "route").get
      updatedRoute.shouldMatchTo(
        MonitorRoute(
          _id = route._id,
          groupId = group._id,
          name = "route",
          description = "route",
          comment = None,
          relationId = Some(1),
          user = "user",
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          referenceType = "osm",
          referenceTimestamp = Some(Timestamp(2022, 8, 1)),
          referenceFilename = None,
          referenceDistance = 0,
          deviationDistance = 0,
          deviationCount = 0,
          osmWayCount = 0,
          osmDistance = 0,
          osmSegmentCount = 0,
          osmSegments = Seq.empty,
          relation = Some(
            MonitorRouteRelation(
              relationId = 1,
              name = "route",
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
              relations = Seq.empty
            )
          ),
          happy = false
        )
      )

      configuration.monitorRouteRepository.routeRelationReference(route._id, 1) should equal(None)
      configuration.monitorRouteRepository.routeState(route._id, 1) should equal(None)
    }
  }

  private def setupLoadStructure(configuration: MonitorUpdaterConfiguration): Unit = {

    val overpassData = OverpassData()
      .relation(
        1,
        tags = Tags.from(
          "name" -> "route"
        ),
      )

    val relation = new MonitorRelationDataBuilder(overpassData.rawData).data.relations(1)
    (configuration.monitorRouteRelationRepository.loadStructure _).when(None, 1).returns(Some(relation))
  }

  private def setupLoadTopLevel(configuration: MonitorUpdaterConfiguration): Unit = {

    val overpassData = OverpassData()
      .node(1001, latitude = "51.4633666", longitude = "4.4553911")
      .node(1002, latitude = "51.4618272", longitude = "4.4562458")
      .way(101, 1001, 1002)
      .relation(
        1,
        tags = Tags.from(
          "name" -> "route"
        ),
        members = Seq(
          newMember("way", 101),
        )
      )

    val relation = new DataBuilder(overpassData.rawData).data.relations(1)
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 1).returns(Some(relation))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(Timestamp(2022, 8, 1)), 1).returns(None)
  }
}
