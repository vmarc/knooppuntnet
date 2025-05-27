package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.data.MemberType
import kpn.api.common.data.raw.RawMember
import kpn.api.custom.ScopedRouteType
import kpn.core.test.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.test.Timestamps
import kpn.core.util.UnitTest
import kpn.server.repository.NodeRepositoryImpl

class NetworkNodeDocAnalyzerTest extends UnitTest with SharedTestObjects {

  test("pick up correct node docs") {

    withDatabase { database =>

      val network = newBaseNetworkDoc(
        _id = 1L,
        members = Seq(
          RawMember(MemberType.Node, 1002L, None),
          RawMember(MemberType.Node, 1003L, None),
        ),
      )

      val context = NetworkAnalysisContext(
        network,
        Timestamps.default,
        _scopedRouteTypeOption = Some(Some(ScopedRouteType.rwn)),
        _routeDetails = Some(
          Seq(
            newNetworkRouteDetail(
              id = 10L,
              nodeRefs = Seq(1001L)
            )
          )
        )
      )

      database.nodes.save(newNodeDoc(1001L))
      database.nodes.save(newNodeDoc(1002L))
      database.nodes.save(newNodeDoc(1003L))
      database.nodes.save(newNodeDoc(1004L))

      val nodeRepository = new NodeRepositoryImpl(database)
      val analyzer = new NetworkNodeDocAnalyzer(nodeRepository)
      val updatedContext = analyzer.analyze(context)

      assertEqual(
        updatedContext.nodeDocs,
        Seq(
          newNodeDoc(id = 1001L),
          newNodeDoc(id = 1002L),
          newNodeDoc(id = 1003L)
        )
      )
    }
  }
}
