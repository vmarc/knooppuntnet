package kpn.server.analyzer.engine.changes

import kpn.core.test.TestObjects.newChangeSetContext
import kpn.core.test.TestObjects.newNodeChange
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.ChangeSetInfoUpdater
import kpn.server.analyzer.engine.changes.network.base.BaseNetworkChangeProcessor
import kpn.server.analyzer.engine.changes.network.main.NetworkChangeProcessor
import kpn.server.analyzer.engine.changes.node.base.BaseNodeChangeProcessor
import kpn.server.analyzer.engine.changes.node.main.NodeChangeProcessor
import kpn.server.analyzer.engine.changes.route.base.BaseRouteChangeProcessor
import kpn.server.analyzer.engine.changes.route.main.RouteChangeProcessor
import org.scalamock.stubs.Stub
import org.scalamock.stubs.Stubs

class ChangeProcessorPipelineTest extends UnitTest with Stubs {

  test("pipeline executes all processors and triggers saving when changes are present") {
    // setup
    val setup = new Setup(hasChanges = true)
    (setup.changeSetInfoUpdater.changeSetInfo _).returnsWith(())
    (setup.changeSaver.save _).returnsWith(())
    val initialContext = newChangeSetContext()

    // execute
    val resultContext = setup.pipeline.process(initialContext)

    // verify
    setup.assertAllProcessorsCalled()
    (setup.changeSetInfoUpdater.changeSetInfo _).times should equal(1)
    (setup.changeSaver.save _).times should equal(1)
  }

  test("pipeline executes all processors but does not trigger saving when no changes") {
    // setup
    val setup = new Setup(hasChanges = false)
    (setup.changeSetInfoUpdater.changeSetInfo _).returnsWith(())
    (setup.changeSaver.save _).returnsWith(())
    val initialContext = newChangeSetContext()

    // execute
    val resultContext = setup.pipeline.process(initialContext)

    // verify
    setup.assertAllProcessorsCalled()
    (setup.changeSetInfoUpdater.changeSetInfo _).times should equal(0)
    (setup.changeSaver.save _).times should equal(0)
  }

  private class Setup(hasChanges: Boolean) {
    private val baseNodeChangeProcessor = stub[BaseNodeChangeProcessor]
    private val baseNetworkChangeProcessor = stub[BaseNetworkChangeProcessor]
    private val baseRouteChangeProcessor = stub[BaseRouteChangeProcessor]
    private val networkChangeProcessor = stub[NetworkChangeProcessor]
    private val routeChangeProcessor = stub[RouteChangeProcessor]
    private val nodeChangeProcessor = stub[NodeChangeProcessor]
    val changeSetInfoUpdater: Stub[ChangeSetInfoUpdater] = stub[ChangeSetInfoUpdater]
    val changeSaver: Stub[ChangeSaver] = stub[ChangeSaver]

    (baseNodeChangeProcessor.process _).returns { (context: ChangeSetContext) =>
      if (hasChanges) {
        val updatedChanges = context.changes.copy(nodeChanges = context.changes.nodeChanges :+ newNodeChange())
        context.copy(changes = updatedChanges)
      }
      else {
        context
      }
    }

    (baseNetworkChangeProcessor.process _).returns((context: ChangeSetContext) => context)
    (baseRouteChangeProcessor.process _).returns((context: ChangeSetContext) => context)
    (networkChangeProcessor.process _).returns((context: ChangeSetContext) => context)
    (routeChangeProcessor.process _).returns((context: ChangeSetContext) => context)
    (nodeChangeProcessor.process _).returns((context: ChangeSetContext) => context)

    val pipeline = new ChangeProcessorPipeline(
      baseNodeChangeProcessor,
      baseNetworkChangeProcessor,
      baseRouteChangeProcessor,
      networkChangeProcessor,
      routeChangeProcessor,
      nodeChangeProcessor,
      changeSetInfoUpdater,
      changeSaver
    )

    def assertAllProcessorsCalled(): Unit = {
      (baseNodeChangeProcessor.process _).times should equal(1)
      (baseNetworkChangeProcessor.process _).times should equal(1)
      (baseRouteChangeProcessor.process _).times should equal(1)
      (networkChangeProcessor.process _).times should equal(1)
      (routeChangeProcessor.process _).times should equal(1)
      (nodeChangeProcessor.process _).times should equal(1)
    }
  }
}
