package kpn.core.database.views.node

import kpn.api.common.NodeName
import kpn.api.common.SharedTestObjects
import kpn.api.common.common.NodeRouteExpectedCount
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NodeRepositoryImpl

class NodeRouteExpectedViewTest extends UnitTest with SharedTestObjects {

  test("node references in route") {

    pending

    withCouchDatabase { database =>

      val nodeRepository = new NodeRepositoryImpl(null)

      def node(nodeId: Long, nodeName: String, networkScope: NetworkScope, networkType: NetworkType, expectedRouteRelations: Int): Unit = {
        val key = s"${networkScope.letter}${networkType.letter}n"
        nodeRepository.save(
          newNodeDoc(
            id = nodeId,
            name = nodeName,
            names = Seq(
              NodeName(
                networkType,
                networkScope,
                nodeName,
                None,
                proposed = false
              )
            ),
            tags = Tags.from(
              s"expected_${key}_route_relations" -> expectedRouteRelations.toString,
              s"${key}_ref" -> nodeName
            ),
            locations = Seq("a", "b")
          )
        )
      }

      def queryNetworkType(networkType: NetworkType): Seq[NodeRouteExpectedCount] = {
        NodeRouteExpectedView.queryNetworkType(database, networkType)
      }

      def queryScopedNetworkType(networkScope: NetworkScope, networkType: NetworkType): Seq[NodeRouteExpectedCount] = {
        val scopedNetworkType = ScopedNetworkType.from(networkScope, networkType)
        NodeRouteExpectedView.queryScopedNetworkType(database, scopedNetworkType)
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

      nodeRepository.save(newNodeDoc(id = 1013))

      queryNetworkType(NetworkType.hiking) should matchTo(
        Seq(
          NodeRouteExpectedCount(1001, "01", Seq("a", "b"), 1),
          NodeRouteExpectedCount(1002, "02", Seq("a", "b"), 2)
        )
      )

      queryNetworkType(NetworkType.cycling) should matchTo(
        Seq(
          NodeRouteExpectedCount(1003, "03", Seq("a", "b"), 3),
          NodeRouteExpectedCount(1004, "04", Seq("a", "b"), 4)
        )
      )

      queryNetworkType(NetworkType.horseRiding) should matchTo(
        Seq(
          NodeRouteExpectedCount(1005, "05", Seq("a", "b"), 5),
          NodeRouteExpectedCount(1006, "06", Seq("a", "b"), 6)
        )
      )

      queryNetworkType(NetworkType.canoe) should matchTo(
        Seq(
          NodeRouteExpectedCount(1007, "07", Seq("a", "b"), 7),
          NodeRouteExpectedCount(1008, "08", Seq("a", "b"), 8)
        )
      )

      queryNetworkType(NetworkType.motorboat) should matchTo(
        Seq(
          NodeRouteExpectedCount(1009, "09", Seq("a", "b"), 9),
          NodeRouteExpectedCount(1010, "10", Seq("a", "b"), 10)
        )
      )

      queryNetworkType(NetworkType.inlineSkating) should matchTo(
        Seq(
          NodeRouteExpectedCount(1011, "11", Seq("a", "b"), 11),
          NodeRouteExpectedCount(1012, "12", Seq("a", "b"), 12)
        )
      )

      queryScopedNetworkType(NetworkScope.local, NetworkType.hiking) should matchTo(
        Seq(
          NodeRouteExpectedCount(1001, "01", Seq("a", "b"), 1)
        )
      )

      queryScopedNetworkType(NetworkScope.regional, NetworkType.hiking) should matchTo(
        Seq(
          NodeRouteExpectedCount(1002, "02", Seq("a", "b"), 2)
        )
      )

      queryScopedNetworkType(NetworkScope.national, NetworkType.cycling) should matchTo(
        Seq(
          NodeRouteExpectedCount(1003, "03", Seq("a", "b"), 3)
        )
      )

      queryScopedNetworkType(NetworkScope.local, NetworkType.cycling) should matchTo(
        Seq(
          NodeRouteExpectedCount(1004, "04", Seq("a", "b"), 4)
        )
      )

      queryScopedNetworkType(NetworkScope.regional, NetworkType.horseRiding) should matchTo(
        Seq(
          NodeRouteExpectedCount(1005, "05", Seq("a", "b"), 5)
        )
      )

      queryScopedNetworkType(NetworkScope.national, NetworkType.horseRiding) should matchTo(
        Seq(
          NodeRouteExpectedCount(1006, "06", Seq("a", "b"), 6)
        )
      )

      queryScopedNetworkType(NetworkScope.local, NetworkType.canoe) should matchTo(
        Seq(
          NodeRouteExpectedCount(1007, "07", Seq("a", "b"), 7)
        )
      )

      queryScopedNetworkType(NetworkScope.regional, NetworkType.canoe) should matchTo(
        Seq(
          NodeRouteExpectedCount(1008, "08", Seq("a", "b"), 8)
        )
      )

      queryScopedNetworkType(NetworkScope.national, NetworkType.motorboat) should matchTo(
        Seq(
          NodeRouteExpectedCount(1009, "09", Seq("a", "b"), 9)
        )
      )

      queryScopedNetworkType(NetworkScope.local, NetworkType.motorboat) should matchTo(
        Seq(
          NodeRouteExpectedCount(1010, "10", Seq("a", "b"), 10)
        )
      )

      queryScopedNetworkType(NetworkScope.regional, NetworkType.inlineSkating) should matchTo(
        Seq(
          NodeRouteExpectedCount(1011, "11", Seq("a", "b"), 11)
        )
      )

      queryScopedNetworkType(NetworkScope.national, NetworkType.inlineSkating) should matchTo(
        Seq(
          NodeRouteExpectedCount(1012, "12", Seq("a", "b"), 12)
        )
      )
    }
  }
}
