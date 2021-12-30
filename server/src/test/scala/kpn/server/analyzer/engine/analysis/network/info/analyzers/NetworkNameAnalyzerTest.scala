package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class NetworkNameAnalyzerTest extends UnitTest {

  test("name abreviations") {
    NetworkNameAnalyzer.name(Tags.from("name" -> "Réseau pédestre d'ECLA")) should equal("ECLA")
  }
}

