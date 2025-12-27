package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.route.RouteNodes
import kpn.core.doc.BaseRouteDoc
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newChangeSetContext
import kpn.core.test.TestObjects.newRouteBaseData
import kpn.core.test.TestObjects.newRouteNode
import kpn.core.util.Log
import kpn.core.util.MockLog
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.repository.RouteRepository
import kpn.server.repository.RouteTileRepository
import org.scalamock.stubs.Stub
import org.scalamock.stubs.Stubs

class BaseRouteChangeDeleterTest extends UnitTest with Stubs {

  private class Setup {
    val log: MockLog = Log.mock
    val analysisContext = new AnalysisContext()
    analysisContext.watched.routes.add(11, ElementIds.from(nodeIds = Set(1001, 1002)))
    val routeRepository: Stub[RouteRepository] = stub[RouteRepository]
    val routeTileRepository: Stub[RouteTileRepository] = stub[RouteTileRepository]
    val deleter = new BaseRouteChangeDeleterImpl(
      analysisContext,
      routeRepository,
      routeTileRepository
    )

    def delete(): ChangeSetContext = {
      deleter.loggedDelete(log, newChangeSetContext(), 11)
    }
  }

  test("delete route happy path") {

    // setup
    val setup = new Setup()
    (setup.routeRepository.findBaseRouteById _).returns { case 11 => Some(buildBaseRouteDoc()) }
    (setup.routeRepository.saveBaseRoute _).returnsWith(())
    (setup.routeTileRepository.routeTileIds _).returns { case 11 => Seq("tile-1", "tile-2") }
    (setup.routeTileRepository.deleteRouteTile _).returnsWith(())

    // execute
    val changeSetContext = setup.delete()

    // verify
    setup.analysisContext.watched.routes.size should equal(0)
    (setup.routeTileRepository.deleteRouteTile _).calls should equal(Seq("tile-1", "tile-2"))
    (setup.routeRepository.saveBaseRoute _).calls.map(_.active) should equal(Seq(false))

    assertEqual(changeSetContext.impactedNodeIds, Seq(1001, 1002))
    assertEqual(changeSetContext.impactedRouteIds, Seq(11))
    assertEqual(changeSetContext.impactedTileIds, Seq("tile-1", "tile-2"))
  }

  test("log warning if route to be deleted is not found") {

    // setup
    val setup = new Setup()
    (setup.routeRepository.findBaseRouteById _).returns { case 11 => None }
    (setup.routeRepository.saveBaseRoute _).returnsWith(())
    (setup.routeTileRepository.routeTileIds _).returns { case 11 => Seq.empty }
    (setup.routeTileRepository.deleteRouteTile _).returnsWith(())

    // execute
    val changeSetContext = setup.delete()

    // verify
    setup.analysisContext.watched.routes.size should equal(0)
    (setup.routeRepository.saveBaseRoute _).times should equal(0)
    (setup.routeTileRepository.deleteRouteTile _).times should equal(0)
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
    (setup.routeRepository.findBaseRouteById _).returns { case 11 => None }
    (setup.routeTileRepository.deleteRouteTile _).returnsWith(())
    (setup.routeTileRepository.routeTileIds _).returns { case 11 => Seq("tile-1", "tile-2") }

    // execute
    val changeSetContext = setup.delete()

    // verify
    setup.analysisContext.watched.routes.size should equal(0)
    (setup.routeRepository.saveBaseRoute _).times should equal(0)
    (setup.routeTileRepository.deleteRouteTile _).calls should equal(Seq("tile-1", "tile-2"))
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
      11,
      base = newRouteBaseData(
        nodes = RouteNodes(
          startNode = Some(newRouteNode(1001, "01")),
          endNode = Some(newRouteNode(1002, "02")),
        )
      )
    )
  }
}
