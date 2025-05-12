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

class BaseRouteChangeDeleterTest extends UnitTest with SharedTestObjects {

  private class Setup {
    val log: MockLog = Log.mock
    val analysisContext = new AnalysisContext()
    analysisContext.watched.routes.add(11, ElementIds(nodeIds = Set(1001, 1002)))
    val routeRepository: RouteRepository = stub[RouteRepository]
    val deleter = new BaseRouteChangeDeleterImpl(
      analysisContext,
      routeRepository,
    )

    def delete(): ChangeSetContext = {
      deleter.loggedDelete(log, newChangeSetContext(), 11)
    }
  }

  test("delete route happy path") {

    // setup
    val setup = new Setup()
    (setup.routeRepository.findBaseRouteById _).when(11).returns(Some(buildBaseRouteDoc()))
    (setup.routeRepository.routeTileIds _).when(11).returns(Seq("tile-1", "tile-2"))

    // execute
    val changeSetContext = setup.delete()

    // verify
    setup.analysisContext.watched.routes.size should equal(0)

    (setup.routeRepository.deleteRouteTile _).verify("tile-1").once()
    (setup.routeRepository.deleteRouteTile _).verify("tile-2").once()

    (setup.routeRepository.saveBaseRoute _).verify(
      where { (doc: BaseRouteDoc) =>
        doc.labels == Seq.empty
      }
    ).once()

    assertEqual(changeSetContext.impactedNodeIds, Seq(1001, 1002))
    assertEqual(changeSetContext.impactedRouteIds, Seq(11))
    assertEqual(changeSetContext.impactedTileIds, Seq("tile-1", "tile-2"))
  }

  test("log warning if route to be deleted is not found") {

    // setup
    val setup = new Setup()
    (setup.routeRepository.findBaseRouteById _).when(11).returns(None)
    (setup.routeRepository.routeTileIds _).when(11).returns(Seq.empty)

    // execute
    val changeSetContext = setup.delete()

    // verify
    setup.analysisContext.watched.routes.size should equal(0)
    (setup.routeRepository.saveBaseRoute _).verify(*).never()
    (setup.routeRepository.deleteRouteTile _).verify(*).never()
    assertEqual(
      setup.log.messages,
      Seq(
        "WARN route 11 not found"
      )
    )
    assertEqual(changeSetContext.impactedRouteIds, Seq(11))
    assertEqual(changeSetContext.impactedTileIds, Seq.empty)
  }

  test("log warning if route to be deleted is not found, but tile docs are found and deleted") {

    // setup
    val setup = new Setup()
    (setup.routeRepository.findBaseRouteById _).when(11).returns(None)
    (setup.routeRepository.routeTileIds _).when(11).returns(Seq("tile-1", "tile-2"))

    // execute
    val changeSetContext = setup.delete()

    // verify
    setup.analysisContext.watched.routes.size should equal(0)
    (setup.routeRepository.saveBaseRoute _).verify(*).never()
    (setup.routeRepository.deleteRouteTile _).verify("tile-1").once()
    (setup.routeRepository.deleteRouteTile _).verify("tile-2").once()
    assertEqual(
      setup.log.messages,
      Seq(
        "WARN route 11 not found"
      )
    )
    assertEqual(changeSetContext.impactedRouteIds, Seq(11))
    assertEqual(changeSetContext.impactedTileIds, Seq("tile-1", "tile-2"))
  }

  private def buildBaseRouteDoc(): BaseRouteDoc = {
    newBaseRouteDoc(
      newRouteSummary(11),
      nodes = RouteNodes(
        startNode = Some(newRouteNode(1001, "01")),
        endNode = Some(newRouteNode(1002, "02")),
      )
    )
  }
}
