package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.api.common.Fact.RouteUnexpectedRelation
import kpn.api.common.data.MemberType
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newRouteBaseData
import kpn.core.test.TestObjects.newRouteMemberInfo
import kpn.core.test.TestObjects.newRouteRelation
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.repository.RouteRepository

class RouteIdsAnalyzerTest extends MongoTest {

  test("route id when there is no subRelationTree") {

    // setup
    val baseRouteRepository = new RouteRepository(database)
    val routeIdsAnalyzer = new RouteIdsAnalyzer(baseRouteRepository)

    val baseRouteDoc = newBaseRouteDoc(11L)
    baseRouteRepository.saveBaseRoute(baseRouteDoc)

    // execute
    val context = routeIdsAnalyzer.analyze(RouteAnalysisContext(baseRouteDoc))

    // verify
    assertEqual(
      context.routeIds,
      Seq(11)
    )
  }

  test("route ids from subRelationTree") {

    // setup
    val baseRouteRepository = new RouteRepository(database)
    val routeIdsAnalyzer = new RouteIdsAnalyzer(baseRouteRepository)

    val baseRouteDoc = newBaseRouteDoc(
      11L,
      subRelationTree = Some(
        newRouteRelation(
          1,
          relations = Seq(
            newRouteRelation(2),
            newRouteRelation(
              3,
              relations = Seq(
                newRouteRelation(4)
              )
            )
          )
        )
      )
    )

    baseRouteRepository.saveBaseRoute(baseRouteDoc)
    baseRouteRepository.saveBaseRoute(newBaseRouteDoc(1L))
    baseRouteRepository.saveBaseRoute(newBaseRouteDoc(2L))
    baseRouteRepository.saveBaseRoute(newBaseRouteDoc(3L))
    baseRouteRepository.saveBaseRoute(newBaseRouteDoc(4L))

    // execute
    val context = routeIdsAnalyzer.analyze(RouteAnalysisContext(baseRouteDoc))

    // verify
    assertEqual(
      context.routeIds,
      Seq(
        1,
        2,
        3,
        4,
        11
      )
    )
  }

  test("relation ids from route members") {

    // setup
    val baseRouteRepository = new RouteRepository(database)
    val routeIdsAnalyzer = new RouteIdsAnalyzer(baseRouteRepository)

    val baseRouteDoc = newBaseRouteDoc(
      11L,
      base = newRouteBaseData(
        members = Seq(
          newRouteMemberInfo(13, MemberType.Relation)
        )
      )
    )

    baseRouteRepository.saveBaseRoute(baseRouteDoc)

    // execute
    val context = routeIdsAnalyzer.analyze(RouteAnalysisContext(baseRouteDoc))

    // verify
    assertEqual(
      context.routeIds,
      Seq(11)
    )
    assertEqual(
      context.facts,
      Seq(RouteUnexpectedRelation)
    )
    assertEqual(
      context.unexpectedRelationIds,
      Seq(13)
    )
  }
}
