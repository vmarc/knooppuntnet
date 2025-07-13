package kpn.server.analyzer.engine.analysis.node.base.analyzers

import kpn.api.common.Fact
import kpn.api.custom.Day
import kpn.api.custom.Tags
import kpn.core.test.TestObjects.newRawNode
import kpn.core.util.UnitTest

class BaseNodeSurveyAnalyzerTest extends UnitTest {

  test("survey date") {
    val context = analyze(
      "survey:date" -> "2020-08"
    )
    context.lastSurvey should equal(Some(Day(2020, 8)))
    context.facts should equal(Seq.empty)
  }

  test("survey date invalid format") {
    val context = analyze(
      "survey:date" -> "bla"
    )
    context.lastSurvey should equal(None)
    context.facts should equal(Seq(Fact.NodeInvalidSurveyDate))
  }

  private def analyze(tags: (String, String)*): BaseNodeAnalysisContext = {
    val context = BaseNodeAnalysisContext(newRawNode(tags = Tags.from(tags: _*)))
    BaseNodeSurveyAnalyzer.analyze(context)
  }
}
