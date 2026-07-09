package kpn.server.analyzer.engine.changes.route.base

import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeProcessor
import kpn.server.analyzer.engine.changes.ChangeSetContext
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

case class ChangeTask(processor: BaseRouteChangeSubProcessor, routeIds: Seq[Long])

@Component
@Profile(Array("analysis"))
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
          ChangeTask(createProcessor, routeElementChanges.creates),
          ChangeTask(updateProcessor, routeElementChanges.updates),
          ChangeTask(deleteProcessor, routeElementChanges.deletes)
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
    tasks: Seq[ChangeTask]
  ): ChangeSetContext = {
    tasks.foldLeft(initialContext) { case (context, task) =>
      processRouteChanges(context, task)
    }
  }

  private def processRouteChanges(
    initialContext: ChangeSetContext,
    task: ChangeTask
  ): ChangeSetContext = {
    task.routeIds.foldLeft(initialContext) { (context, routeId) =>
      task.processor.process(context, routeId)
    }
  }
}
