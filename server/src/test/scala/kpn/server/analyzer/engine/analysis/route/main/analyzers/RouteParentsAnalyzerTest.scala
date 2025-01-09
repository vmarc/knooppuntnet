package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.api.common.SharedTestObjects
import kpn.api.common.route.ParentRoute
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.repository.BaseRouteRepositoryImpl

class RouteParentsAnalyzerTest extends UnitTest with SharedTestObjects {

  test("two levels of parent routes") {
    withDatabase { database =>
      val baseRouteRepository = new BaseRouteRepositoryImpl(database)
      val analyzer = new RouteParentAnalyzer(baseRouteRepository)

      baseRouteRepository.save(
        newBaseRouteDoc(
          newRouteSummary(11, name = "route 11"),
          subRouteIds = Seq(12)
        )
      )
      baseRouteRepository.save(
        newBaseRouteDoc(
          newRouteSummary(12, name = "route 12"),
          subRouteIds = Seq(13)
        )
      )
      baseRouteRepository.save(
        newBaseRouteDoc(
          newRouteSummary(13, name = "route 13"),
        )
      )

      val route11 = baseRouteRepository.findById(11).get
      val route12 = baseRouteRepository.findById(12).get
      val route13 = baseRouteRepository.findById(13).get

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
  }

  test("protect against indirect self referential routes") {
    withDatabase { database =>
      val baseRouteRepository = new BaseRouteRepositoryImpl(database)
      val analyzer = new RouteParentAnalyzer(baseRouteRepository)

      baseRouteRepository.save(
        newBaseRouteDoc(
          newRouteSummary(11, name = "route 11"),
          subRouteIds = Seq(12)
        )
      )
      baseRouteRepository.save(
        newBaseRouteDoc(
          newRouteSummary(12, name = "route 12"),
          subRouteIds = Seq(13)
        )
      )
      baseRouteRepository.save(
        newBaseRouteDoc(
          newRouteSummary(13, name = "route 13"),
          subRouteIds = Seq(11)
        )
      )

      val route13 = baseRouteRepository.findById(13).get

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
}
