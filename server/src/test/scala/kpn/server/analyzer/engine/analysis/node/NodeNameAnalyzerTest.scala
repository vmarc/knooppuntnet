package kpn.server.analyzer.engine.analysis.node

import kpn.core.util.UnitTest

class NodeNameAnalyzerTest extends UnitTest {

  test("normalize") {
    OldNodeNameAnalyzer.normalize("1") should equal("01")
    OldNodeNameAnalyzer.normalize("01") should equal("01")
    OldNodeNameAnalyzer.normalize("101") should equal("101")
    OldNodeNameAnalyzer.normalize("A") should equal("A")
    OldNodeNameAnalyzer.normalize("A1") should equal("A1")
  }

}
