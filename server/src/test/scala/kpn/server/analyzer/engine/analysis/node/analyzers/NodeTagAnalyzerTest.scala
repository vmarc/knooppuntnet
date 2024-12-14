package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.Fact
import kpn.api.common.NetworkScope
import kpn.api.common.NetworkType
import kpn.api.common.NodeName
import kpn.api.common.SharedTestObjects
import kpn.api.custom.Day
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.node.domain.NodeTagAnalysis

class NodeTagAnalyzerTest extends UnitTest with SharedTestObjects {

  test("no tags") {
    analyze() should equal(None)
  }

  test("no name") {
    analyze(
      "network:type" -> "node_network"
    ) should equal(None)
  }

  test("??n_ref") {
    ScopedNetworkType.all.foreach { scopedNetworkType =>
      analyze(
        "network:type" -> "node_network",
        scopedNetworkType.nodeRefTagKey -> "01"
      ) should equal(
        Some(
          NodeTagAnalysis(
            "01",
            nodeNames = Seq(
              NodeName(
                networkType = scopedNetworkType.networkType,
                networkScope = scopedNetworkType.networkScope,
                name = "01",
                longName = None,
                proposed = false
              )
            ),
            lastSurvey = None,
            facts = Seq.empty
          )
        )
      )
    }
  }

  test("??n_name") {
    ScopedNetworkType.all.foreach { scopedNetworkType =>
      analyze(
        "network:type" -> "node_network",
        scopedNetworkType.nodeNameTagKey -> "01"
      ) should equal(
        Some(
          NodeTagAnalysis(
            "01",
            nodeNames = Seq(
              NodeName(
                networkType = scopedNetworkType.networkType,
                networkScope = scopedNetworkType.networkScope,
                name = "01",
                longName = Some("01"),
                proposed = false
              )
            ),
            lastSurvey = None,
            facts = Seq.empty
          )
        )
      )
    }
  }

  test("proposed:??n_ref") {
    ScopedNetworkType.all.foreach { scopedNetworkType =>
      analyze(
        "network:type" -> "node_network",
        scopedNetworkType.proposedNodeRefTagKey -> "01"
      ) should equal(
        Some(
          NodeTagAnalysis(
            "01",
            nodeNames = Seq(
              NodeName(
                networkType = scopedNetworkType.networkType,
                networkScope = scopedNetworkType.networkScope,
                name = "01",
                longName = None,
                proposed = true
              )
            ),
            lastSurvey = None,
            facts = Seq.empty
          )
        )
      )
    }
  }

  test("propsed:??n_name") {
    ScopedNetworkType.all.foreach { scopedNetworkType =>
      analyze(
        "network:type" -> "node_network",
        scopedNetworkType.proposedNodeNameTagKey -> "01"
      ) should equal(
        Some(
          NodeTagAnalysis(
            "01",
            nodeNames = Seq(
              NodeName(
                networkType = scopedNetworkType.networkType,
                networkScope = scopedNetworkType.networkScope,
                name = "01",
                longName = Some("01"),
                proposed = true
              )
            ),
            lastSurvey = None,
            facts = Seq.empty
          )
        )
      )
    }
  }

  test("survey date") {
    analyze(
      "network:type" -> "node_network",
      "rwn_ref" -> "01",
      "survey:date" -> "2020-08"
    ).get.lastSurvey should equal(Some(Day(2020, 8)))
  }

  test("survey date invalid format") {
    analyze(
      "network:type" -> "node_network",
      "rwn_ref" -> "01",
      "survey:date" -> "bla"
    ).get.facts.shouldMatchTo(Seq(Fact.NodeInvalidSurveyDate))
  }

  test("multiple scopes and network types") {
    analyze(
      "network:type" -> "node_network",
      "rwn_ref" -> "01",
      "lwn_ref" -> "02",
      "rcn_ref" -> "03"
    ) should equal(
      Some(
        NodeTagAnalysis(
          "01 / 02 / 03",
          nodeNames = Seq(
            NodeName(
              networkType = NetworkType.hiking,
              networkScope = NetworkScope.regional,
              name = "01",
              longName = None,
              proposed = false
            ),
            NodeName(
              networkType = NetworkType.hiking,
              networkScope = NetworkScope.local,
              name = "02",
              longName = None,
              proposed = false
            ),
            NodeName(
              networkType = NetworkType.cycling,
              networkScope = NetworkScope.regional,
              name = "03",
              longName = None,
              proposed = false
            )
          ),
          lastSurvey = None,
          facts = Seq.empty
        )
      )
    )
  }

  test("state=proposed") {
    analyze(
      "network:type" -> "node_network",
      "rwn_ref" -> "01",
      "state" -> "proposed"
    ) should equal(
      Some(
        NodeTagAnalysis(
          "01",
          nodeNames = Seq(
            NodeName(
              networkType = NetworkType.hiking,
              networkScope = NetworkScope.regional,
              name = "01",
              longName = None,
              proposed = true
            )
          ),
          lastSurvey = None,
          facts = Seq.empty
        )
      )
    )
  }

  private def analyze(tags: (String, String)*): Option[NodeTagAnalysis] = {
    NodeTagAnalyzer.analyze(
      newNode(
        tags = Tags.from(
          tags *
        )
      )
    )
  }
}
