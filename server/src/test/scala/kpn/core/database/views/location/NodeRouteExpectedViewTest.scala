package kpn.core.database.views.location

import kpn.api.common.SharedTestObjects
import kpn.api.common.common.NodeRouteCount
import kpn.api.common.common.NodeRouteExpectedCount
import kpn.api.common.location.Location
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NodeRepositoryImpl

class NodeRouteExpectedViewTest extends UnitTest with SharedTestObjects {

  test("node references in route") {

    withDatabase { database =>

      val nodeRepository = new NodeRepositoryImpl(database)

      def node(nodeId: Long, networkType: NetworkType, expectedRouteRelations: Int): Unit = {
        nodeRepository.save(
          newNodeInfo(
            id = nodeId,
            tags = Tags.from(s"expected_r${networkType.letter}n_route_relations" -> expectedRouteRelations.toString),
            location = Some(Location(Seq("a", "b")))
          )
        )
      }

      def query(networkType: NetworkType): Seq[NodeRouteExpectedCount] = {
        NodeRouteExpectedView.query(database, networkType, stale = false)
      }

      node(1001, NetworkType.hiking, 1)
      node(1002, NetworkType.hiking, 2)
      node(1003, NetworkType.cycling, 3)
      node(1004, NetworkType.cycling, 4)
      node(1005, NetworkType.horseRiding, 5)
      node(1006, NetworkType.horseRiding, 6)
      node(1007, NetworkType.canoe, 7)
      node(1008, NetworkType.canoe, 8)
      node(1009, NetworkType.motorboat, 9)
      node(1010, NetworkType.motorboat, 10)
      node(1011, NetworkType.inlineSkating, 11)
      node(1012, NetworkType.inlineSkating, 12)

      nodeRepository.save(newNodeInfo(id = 1013))

      query(NetworkType.hiking) should equal(
        Seq(
          NodeRouteExpectedCount(1001, 1, Seq("a", "b")),
          NodeRouteExpectedCount(1002, 2, Seq("a", "b"))
        )
      )

      query(NetworkType.cycling) should equal(
        Seq(
          NodeRouteExpectedCount(1003, 3, Seq("a", "b")),
          NodeRouteExpectedCount(1004, 4, Seq("a", "b"))
        )
      )

      query(NetworkType.horseRiding) should equal(
        Seq(
          NodeRouteExpectedCount(1005, 5, Seq("a", "b")),
          NodeRouteExpectedCount(1006, 6, Seq("a", "b"))
        )
      )

      query(NetworkType.canoe) should equal(
        Seq(
          NodeRouteExpectedCount(1007, 7, Seq("a", "b")),
          NodeRouteExpectedCount(1008, 8, Seq("a", "b"))
        )
      )

      query(NetworkType.motorboat) should equal(
        Seq(
          NodeRouteExpectedCount(1009, 9, Seq("a", "b")),
          NodeRouteExpectedCount(1010, 10, Seq("a", "b"))
        )
      )

      query(NetworkType.inlineSkating) should equal(
        Seq(
          NodeRouteExpectedCount(1011, 11, Seq("a", "b")),
          NodeRouteExpectedCount(1012, 12, Seq("a", "b"))
        )
      )
    }
  }

}
