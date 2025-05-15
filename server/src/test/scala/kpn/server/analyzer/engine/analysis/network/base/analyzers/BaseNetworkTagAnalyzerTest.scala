package kpn.server.analyzer.engine.analysis.network.base.analyzers

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class BaseNetworkTagAnalyzerTest extends UnitTest with SharedTestObjects {

  test("all required tags are present") {
    val context = BaseNetworkAnalysisContext(
      newRawRelation(
        tags = Tags.from(
          "network:type" -> "node_network",
          "type" -> "network",
          "network" -> "rcn",
        )
      )
    )

    val updatedContext = BaseNetworkTagAnalyzer.analyze(context)
    updatedContext.abort should equal(false)
  }

  test("abort when required tag is missing") {
    val context = BaseNetworkAnalysisContext(
      newRawRelation(
        tags = Tags.from(
          // "network:type" -> "node_network",
          "type" -> "network",
          "network" -> "rcn",
        )
      )
    )

    val updatedContext = BaseNetworkTagAnalyzer.analyze(context)
    updatedContext.abort should equal(true)
  }
}
