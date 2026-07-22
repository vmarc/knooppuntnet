package kpn.server.analyzer.engine.changes.route.main

import kpn.api.common.ReplicationId
import kpn.api.common.RouteType
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.route.ParentRoute
import kpn.core.doc.RouteDoc
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSet
import kpn.core.test.TestObjects.newRouteChange
import kpn.core.test.TestObjects.newRouteData
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.util.Log
import kpn.core.util.MockLog
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.main.RouteMainAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.context.ChangeElementIds
import kpn.server.repository.RouteRepository
import org.scalamock.stubs.Stub
import org.scalamock.stubs.Stubs

class RouteChangeProcessorTest extends UnitTest with Stubs {

  test("happy path - process new route where BaseRouteDoc already exists") {

    // setup
    val setup = new Setup()

    (setup.routeRepository.findRouteById _).returnsWith(None)
    (setup.routeRepository.findBaseRouteById _).returnsWith(
      Some(newBaseRouteDoc(11))
    )
    (setup.routeMainAnalyzer.analyze _).returnsWith(
      Some(newRouteDoc())
    )

    val routeChange = setupRouteChange(11)

    (setup.createProcessor.process _).returnsWith(
      Some(
        RouteChangeContext(
          routeChange = routeChange,
          impactedNodeIds = Seq.empty,
          impactedNetworkIds = Seq.empty
        )
      )
    )

    (setup.routeRepository.saveRoute _).returnsWith(())

    val initialContext = setupInitialChangeSetContext(11)

    // execute
    val updatedContext = setup.processor.process(initialContext)

    // verify
    (setup.routeRepository.saveRoute _).calls.map(_._id) should equal(Seq(1))

    assertEqual(
      updatedContext.changes,
      ChangeSetChanges(
        routeChanges = Seq(routeChange)
      )
    )
  }

  test("update route") {

    // setup
    val setup = new Setup()

    (setup.routeRepository.findRouteById _).returnsWith(
      Some(newRouteDoc(11))
    )
    (setup.routeRepository.findBaseRouteById _).returnsWith(
      Some(newBaseRouteDoc(11))
    )
    (setup.routeMainAnalyzer.analyze _).returnsWith(
      Some(newRouteDoc(11))
    )

    val routeChange = setupRouteChange(11)

    (setup.updateProcessor.process _).returnsWith(
      Some(
        RouteChangeContext(
          routeChange = routeChange,
          impactedNodeIds = Seq.empty,
          impactedNetworkIds = Seq.empty
        )
      )
    )

    (setup.routeRepository.saveRoute _).returnsWith(())

    val initialContext = setupInitialChangeSetContext(11)

    // execute
    val updatedContext = setup.processor.process(initialContext)

    // verify
    (setup.routeRepository.saveRoute _).calls.map(_._id) should equal(Seq(11))

    assertEqual(
      updatedContext.changes,
      ChangeSetChanges(
        routeChanges = Seq(routeChange)
      )
    )
  }

