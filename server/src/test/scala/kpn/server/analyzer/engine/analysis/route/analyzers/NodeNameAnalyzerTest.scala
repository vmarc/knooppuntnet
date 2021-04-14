package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.core.util.UnitTest

class NodeNameAnalyzerTest extends UnitTest {

  test("normalize") {
    NodeNameAnalyzer.normalize("1") should equal("01")
    NodeNameAnalyzer.normalize("01") should equal("01")
    NodeNameAnalyzer.normalize("101") should equal("101")
    NodeNameAnalyzer.normalize("A") should equal("A")
    NodeNameAnalyzer.normalize("A1") should equal("A1")
  }

}
