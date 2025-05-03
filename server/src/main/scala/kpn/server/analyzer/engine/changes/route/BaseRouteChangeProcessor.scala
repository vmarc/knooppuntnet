package kpn.server.analyzer.engine.changes.route

import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeProcessor
import kpn.server.analyzer.engine.changes.ChangeSetContext
import org.springframework.stereotype.Component

@Component
class BaseRouteChangeProcessor(
  analyzer: BaseRouteChangeAnalyzer,
  createProcessor: BaseRouteChangeCreateProcessor,
  updateProcessor: BaseRouteChangeUpdateProcessor,
  deleteProcessor: BaseRouteChangeDeleteProcessor,
) extends ChangeProcessor {

  private val log = Log(classOf[BaseRouteChangeProcessor])

  def process(changeSetContext: ChangeSetContext): ChangeSetContext = {
    log.debugElapsed {
      val routeElementChanges = analyzer.analyze(changeSetContext)
      val changeSetContext1 = createProcessor.process(changeSetContext, routeElementChanges.creates)
      val changeSetContext2 = updateProcessor.process(changeSetContext1, routeElementChanges.updates)
      val changeSetContext3 = deleteProcessor.process(changeSetContext2, routeElementChanges.deletes)
      (
        s"${routeElementChanges.elementIds.size} base routes",
        changeSetContext3
      )
    }
  }
}