  test("when updating a route, also update the parent routes") {

    // setup
    val setup = new Setup()

    val parentRoute = newRouteDoc(11)
    val childRoute = newRouteDoc(12)
    val updatedChildRoute = newRouteDoc(
      childRoute._id,
      parentRoutes = Seq(
        ParentRoute(
          level = 1,
          routeId = parentRoute._id,
          name = "parent"
        )
      )
    )

    (setup.routeRepository.findRouteById _).returns {
      case childRoute._id => Some(childRoute)
      case parentRoute._id => Some(parentRoute)
      case _ => None
    }

    (setup.routeRepository.findBaseRouteById _).returns {
      case childRoute._id => Some(newBaseRouteDoc(childRoute._id))
      case parentRoute._id => Some(newBaseRouteDoc(parentRoute._id))
      case _ => None
    }

    (setup.routeMainAnalyzer.analyze _).returns { case (baseRouteDoc) =>
      if (baseRouteDoc._id == childRoute._id) {
        Some(updatedChildRoute)
      }
      else if (baseRouteDoc._id == parentRoute._id) {
        Some(parentRoute)
      }
      else {
        None
      }
    }

    val childRouteChange = setupRouteChange(childRoute._id)
    val parentRouteChange = setupRouteChange(parentRoute._id)

    (setup.updateProcessor.process _).returns {
      case (context: ChangeSetContext, before: RouteDoc, after: RouteDoc, routeId: Long) =>
        if (routeId == childRoute._id) {
          Some(
            RouteChangeContext(
              childRouteChange,
              Seq.empty,
              Seq.empty
            )
          )
        }
        else if (routeId == parentRoute._id) {
          Some(
            RouteChangeContext(
              parentRouteChange,
              Seq.empty,
              Seq.empty
            )
          )
        }
        else {
          None
        }
    }

    (setup.routeRepository.saveRoute _).returnsWith(())

    val initialContext = setupInitialChangeSetContext(childRoute._id)

    // execute
    val updatedContext = setup.processor.process(initialContext)

    // verify
    (setup.routeRepository.saveRoute _).calls.map(_._id) should equal(Seq(childRoute._id, parentRoute._id))

    assertEqual(
      updatedContext.changes,
      ChangeSetChanges(
        routeChanges = Seq(
          childRouteChange,
          parentRouteChange
        )
      )
    )
  }

  test("unexpected situation: process new route, but base route doc is missing") {

    // setup
    val setup = new Setup()

    (setup.routeRepository.findRouteById _).returnsWith(None)
    (setup.routeRepository.findBaseRouteById _).returnsWith(None)

    val initialContext = setupInitialChangeSetContext(11)

    // execute
    val updatedContext = setup.processor.process(initialContext)

    // verify
    setup.log.warnings should equal(
      Seq(
        "WARN unexpected: BaseRouteDoc(11) missing while processing new route"
      )
    )

    updatedContext.changes.isEmpty should equal(true)
    (setup.routeRepository.saveRoute _).calls should equal(Seq.empty)
  }

  test("unexpected: new route analysis fails") {

    // setup
    val setup = new Setup()

    (setup.routeRepository.findRouteById _).returnsWith(None)
    (setup.routeRepository.findBaseRouteById _).returnsWith(
      Some(newBaseRouteDoc(11))
    )
    (setup.routeMainAnalyzer.analyze _).returnsWith(
      None
    )

    val initialContext = setupInitialChangeSetContext(11)

    // execute
    val updatedContext = setup.processor.process(initialContext)

    // verify
    setup.log.warnings should equal(
      Seq("WARN unexpected: BaseRouteDoc(11) is active, but new RouteDoc analysis failed")
    )

    updatedContext.changes.isEmpty should equal(true)
    (setup.routeRepository.saveRoute _).calls should equal(Seq.empty)
  }

  test("new route, but BaseRouteDoc not active - nothing to do") {

    // setup
    val setup = new Setup()

    (setup.routeRepository.findRouteById _).returnsWith(None)
    (setup.routeRepository.findBaseRouteById _).returnsWith(
      Some(newBaseRouteDoc(11, active = false))
    )

    val initialContext = setupInitialChangeSetContext(11)

    // execute
    val updatedContext = setup.processor.process(initialContext)

    // verify
    setup.log.warnings should equal(Seq.empty)
    updatedContext.changes.isEmpty should equal(true)
    (setup.routeRepository.saveRoute _).calls should equal(Seq.empty)
  }

  test("unexpected: update route but BaseRouteDoc not found - deactivate RouteDoc") {

    // setup
    val setup = new Setup()

    (setup.routeRepository.findRouteById _).returnsWith(
      Some(newRouteDoc(11))
    )
    (setup.routeRepository.findBaseRouteById _).returnsWith(
      None
    )

    (setup.routeRepository.saveRoute _).returnsWith(())

    val initialContext = setupInitialChangeSetContext(11)

    // execute
    val updatedContext = setup.processor.process(initialContext)

    // verify
    setup.log.warnings should equal(
      Seq("WARN unexpected: BaseRouteDoc(11) not found while updating route")
    )

    (setup.routeRepository.saveRoute _).calls.map(_._id) should equal(Seq(11))
    (setup.routeRepository.saveRoute _).calls.map(_.active) should equal(Seq(false))
    updatedContext.changes.isEmpty should equal(true)
  }

