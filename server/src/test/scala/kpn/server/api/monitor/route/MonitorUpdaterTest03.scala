package kpn.server.api.monitor.route

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.custom.Day
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.api.monitor.MonitorRelationDataBuilder
import kpn.server.api.monitor.domain.MonitorRoute
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest03 extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("add superroute osm reference") {

    val referenceDay = Day(2023, 8, 11)

    withDatabase() { database =>

      val config = new MonitorUpdaterConfiguration(database)
      setupLoadStructure(config)
      setupLoadTopLevel(config, referenceDay)

      val group = newMonitorGroup("group")
      config.monitorGroupRepository.saveGroup(group)

      val properties = MonitorRouteProperties(
        group.name,
        "route-name",
        "route-description",
        Some("route-comment"),
        Some(1L),
        "osm",
        Some(referenceDay),
        None,
        referenceFileChanged = false,
      )

      Time.set(Timestamp(2022, 8, 11, 12, 0, 0))
      config.monitorUpdater.add("user", group.name, properties)

      val route = config.monitorRouteRepository.routeByName(group._id, "route-name").get
      route.shouldMatchTo(
        MonitorRoute(
          route._id,
          groupId = group._id,
          name = "route-name",
          description = "route-description",
          comment = Some("route-comment"),
          relationId = Some(1L),
          user = "user",
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          referenceType = "osm",
          referenceDay = Some(referenceDay),
          referenceFilename = None,
          referenceDistance = 335L,
          deviationDistance = 0L,
          deviationCount = 0L,
          osmWayCount = 2L,
          osmDistance = 335L,
          osmSegmentCount = 0, // TODO not implemented yet??
          happy = false, // TODO needs osmSegmentCount
          relation = Some(
            MonitorRouteRelation(
              relationId = 1L,
              name = "main-relation",
              role = None,
              survey = None,
              deviationDistance = 0L,
              deviationCount = 0L,
              osmWayCount = 0L, // TODO 2L,
              osmDistance = 0L, // TODO 335L,
              osmSegmentCount = 0L, // TODO 1L,
              happy = false, // TODO true,
              relations = Seq(
                MonitorRouteRelation(
                  relationId = 11L,
                  name = "sub-relation-1",
                  role = None,
                  survey = None,
                  deviationDistance = 0L,
                  deviationCount = 0L,
                  osmWayCount = 1L,
                  osmDistance = 196L,
                  osmSegmentCount = 1L,
                  happy = true,
                  relations = Seq.empty
                ),
                MonitorRouteRelation(
                  relationId = 12L,
                  name = "sub-relation-2",
                  role = None,
                  survey = None,
                  deviationDistance = 0L,
                  deviationCount = 0L,
                  osmWayCount = 1L,
                  osmDistance = 139L,
                  osmSegmentCount = 1L,
                  happy = true,
                  relations = Seq.empty
                )
              )
            )
          )
        )
      )
    }
  }

  private def setupLoadStructure(config: MonitorUpdaterConfiguration): Unit = {

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

    val relation = new MonitorRelationDataBuilder(overpassData.rawData).data.relations(1L)
    (config.monitorRouteRelationRepository.loadStructure _).when(None, 1L).returns(Some(relation))
  }

  private def setupLoadTopLevel(config: MonitorUpdaterConfiguration, referenceDay: Day): Unit = {

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
    val subRelation1 = data.relations(11L)
    val subRelation2 = data.relations(12L)

    (config.monitorRouteRelationRepository.loadTopLevel _).when(Some(Timestamp(referenceDay)), 11L).returns(Some(subRelation1))
    (config.monitorRouteRelationRepository.loadTopLevel _).when(Some(Timestamp(referenceDay)), 12L).returns(Some(subRelation2))

    (config.monitorRouteRelationRepository.loadTopLevel _).when(None, 11L).returns(Some(subRelation1))
    (config.monitorRouteRelationRepository.loadTopLevel _).when(None, 12L).returns(Some(subRelation2))
  }
}
