package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.data.MemberType
import kpn.api.common.data.raw.RawMember
import kpn.api.custom.ScopedRouteType
import kpn.api.time.Timestamps
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newBaseNetworkDoc
import kpn.core.test.TestObjects.newNetworkBaseData
import kpn.core.test.TestObjects.newNetworkRouteDetail
import kpn.core.test.TestObjects.newNodeDoc
import kpn.server.repository.NodeRepository

class NetworkNodeDocAnalyzerTest extends MongoTest {

  test("pick up correct node docs") {

    val network = newBaseNetworkDoc(
      _id = 1L,
      base = newNetworkBaseData(
        members = Seq(
          RawMember(MemberType.Node, 1002L, None),
          RawMember(MemberType.Node, 1003L, None),
        )
      )
    )

    val context = NetworkAnalysisContext(
      network,
      Timestamps.default,
      _scopedRouteTypeOption = Some(Some(ScopedRouteType.rwn)),
      _routeDetails = Some(
        Seq(
          newNetworkRouteDetail(
            id = 10L,
            networkNodeIds = Some(Seq(1001L))
          )
        )
      )
    )

    database.nodes.save(newNodeDoc(1001L))
    database.nodes.save(newNodeDoc(1002L))
    database.nodes.save(newNodeDoc(1003L))
    database.nodes.save(newNodeDoc(1004L))

    val nodeRepository = new NodeRepository(database)
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
