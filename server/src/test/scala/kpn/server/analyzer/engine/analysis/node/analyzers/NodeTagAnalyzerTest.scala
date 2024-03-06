package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.NodeName
import kpn.api.common.SharedTestObjects
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.node.domain.NodeTagAnalysis

class NodeTagAnalyzerTest extends UnitTest with SharedTestObjects {

  test("no tags") {
    NodeTagAnalyzer.analyze(Tags.empty) should equal(None)
  }

  test("no name") {
    NodeTagAnalyzer.analyze(
      Tags.from(
        "network:type" -> "node_network"
      )
    ) should equal(None)
  }

  test("??n_ref") {
    ScopedNetworkType.all.foreach { scopedNetworkType =>
      NodeTagAnalyzer.analyze(
        Tags.from(
          "network:type" -> "node_network",
          scopedNetworkType.nodeRefTagKey -> "01"
        )
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
      NodeTagAnalyzer.analyze(
        Tags.from(
          "network:type" -> "node_network",
          scopedNetworkType.nodeNameTagKey -> "01"
        )
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
      NodeTagAnalyzer.analyze(
        Tags.from(
          "network:type" -> "node_network",
          scopedNetworkType.proposedNodeRefTagKey -> "01"
        )
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
      NodeTagAnalyzer.analyze(
        Tags.from(
          "network:type" -> "node_network",
          scopedNetworkType.proposedNodeNameTagKey -> "01"
        )
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
    NodeTagAnalyzer.analyze(
      Tags.from(
        "network:type" -> "node_network",
        "rwn_ref" -> "01",
        "survey:date" -> "2020-08"
      )
    ).get.lastSurvey should equal(Some(Day(2020, 8)))
  }

  test("survey date invalid format") {
    NodeTagAnalyzer.analyze(
      Tags.from(
        "network:type" -> "node_network",
        "rwn_ref" -> "01",
        "survey:date" -> "bla"
      )
    ).get.facts.shouldMatchTo(Seq(Fact.NodeInvalidSurveyDate))
  }

  test("multiple scopes and network types") {
    NodeTagAnalyzer.analyze(
      Tags.from(
        "network:type" -> "node_network",
        "rwn_ref" -> "01",
        "lwn_ref" -> "02",
        "rcn_ref" -> "03"
      )
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
    NodeTagAnalyzer.analyze(
      Tags.from(
        "network:type" -> "node_network",
        "rwn_ref" -> "01",
        "state" -> "proposed"
      )
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
}
