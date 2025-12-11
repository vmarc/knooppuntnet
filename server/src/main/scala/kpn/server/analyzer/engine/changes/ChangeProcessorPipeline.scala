package kpn.server.analyzer.engine.changes

import kpn.server.analyzer.engine.analysis.ChangeSetInfoUpdater
import kpn.server.analyzer.engine.changes.network.base.BaseNetworkChangeProcessor
import kpn.server.analyzer.engine.changes.network.main.NetworkChangeProcessor
import kpn.server.analyzer.engine.changes.node.base.BaseNodeChangeProcessor
import kpn.server.analyzer.engine.changes.node.main.NodeChangeProcessor
import kpn.server.analyzer.engine.changes.route.base.BaseRouteChangeProcessor
import kpn.server.analyzer.engine.changes.route.main.RouteChangeProcessor
import org.springframework.stereotype.Component

@Component
class ChangeProcessorPipeline(
  baseNodeChangeProcessor: BaseNodeChangeProcessor,
  baseNetworkChangeProcessor: BaseNetworkChangeProcessor,
  baseRouteChangeProcessor: BaseRouteChangeProcessor,
  networkChangeProcessor: NetworkChangeProcessor,
  routeChangeProcessor: RouteChangeProcessor,
  nodeChangeProcessor: NodeChangeProcessor,
  changeSetInfoUpdater: ChangeSetInfoUpdater,
  changeSaver: ChangeSaver
) extends ChangeProcessor {

  private val pipeline: Seq[ChangeProcessor] = Seq(
    baseNodeChangeProcessor,
    baseNetworkChangeProcessor,
    baseRouteChangeProcessor,
    nodeChangeProcessor,
    routeChangeProcessor,
    networkChangeProcessor
  )

  def process(initialContext: ChangeSetContext): ChangeSetContext = {
    val context = processPipeline(initialContext)
    saveChanges(context)
    context
  }

  private def processPipeline(initialContext: ChangeSetContext): ChangeSetContext = {
    pipeline.foldLeft(initialContext) { (currentContext, processor) =>
      processor.process(currentContext)
    }
  }

  private def saveChanges(context: ChangeSetContext): Unit = {
    if (context.changes.nonEmpty) {
      changeSetInfoUpdater.changeSetInfo(context.changeSet.id)
      changeSaver.save(context)
    }
  }
}