  test("update route analysis fails") {

    // setup
    val setup = new Setup()

    (setup.routeRepository.findRouteById _).returnsWith(
      Some(newRouteDoc(11))
    )
    (setup.routeRepository.findBaseRouteById _).returnsWith(
      Some(newBaseRouteDoc(11))
    )
    (setup.routeMainAnalyzer.analyze _).returnsWith(
      None
    )

    (setup.routeRepository.saveRoute _).returnsWith(())

    val initialContext = setupInitialChangeSetContext(11)

    // execute
    val updatedContext = setup.processor.process(initialContext)

    // verify
    setup.log.warnings should equal(
      Seq("WARN unexpected: RouteDoc(11) update analysis failed")
    )

    (setup.routeRepository.saveRoute _).calls.map(_._id) should equal(Seq(11))
    (setup.routeRepository.saveRoute _).calls.map(_.active) should equal(Seq(false))
    updatedContext.changes.isEmpty should equal(true)
  }

  test("delete route") {

    // setup
    val setup = new Setup()

    (setup.routeRepository.findRouteById _).returnsWith(
      Some(newRouteDoc(11))
    )
    (setup.routeRepository.findBaseRouteById _).returnsWith(
      Some(newBaseRouteDoc(11))
    )
    (setup.routeMainAnalyzer.analyze _).returnsWith(
      Some(newRouteDoc(11))
    )

    val routeChange = setupRouteChange(11)

    (setup.deleteProcessor.process _).returnsWith(
      Some(
        RouteChangeContext(
          routeChange = routeChange,
          impactedNodeIds = Seq.empty,
          impactedNetworkIds = Seq.empty
        )
      )
    )

    (setup.routeRepository.saveRoute _).returnsWith(())

    val initialContext = setupInitialChangeSetContext(11).copy(
      baseRouteDeletedIds = Seq(11)
    )

    // execute
    val updatedContext = setup.processor.process(initialContext)

    // verify
    (setup.routeRepository.saveRoute _).calls.map(_._id) should equal(Seq(11))
    (setup.routeRepository.saveRoute _).calls.map(_.active) should equal(Seq(false))

    assertEqual(
      updatedContext.changes,
      ChangeSetChanges(
        routeChanges = Seq(routeChange)
      )
    )
  }

  private def setupInitialChangeSetContext(routeId: Long): ChangeSetContext = {
    ChangeSetContext(
      replicationId = ReplicationId(0, 0, 1),
      changeSet = newChangeSet(),
      elementIds = ChangeElementIds(),
      impactedRouteIds = Seq(routeId)
    )
  }

  private def setupRouteChange(routeId: Long): RouteChange = {
    newRouteChange(
      newChangeKey().copy(elementId = routeId),
      after = Some(
        newRouteData(
          relationId = routeId,
          routeTypes = Seq(RouteType.hiking),
        )
      )
    )
  }

  private class Setup {
    val routeMainAnalyzer: Stub[RouteMainAnalyzer] = stub[RouteMainAnalyzer]
    val routeRepository: Stub[RouteRepository] = stub[RouteRepository]
    val createProcessor: Stub[RouteChangeCreateProcessor] = stub[RouteChangeCreateProcessor]
    val updateProcessor: Stub[RouteChangeUpdateProcessor] = stub[RouteChangeUpdateProcessor]
    val deleteProcessor: Stub[RouteChangeDeleteProcessor] = stub[RouteChangeDeleteProcessor]
    val log: MockLog = Log.mock

    val processor = new RouteChangeProcessor(
      routeMainAnalyzer,
      routeRepository,
      createProcessor,
      updateProcessor,
      deleteProcessor,
      log
    )
  }
}
