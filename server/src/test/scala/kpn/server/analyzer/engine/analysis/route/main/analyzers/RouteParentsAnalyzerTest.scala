package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.api.common.route.ParentRoute
import kpn.core.test.MongoTest
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.repository.RouteRepositoryImpl

class RouteParentsAnalyzerTest extends MongoTest {

  test("two levels of parent routes") {
    val baseRouteRepository = new RouteRepositoryImpl(database)
    val analyzer = new RouteParentAnalyzer(baseRouteRepository)

    baseRouteRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(11, name = "route 11"),
        subRouteIds = Seq(12)
      )
    )
    baseRouteRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(12, name = "route 12"),
        subRouteIds = Seq(13)
      )
    )
    baseRouteRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(13, name = "route 13"),
      )
    )

    val route11 = baseRouteRepository.findBaseRouteById(11).get
    val route12 = baseRouteRepository.findBaseRouteById(12).get
    val route13 = baseRouteRepository.findBaseRouteById(13).get

    assertEqual(
      analyzer.analyze(RouteAnalysisContext(route11)).parentRoutes,
      Seq.empty
    )

    assertEqual(
      analyzer.analyze(RouteAnalysisContext(route12)).parentRoutes,
      Seq(
        ParentRoute(
          1,
          11,
          "route 11"
        )
      )
    )

    assertEqual(
      analyzer.analyze(RouteAnalysisContext(route13)).parentRoutes,
      Seq(
        ParentRoute(
          1,
          12,
          "route 12"
        ),
        ParentRoute(
          2,
          11,
          "route 11"
        )
      )
    )
  }

  test("protect against indirect self referential routes") {
    val baseRouteRepository = new RouteRepositoryImpl(database)
    val analyzer = new RouteParentAnalyzer(baseRouteRepository)

    baseRouteRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(11, name = "route 11"),
        subRouteIds = Seq(12)
      )
    )
    baseRouteRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(12, name = "route 12"),
        subRouteIds = Seq(13)
      )
    )
    baseRouteRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(13, name = "route 13"),
        subRouteIds = Seq(11)
      )
    )

    val route13 = baseRouteRepository.findBaseRouteById(13).get

    assertEqual(
      analyzer.analyze(RouteAnalysisContext(route13)).parentRoutes,
      Seq(
        ParentRoute(
          1,
          12,
          "route 12"
        ),
        ParentRoute(
          2,
          11,
          "route 11"
        ),
        ParentRoute(
          3,
          13,
          "route 13"
        )
      )
    )
  }
}
