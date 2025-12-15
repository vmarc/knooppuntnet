package kpn.server.analyzer.engine.changes.route.main

import kpn.api.common.ReplicationId
import kpn.api.common.RouteType
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSet
import kpn.core.test.TestObjects.newMetaData
import kpn.core.test.TestObjects.newRouteChange
import kpn.core.test.TestObjects.newRouteData
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.test.TestObjects.newRouteSummary
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.main.RouteMainAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
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
    val createProcessor = stub[RouteChangeCreateProcessor]
    val updateProcessor = stub[RouteChangeUpdateProcessor]
    val deleteProcessor = stub[RouteChangeDeleteProcessor]

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

    val routeChange = newRouteChange(
      newChangeKey().copy(elementId = 1),
      after = Some(
        newRouteData(
          relationId = 1,
          meta = newMetaData(changeSetId = 1),
          routeTypes = Seq(RouteType.hiking),
        )
      ),
      happy = true,
      impact = true,
      locationHappy = true,
      locationImpact = true
    )

    (createProcessor.process _).returnsWith(
      Some(
        RouteChangeContext(
          routeChange = routeChange,
          impactedNodeIds = Seq.empty,
          impactedNetworkIds = Seq.empty
        )
      )
    )

    (routeRepository.saveRoute _).returnsWith(())

    val processor = new RouteChangeProcessor(
      analysisContext,
      routeMainAnalyzer,
      routeRepository,
      createProcessor,
      updateProcessor,
      deleteProcessor
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
      updatedContext.changes,
      ChangeSetChanges(
        routeChanges = Seq(routeChange)
      )
    )
  }
}
