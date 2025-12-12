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
import org.scalamock.scalatest.MockFactory

class ChangeProcessorPipelineTest extends UnitTest with MockFactory {

  test("pipeline executes all processors and triggers saving when changes are present") {
    val setup = new Setup(hasChanges = true)
    val initialContext = newChangeSetContext()
    val resultContext = setup.pipeline.process(initialContext)
    setup.assertAllProcessorsCalled()
    (setup.changeSetInfoUpdater.changeSetInfo _).verify(*).once()
    (setup.changeSaver.save _).verify(*).once()
  }

  test("pipeline executes all processors but does not trigger saving when no changes") {
    val setup = new Setup(hasChanges = false)
    val initialContext = newChangeSetContext()
    val resultContext = setup.pipeline.process(initialContext)
    setup.assertAllProcessorsCalled()
    (setup.changeSetInfoUpdater.changeSetInfo _).verify(*).never()
    (setup.changeSaver.save _).verify(*).never()
  }

  private class Setup(hasChanges: Boolean) {
    private val baseNodeChangeProcessor = stub[BaseNodeChangeProcessor]
    private val baseNetworkChangeProcessor = stub[BaseNetworkChangeProcessor]
    private val baseRouteChangeProcessor = stub[BaseRouteChangeProcessor]
    private val networkChangeProcessor = stub[NetworkChangeProcessor]
    private val routeChangeProcessor = stub[RouteChangeProcessor]
    private val nodeChangeProcessor = stub[NodeChangeProcessor]
    val changeSetInfoUpdater: ChangeSetInfoUpdater = stub[ChangeSetInfoUpdater]
    val changeSaver: ChangeSaver = stub[ChangeSaver]

    (baseNodeChangeProcessor.process _).when(*).onCall { (context: ChangeSetContext) =>
      if (hasChanges) {
        val updatedChanges = context.changes.copy(nodeChanges = context.changes.nodeChanges :+ newNodeChange())
        context.copy(changes = updatedChanges)
      }
      else {
        context
      }
    }

    (baseNetworkChangeProcessor.process _).when(*).onCall((context: ChangeSetContext) => context)
    (baseRouteChangeProcessor.process _).when(*).onCall((context: ChangeSetContext) => context)
    (networkChangeProcessor.process _).when(*).onCall((context: ChangeSetContext) => context)
    (routeChangeProcessor.process _).when(*).onCall((context: ChangeSetContext) => context)
    (nodeChangeProcessor.process _).when(*).onCall((context: ChangeSetContext) => context)

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
      (baseNodeChangeProcessor.process _).verify(*).once()
      (baseNetworkChangeProcessor.process _).verify(*).once()
      (baseRouteChangeProcessor.process _).verify(*).once()
      (networkChangeProcessor.process _).verify(*).once()
      (routeChangeProcessor.process _).verify(*).once()
      (nodeChangeProcessor.process _).verify(*).once()
    }
  }
}
