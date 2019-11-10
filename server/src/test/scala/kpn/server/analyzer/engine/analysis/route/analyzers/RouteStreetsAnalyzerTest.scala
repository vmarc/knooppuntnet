package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Tags
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.api.common.data.Way
import org.scalatest.FunSuite
import org.scalatest.Matchers

class RouteStreetsAnalyzerTest extends FunSuite with Matchers {

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
      networkNodes = Map(),
      loadedRoute = null,
      orphan = false,
      ways = Some(ways)
    )

    val newContext = RouteStreetsAnalyzer.analyze(context)
    newContext.streets should equal(Some(Seq("street-1", "street-2")))
  }

}
