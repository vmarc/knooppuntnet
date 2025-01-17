package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.Fact
import kpn.api.common.NetworkFact
import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Ref
import kpn.core.util.UnitTest

class NetworkInfoNodeMemberMissingAnalyzerTest extends UnitTest with SharedTestObjects {

  test("error if node not defined in relation") {
    analyze(definedInRelation = false) should equal(
      Seq(
        NetworkFact(
          Fact.NodeMemberMissing,
          Some("node"),
          None,
          Some(Seq(Ref(1001, "01"))),
          None
        )
      )
    )
  }

  test("no error if node defined in relation") {
    analyze(definedInRelation = true) should equal(Seq.empty)
  }

  test("no error if proposed node not defined in regular non-proposed network") {
    analyze(definedInRelation = false, nodeProposed = true) should equal(Seq.empty)
  }

  test("no error if non-proposed node not defined in proposed network") {
    analyze(definedInRelation = false, networkProposed = true) should equal(Seq.empty)
  }

  private def analyze(definedInRelation: Boolean, nodeProposed: Boolean = false, networkProposed: Boolean = false): Seq[NetworkFact] = {

    val nodeDetails = Seq(
      newNetworkInfoNodeDetail(
        1001L,
        "01",
        definedInRelation = definedInRelation,
        proposed = nodeProposed
      )
    )

    val context = NetworkAnalysisContext(
      null,
      null,
      _nodeDetails = Some(nodeDetails),
      _proposed = Some(networkProposed)
    )
    NetworkInfoNodeMemberMissingAnalyzer.analyze(context).networkFacts
  }
}
