package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class NetworkNameAnalyzerTest extends UnitTest with SharedTestObjects {

  test("name abreviations") {
    val relation = newRelation(tags = Tags.from("name" -> "Réseau pédestre d'ECLA"))
    NetworkNameAnalyzer.name(relation) should equal("ECLA")
  }
}

