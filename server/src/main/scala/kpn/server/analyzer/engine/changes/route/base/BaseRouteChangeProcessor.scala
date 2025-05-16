package kpn.server.analyzer.engine.changes.route.base

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
      val context = processChanges(
        changeSetContext,
        Seq(
          (routeElementChanges.creates, createProcessor.process),
          (routeElementChanges.updates, updateProcessor.process),
          (routeElementChanges.deletes, deleteProcessor.process)
        )
      )
      (
        s"${routeElementChanges.size} base routes",
        context.copy(
          baseRouteCreatedIds = routeElementChanges.creates,
          baseRouteUpdatedIds = routeElementChanges.updates,
          baseRouteDeletedIds = routeElementChanges.deletes,
        )
      )
    }
  }

  private def processChanges(
    initialContext: ChangeSetContext,
    changes: Seq[(Seq[Long], BaseRouteChangeSubProcessor)]
  ): ChangeSetContext = {
    changes.foldLeft(initialContext) { case (context, (routeIds, processor)) =>
      processRouteChanges(context, routeIds, processor)
    }
  }

  private def processRouteChanges(
    initialContext: ChangeSetContext,
    routeIds: Seq[Long],
    processor: BaseRouteChangeSubProcessor
  ): ChangeSetContext = {
    routeIds.foldLeft(initialContext) { (context, routeId) =>
      processor.process(context, routeId)
    }
  }
}

