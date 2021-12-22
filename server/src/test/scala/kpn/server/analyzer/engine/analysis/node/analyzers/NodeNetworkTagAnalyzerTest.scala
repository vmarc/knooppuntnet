package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

class NodeNetworkTagAnalyzerTest extends UnitTest with SharedTestObjects {

  test("analysis is aborted when there is no tag network:type=node_network") {
    val nodeAnalysis = NodeAnalysis(newRawNode())
    NodeNetworkTagAnalyzer.analyze(nodeAnalysis).abort should equal(true)
  }

  test("analysis not aborted when network:type=node_network") {
    val nodeAnalysis = NodeAnalysis(newRawNode(tags = Tags.from("network:type" -> "node_network")))
    NodeNetworkTagAnalyzer.analyze(nodeAnalysis).abort should equal(false)
  }
}
