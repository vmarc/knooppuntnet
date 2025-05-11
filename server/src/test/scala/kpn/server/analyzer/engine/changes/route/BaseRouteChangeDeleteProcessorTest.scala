package kpn.server.analyzer.engine.changes.route

import kpn.api.common.SharedTestObjects
import kpn.api.common.route.RouteNodes
import kpn.core.doc.BaseRouteDoc
import kpn.core.util.Log
import kpn.core.util.MockLog
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.repository.RouteRepository

class BaseRouteChangeDeleteProcessorTest extends UnitTest with SharedTestObjects {

  private class Setup extends SharedTestObjects {
    val log: MockLog = Log.mock
    val analysisContext = new AnalysisContext()
    analysisContext.watched.routes.add(11, ElementIds(nodeIds = Set(1001, 1002)))
    val routeRepository: RouteRepository = stub[RouteRepository]
    val processor = new BaseRouteChangeDeleteProcessor(
      analysisContext,
      routeRepository,
    )

    def process(): ChangeSetContext = {
      processor.loggedProcess(newChangeSetContext(), 11, log)
    }
  }

  test("delete route") {

    // setup
    val setup = new Setup()
    (setup.routeRepository.findBaseRouteById _).when(11).returns(Some(buildBaseRouteDoc()))

    // execute
    val updatedChangeSetContext = setup.process()

    // verify
    setup.analysisContext.watched.routes.size should equal(0)

    (setup.routeRepository.deleteRouteTiles _).verify(11).once()
    (setup.routeRepository.saveBaseRoute _).verify(
      where { (doc: BaseRouteDoc) =>
        doc.labels shouldBe empty
        true
      }
    ).once()

    assertEqual(updatedChangeSetContext.impactedTiles, Seq("tile-1", "tile-2"))
    assertEqual(updatedChangeSetContext.impactedNodeIds, Seq(1001, 1002))
    assertEqual(updatedChangeSetContext.impactedRouteIds, Seq(11))
  }

  test("log warning if route to be deleted is not found") {

    // setup
    val setup = new Setup()
    (setup.routeRepository.findBaseRouteById _).when(11).returns(None)

    // execute
    val updatedChangeSetContext = setup.process()

    // verify
    setup.analysisContext.watched.routes.size should equal(0)
    (setup.routeRepository.saveBaseRoute _).verify(*).never()
    (setup.routeRepository.deleteRouteTiles _).verify(11).once()
    assertEqual(
      setup.log.messages,
      Seq(
        "WARN route 11 not found"
      )
    )
    assertEqual(updatedChangeSetContext.impactedRouteIds, Seq(11))
  }

  private def buildBaseRouteDoc(): BaseRouteDoc = {
    newBaseRouteDoc(
      newRouteSummary(11),
      tiles = Seq("tile-1", "tile-2"),
      nodes = RouteNodes(
        startNode = Some(newRouteNode(1001, "01")),
        endNode = Some(newRouteNode(1002, "02")),
      )
    )
  }
}
