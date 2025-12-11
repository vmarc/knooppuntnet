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

class ChangeProcessorPipelineTest extends UnitTest {

  test("pipeline executes all processors and triggers saving when changes are present") {
    val setup = new Setup(hasChanges = true)
    val initialContext = newChangeSetContext()
    val resultContext = setup.pipeline.process(initialContext)
    setup.assertAllProcessorsCalled()
    setup.changeSetInfoUpdaterCalled should equal(true)
    setup.changeSaverCalled should equal(true)
  }

  test("pipeline executes all processors but does not trigger saving when no changes") {
    val setup = new Setup(hasChanges = false)
    val initialContext = newChangeSetContext()
    val resultContext = setup.pipeline.process(initialContext)
    setup.assertAllProcessorsCalled()
    setup.changeSetInfoUpdaterCalled should equal(false)
    setup.changeSaverCalled should equal(false)
  }

  private class Setup(hasChanges: Boolean) {
    var baseNodeChangeProcessorCalled = false
    var baseNetworkChangeProcessorCalled = false
    var baseRouteChangeProcessorCalled = false
    var networkChangeProcessorCalled = false
    var routeChangeProcessorCalled = false
    var nodeChangeProcessorCalled = false
    var changeSetInfoUpdaterCalled = false
    var changeSaverCalled = false

    private val baseNodeChangeProcessor = new BaseNodeChangeProcessor {
      override def process(context: ChangeSetContext): ChangeSetContext = {
        baseNodeChangeProcessorCalled = true
        if (hasChanges) {
          val updatedChanges = context.changes.copy(nodeChanges = context.changes.nodeChanges :+ newNodeChange())
          context.copy(changes = updatedChanges)
        }
        else {
          context
        }
      }
    }
    private val baseNetworkChangeProcessor = new BaseNetworkChangeProcessor {
      override def process(context: ChangeSetContext): ChangeSetContext = {
        baseNetworkChangeProcessorCalled = true
        context
      }
    }
    private val baseRouteChangeProcessor = new BaseRouteChangeProcessor {
      override def process(context: ChangeSetContext): ChangeSetContext = {
        baseRouteChangeProcessorCalled = true
        context
      }
    }
    private val networkChangeProcessor = new NetworkChangeProcessor {
      override def process(context: ChangeSetContext): ChangeSetContext = {
        networkChangeProcessorCalled = true
        context
      }
    }
    private val routeChangeProcessor = new RouteChangeProcessor {
      override def process(context: ChangeSetContext): ChangeSetContext = {
        routeChangeProcessorCalled = true
        context
      }
    }
    private val nodeChangeProcessor = new NodeChangeProcessor {
      override def process(context: ChangeSetContext): ChangeSetContext = {
        nodeChangeProcessorCalled = true
        context
      }
    }
    private val changeSetInfoUpdater = new ChangeSetInfoUpdater {
      override def changeSetInfo(changeSetId: Long): Unit = {
        changeSetInfoUpdaterCalled = true
      }
    }
    private val changeSaver = new ChangeSaver {
      override def save(context: ChangeSetContext): Unit = {
        changeSaverCalled = true
      }
    }

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
      baseNodeChangeProcessorCalled should equal(true)
      baseNetworkChangeProcessorCalled should equal(true)
      baseRouteChangeProcessorCalled should equal(true)
      networkChangeProcessorCalled should equal(true)
      routeChangeProcessorCalled should equal(true)
      nodeChangeProcessorCalled should equal(true)
    }

    changeSetInfoUpdaterCalled should equal(false)
    changeSaverCalled should equal(false)
  }
}
