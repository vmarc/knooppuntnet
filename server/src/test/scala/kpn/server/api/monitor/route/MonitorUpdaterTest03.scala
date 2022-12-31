package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.custom.Day
import kpn.api.custom.Relation
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.data.DataBuilder
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.api.monitor.MonitorRelationDataBuilder
import kpn.server.api.monitor.domain.MonitorGroup
import org.scalamock.scalatest.MockFactory

class MonitorUpdaterTest03 extends UnitTest with SharedTestObjects with MockFactory {

  test("add superroute osm reference") {

    val referenceDay = Day(2023, 8, 11)

    withDatabase() { database =>

      val monitorRouteRelationRepository = stub[MonitorRouteRelationRepository]

      val mainRelationData = OverpassData()
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

      val mainRelationStructure: Relation = new MonitorRelationDataBuilder(mainRelationData.rawData).data.relations(1L)

      (monitorRouteRelationRepository.loadStructure _).when(None, 1L).returns(Some(mainRelationStructure))

      val subRelationsOverpassData = OverpassData()
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

      val subRelationsData = new DataBuilder(subRelationsOverpassData.rawData).data
      val subRelation1 = subRelationsData.relations(11L)
      val subRelation2 = subRelationsData.relations(12L)

      (monitorRouteRelationRepository.loadTopLevel _).when(Some(Timestamp(referenceDay)), 11L).returns(Some(subRelation1))
      (monitorRouteRelationRepository.loadTopLevel _).when(Some(Timestamp(referenceDay)), 12L).returns(Some(subRelation2))

      (monitorRouteRelationRepository.loadTopLevel _).when(None, 11L).returns(Some(subRelation1))
      (monitorRouteRelationRepository.loadTopLevel _).when(None, 12L).returns(Some(subRelation2))

      val config = new MonitorUpdaterConfiguration(database, monitorRouteRelationRepository)
      val group = MonitorGroup(ObjectId(), "group", "")
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
      config.monitorUpdater.add("user", group.name, properties)

      val route = config.monitorRouteRepository.routeByName(group._id, "route-name").get
      route.groupId should equal(group._id)
      route.name should equal("route-name")
      route.description should equal("route-description")
      route.comment should equal(Some("route-comment"))
      route.relationId should equal(Some(1L))
      route.user should equal("user")
      route.referenceType should equal("osm")
      route.referenceDay should equal(Some(referenceDay))
      route.referenceFilename should equal(None)
      route.referenceDistance should equal(335L)
      route.deviationDistance should equal(0L)
      route.deviationCount should equal(0L)
      route.osmWayCount should equal(2L)
      route.osmDistance should equal(335L)
      route.osmSegmentCount should equal(0) // TODO not implemented yet??
      route.happy should equal(false) // TODO needs osmSegmentCount

      route.relation match {
        case None => fail("MonitorRouteRelation not found")
        case Some(monitorRouteRelation) =>
          monitorRouteRelation.relations.size should equal(2)
          val subRelation1 = monitorRouteRelation.relations.head
          subRelation1.relationId should equal(11L)
          subRelation1.name should equal(Some("sub-relation-1"))
          subRelation1.role should equal(None)
          subRelation1.survey should equal(None)
          subRelation1.deviationDistance should equal(0L)
          subRelation1.deviationCount should equal(0L)
          subRelation1.osmWayCount should equal(1L)
          subRelation1.osmDistance should equal(196L)
          subRelation1.osmSegmentCount should equal(1L)
          subRelation1.happy should equal(true)
          subRelation1.relations.size should equal(0)

          val subRelation2 = monitorRouteRelation.relations(1)
          subRelation2.relationId should equal(12L)
          subRelation2.name should equal(Some("sub-relation-2"))
          subRelation2.role should equal(None)
          subRelation2.survey should equal(None)
          subRelation2.deviationDistance should equal(0L)
          subRelation2.deviationCount should equal(0L)
          subRelation2.osmWayCount should equal(1L)
          subRelation2.osmDistance should equal(139L)
          subRelation2.osmSegmentCount should equal(1L)
          subRelation1.happy should equal(true)
          subRelation2.relations.size should equal(0)
      }
    }
  }
}
