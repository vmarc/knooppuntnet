package kpn.core.database.views.node

import kpn.api.common.SharedTestObjects
import kpn.api.common.common.NodeRouteExpectedCount
import kpn.api.common.location.Location
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NodeRepositoryImpl

class NodeRouteExpectedViewTest extends UnitTest with SharedTestObjects {

  test("node references in route") {

    withDatabase { database =>

      val nodeRepository = new NodeRepositoryImpl(database)

      def node(nodeId: Long, nodeName: String, networkScope: NetworkScope, networkType: NetworkType, expectedRouteRelations: Int): Unit = {
        nodeRepository.save(
          newNodeInfo(
            id = nodeId,
            tags = Tags.from(
              s"expected_${networkScope.letter}${networkType.letter}n_route_relations" -> expectedRouteRelations.toString,
              s"r${networkType.letter}n_ref" -> nodeName
            ),
            location = Some(Location(Seq("a", "b")))
          )
        )
      }

      def query(networkType: NetworkType): Seq[NodeRouteExpectedCount] = {
        NodeRouteExpectedView.query(database, networkType, stale = false)
      }

      node(1001, "01", NetworkScope.local, NetworkType.hiking, 1)
      node(1002, "02", NetworkScope.regional, NetworkType.hiking, 2)
      node(1003, "03", NetworkScope.national, NetworkType.cycling, 3)
      node(1004, "04", NetworkScope.local, NetworkType.cycling, 4)
      node(1005, "05", NetworkScope.regional, NetworkType.horseRiding, 5)
      node(1006, "06", NetworkScope.national, NetworkType.horseRiding, 6)
      node(1007, "07", NetworkScope.local, NetworkType.canoe, 7)
      node(1008, "08", NetworkScope.regional, NetworkType.canoe, 8)
      node(1009, "09", NetworkScope.national, NetworkType.motorboat, 9)
      node(1010, "10", NetworkScope.local, NetworkType.motorboat, 10)
      node(1011, "11", NetworkScope.regional, NetworkType.inlineSkating, 11)
      node(1012, "12", NetworkScope.national, NetworkType.inlineSkating, 12)

      nodeRepository.save(newNodeInfo(id = 1013))

      query(NetworkType.hiking) should equal(
        Seq(
          NodeRouteExpectedCount(1001, "01", Seq("a", "b"), 1),
          NodeRouteExpectedCount(1002, "02", Seq("a", "b"), 2)
        )
      )

      query(NetworkType.cycling) should equal(
        Seq(
          NodeRouteExpectedCount(1003, "03", Seq("a", "b"), 3),
          NodeRouteExpectedCount(1004, "04", Seq("a", "b"), 4)
        )
      )

      query(NetworkType.horseRiding) should equal(
        Seq(
          NodeRouteExpectedCount(1005, "05", Seq("a", "b"), 5),
          NodeRouteExpectedCount(1006, "06", Seq("a", "b"), 6)
        )
      )

      query(NetworkType.canoe) should equal(
        Seq(
          NodeRouteExpectedCount(1007, "07", Seq("a", "b"), 7),
          NodeRouteExpectedCount(1008, "08", Seq("a", "b"), 8)
        )
      )

      query(NetworkType.motorboat) should equal(
        Seq(
          NodeRouteExpectedCount(1009, "09", Seq("a", "b"), 9),
          NodeRouteExpectedCount(1010, "10", Seq("a", "b"), 10)
        )
      )

      query(NetworkType.inlineSkating) should equal(
        Seq(
          NodeRouteExpectedCount(1011, "11", Seq("a", "b"), 11),
          NodeRouteExpectedCount(1012, "12", Seq("a", "b"), 12)
        )
      )
    }
  }

}
