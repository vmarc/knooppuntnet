package kpn.server.analyzer.engine.analysis.network.base.analyzers

import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.test.TestObjects.newRawRelation
import kpn.core.util.UnitTest

class BaseNetworkNameAnalyzerTest extends UnitTest {

  test("name abreviations") {
    val updatedContext = analyze(Tags.from("name" -> "Réseau pédestre d'ECLA"))
    updatedContext.name should equal(Some("ECLA"))
  }

  test("no name") {
    val updatedContext = analyze(Seq.empty)
    updatedContext.name should equal(None)
  }

  private def analyze(tags: Seq[Tag]): BaseNetworkAnalysisContext = {
    val relation = newRawRelation(tags = tags)
    val context = BaseNetworkAnalysisContext(relation)
    BaseNetworkNameAnalyzer.analyze(context)
  }
}

