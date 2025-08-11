package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.route.RouteNodes
import kpn.core.doc.BaseRouteDoc
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newChangeSetContext
import kpn.core.test.TestObjects.newRouteNode
import kpn.core.test.TestObjects.newRouteSummary
import kpn.core.util.Log
import kpn.core.util.MockLog
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.repository.RouteRepository
import org.scalamock.scalatest.MockFactory

class BaseRouteChangeDeleterTest extends UnitTest with MockFactory {

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
    setup.routeRepository.findBaseRouteById.when(11).returns(Some(buildBaseRouteDoc()))
    setup.routeRepository.routeTileIds.when(11).returns(Seq("tile-1", "tile-2"))

    // execute
    val changeSetContext = setup.delete()

    // verify
    setup.analysisContext.watched.routes.size should equal(0)

    setup.routeRepository.deleteRouteTile.verify("tile-1").once()
    setup.routeRepository.deleteRouteTile.verify("tile-2").once()

    setup.routeRepository.saveBaseRoute.verify(
      where { (doc: BaseRouteDoc) =>
        !doc.active
      }
    ).once()

    assertEqual(changeSetContext.impactedNodeIds, Seq(1001, 1002))
    assertEqual(changeSetContext.impactedRouteIds, Seq(11))
    assertEqual(changeSetContext.impactedTileIds, Seq("tile-1", "tile-2"))
  }

  test("log warning if route to be deleted is not found") {

    // setup
    val setup = new Setup()
    setup.routeRepository.findBaseRouteById.when(11).returns(None)
    setup.routeRepository.routeTileIds.when(11).returns(Seq.empty)

    // execute
    val changeSetContext = setup.delete()

    // verify
    setup.analysisContext.watched.routes.size should equal(0)
    setup.routeRepository.saveBaseRoute.verify(*).never()
    setup.routeRepository.deleteRouteTile.verify(*).never()
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
    setup.routeRepository.findBaseRouteById.when(11).returns(None)
    setup.routeRepository.routeTileIds.when(11).returns(Seq("tile-1", "tile-2"))

    // execute
    val changeSetContext = setup.delete()

    // verify
    setup.analysisContext.watched.routes.size should equal(0)
    setup.routeRepository.saveBaseRoute.verify(*).never()
    setup.routeRepository.deleteRouteTile.verify("tile-1").once()
    setup.routeRepository.deleteRouteTile.verify("tile-2").once()
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
