package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.common.SharedTestObjects
import kpn.api.custom.ScopedNetworkType
import kpn.core.doc.NetworkNodeMember
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext

class NetworkInfoNodeDocAnalyzerTest extends UnitTest with SharedTestObjects {

  test("pick up correct node docs") {

    withDatabase { database =>

      val networkDoc = newNetwork(
        _id = 1L,
        nodeMembers = Seq(
          NetworkNodeMember(1002L, None),
          NetworkNodeMember(1003L, None),
        )
      )

      val context = NetworkInfoAnalysisContext(
        defaultTimestamp,
        networkDoc,
        scopedNetworkTypeOption = Some(ScopedNetworkType.rwn),
        routeDetails = Seq(
          newNetworkInfoRouteDetail(
            id = 10L,
            nodeRefs = Seq(1001L)
          )
        )
      )

      database.nodes.save(newNodeDoc(1001L))
      database.nodes.save(newNodeDoc(1002L))
      database.nodes.save(newNodeDoc(1003L))
      database.nodes.save(newNodeDoc(1004L))

      val analyzer = new NetworkInfoNodeDocAnalyzer(database)
      val updatedContext = analyzer.analyze(context)

      updatedContext.nodeDocs.shouldMatchTo(
        Seq(
          newNodeDoc(id = 1001L),
          newNodeDoc(id = 1002L),
          newNodeDoc(id = 1003L)
        )
      )
    }
  }
}
