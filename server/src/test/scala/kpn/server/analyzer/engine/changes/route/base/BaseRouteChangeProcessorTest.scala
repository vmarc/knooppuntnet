package kpn.server.analyzer.engine.changes.route.base

import kpn.core.test.TestObjects.newChangeSetContext
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges
import org.scalamock.scalatest.MockFactory

class BaseRouteChangeProcessorTest extends UnitTest with MockFactory {

  private class Setup {
    val analyzer: BaseRouteChangeAnalyzer = stub[BaseRouteChangeAnalyzer]
    private val createProcessor = new BaseRouteChangeCreateProcessor {
      override def process(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
        // add routeId to baseRouteCreatedIds to indicate that processor was called
        changeSetContext.copy(baseRouteCreatedIds = changeSetContext.baseRouteCreatedIds :+ routeId)
      }
    }
    private val updateProcessor = new BaseRouteChangeUpdateProcessor {
      override def process(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
        // add routeId to baseRouteUpdatedIds to indicate that processor was called
        changeSetContext.copy(baseRouteUpdatedIds = changeSetContext.baseRouteUpdatedIds :+ routeId)
      }
    }
    private val deleteProcessor = new BaseRouteChangeDeleteProcessor {
      override def process(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
        // add routeId to baseRouteDeletedIds to indicate that processor was called
        changeSetContext.copy(baseRouteDeletedIds = changeSetContext.baseRouteDeletedIds :+ routeId)
      }
    }

    val processor = new BaseRouteChangeProcessorImpl(
      analyzer,
      createProcessor,
      updateProcessor,
      deleteProcessor
    )
  }

  test("the respective subprocessors are called for creates, updates and deletes") {

    val setup = new Setup()

    val routeChanges = ElementChanges(
      creates = Seq(1L, 2L),
      updates = Seq(3L, 4L),
      deletes = Seq(5L, 6L)
    )

    (setup.analyzer.analyze _).when(*).returns(routeChanges)

    val resultContext = setup.processor.process(newChangeSetContext())

    resultContext.baseRouteCreatedIds should equal(Seq(1, 2))
    resultContext.baseRouteUpdatedIds should equal(Seq(3, 4))
    resultContext.baseRouteDeletedIds should equal(Seq(5, 6))
  }

  test("no changes") {

    val setup = new Setup()

    val routeChanges = ElementChanges()
    (setup.analyzer.analyze _).when(*).returns(routeChanges)

    val initialContext = newChangeSetContext()

    val resultContext = setup.processor.process(initialContext)

    resultContext should equal(initialContext)
  }
}
