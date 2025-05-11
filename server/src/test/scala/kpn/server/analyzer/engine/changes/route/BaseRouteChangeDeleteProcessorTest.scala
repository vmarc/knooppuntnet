package kpn.server.analyzer.engine.changes.route

import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects
import kpn.api.common.route.RouteNodes
import kpn.core.doc.BaseRouteDoc
import kpn.core.util.Log
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.repository.RouteRepository

class BaseRouteChangeDeleteProcessorTest extends UnitTest with SharedTestObjects {

  test("delete route") {

    // setup
    val analysisContext = new AnalysisContext()
    analysisContext.watched.routes.add(11, ElementIds(nodeIds = Set(1001, 1002)))

    val routeRepository = stub[RouteRepository]
    (routeRepository.findBaseRouteById _).when(11).returns(Some(buildBaseRouteDoc()))

    val processor = new BaseRouteChangeDeleteProcessor(
      analysisContext,
      routeRepository,
    )

    // execute
    val updatedChangeSetContext = processor.process(buildChangeSetContext(), 11)

    // verify
    analysisContext.watched.routes.size should equal(0)

    (routeRepository.deleteRouteTiles _).verify(11).once()
    (routeRepository.saveBaseRoute _).verify(
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
    val analysisContext = new AnalysisContext()
    analysisContext.watched.routes.add(11, ElementIds(nodeIds = Set(1001, 1002)))

    val routeRepository = stub[RouteRepository]

    (routeRepository.findBaseRouteById _).when(11).returns(None)

    val processor = new BaseRouteChangeDeleteProcessor(
      analysisContext,
      routeRepository,
    )

    val log = Log.mock

    // execute
    val updatedChangeSetContext = processor.loggedProcess(buildChangeSetContext(), 11, log)

    // verify
    analysisContext.watched.routes.size should equal(0)

    (routeRepository.saveBaseRoute _).verify(*).never()

    (routeRepository.deleteRouteTiles _).verify(11).once()

    assertEqual(
      log.messages,
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

  private def buildChangeSetContext(): ChangeSetContext = {
    ChangeSetContext(
      ReplicationId(1),
      newChangeSet(),
      ElementIds()
    )
  }
}
