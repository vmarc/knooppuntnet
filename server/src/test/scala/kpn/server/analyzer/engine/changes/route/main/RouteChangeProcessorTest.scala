package kpn.server.analyzer.engine.changes.route.main

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.main.RouteMainAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.RouteRepository
import org.scalamock.stubs.Stubs

class RouteChangeProcessorTest extends UnitTest with Stubs {

  test("happy path") {
    val analysisContext = new AnalysisContext()
    val routeMainAnalyzer = stub[RouteMainAnalyzer]
    val routeRepository = stub[RouteRepository]

    val processor = new RouteChangeProcessor(
      analysisContext,
      routeMainAnalyzer,
      routeRepository
    )
  }
}
