package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.data.Way
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.context.AnalysisContext

class RouteStreetsAnalyzerTest extends UnitTest {

  ignore("collect streets") {

    val d = new RouteTestData("01-02") {
      memberWay(11, Tags.from(), "")
      memberWay(12, Tags.from("name" -> "street-2"), "")
      memberWay(13, Tags.from(), "")
      memberWay(14, Tags.from("name" -> "street-1"), "")
      memberWay(15, Tags.from("name" -> "street-2"), "")
      memberWay(16, Tags.from("name" -> "street-1"), "")
    }

    val ways: Seq[Way] = d.data.ways.values.toSeq

    val analysisContext = new AnalysisContext()

    val context = RouteAnalysisContext(
      analysisContext,
      relation = null,
      loadedRoute = null,
      orphan = false,
      Map.empty,
      ways = Some(ways)
    )

    val newContext = RouteStreetsAnalyzer.analyze(context)
    newContext.streets should equal(Some(Seq("street-1", "street-2")))
  }

}
