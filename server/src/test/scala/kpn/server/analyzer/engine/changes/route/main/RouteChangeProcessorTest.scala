package kpn.server.analyzer.engine.changes.route.main

import kpn.api.common.ReplicationId
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newChangeSet
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.test.TestObjects.newRouteSummary
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.main.RouteMainAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.context.ChangeElementIds
import kpn.server.repository.RouteRepository
import org.scalamock.stubs.Stubs

class RouteChangeProcessorTest extends UnitTest with Stubs {

  test("happy path - process new route where BaseRouteDoc already exists") {

    // setup
    val analysisContext = new AnalysisContext()
    val routeMainAnalyzer = stub[RouteMainAnalyzer]
    val routeRepository = stub[RouteRepository]

    (routeRepository.findRouteById _).returnsWith(None)
    (routeRepository.findBaseRouteById _).returnsWith(
      Some(
        newBaseRouteDoc(
          newRouteSummary(1),
        )
      )
    )
    (routeMainAnalyzer.analyze _).returnsWith(
      Some(newRouteDoc(newRouteSummary(1)))
    )

    (routeRepository.saveRoute _).returnsWith(())

    val processor = new RouteChangeProcessor(
      analysisContext,
      routeMainAnalyzer,
      routeRepository
    )

    val initialContext = ChangeSetContext(
      replicationId = ReplicationId(0, 0, 1),
      changeSet = newChangeSet(),
      elementIds = ChangeElementIds(),
      impactedRouteIds = Seq(1)
    )

    // execute
    val updatedContext = processor.process(initialContext)

    // verify
    (routeRepository.saveRoute _).calls.map(_._id) should equal(Seq(1))

    assertEqual(
      updatedContext,
      ChangeSetContext(
        replicationId = ReplicationId(0, 0, 1),
        changeSet = newChangeSet(),
        elementIds = ChangeElementIds(),
      )
    )
  }
}
