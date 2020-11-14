package kpn.core.database.views.location

import kpn.api.common.SharedTestObjects
import kpn.api.common.common.NodeRouteCount
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
            tags = Tags.from(s"expected_r${networkType.letter}n_route_relations" -> expectedRouteRelations.toString)
          )
        )
      }

      def query(networkType: NetworkType): Seq[NodeRouteCount] = {
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
          NodeRouteCount(1001, 1),
          NodeRouteCount(1002, 2)
        )
      )

      query(NetworkType.cycling) should equal(
        Seq(
          NodeRouteCount(1003, 3),
          NodeRouteCount(1004, 4)
        )
      )

      query(NetworkType.horseRiding) should equal(
        Seq(
          NodeRouteCount(1005, 5),
          NodeRouteCount(1006, 6)
        )
      )

      query(NetworkType.canoe) should equal(
        Seq(
          NodeRouteCount(1007, 7),
          NodeRouteCount(1008, 8)
        )
      )

      query(NetworkType.motorboat) should equal(
        Seq(
          NodeRouteCount(1009, 9),
          NodeRouteCount(1010, 10)
        )
      )

      query(NetworkType.inlineSkating) should equal(
        Seq(
          NodeRouteCount(1011, 11),
          NodeRouteCount(1012, 12)
        )
      )
    }
  }

}
