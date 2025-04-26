package kpn.server.analyzer.engine.changes

import kpn.server.analyzer.engine.analysis.ChangeSetInfoUpdater
import kpn.server.analyzer.engine.changes.network.BaseNetworkChangeProcessor
import kpn.server.analyzer.engine.changes.network.NetworkChangeProcessor
import kpn.server.analyzer.engine.changes.node.base.BaseNodeChangeProcessor
import kpn.server.analyzer.engine.changes.node.main.NodeChangeProcessor
import kpn.server.analyzer.engine.changes.route.BaseRouteChangeProcessor
import kpn.server.analyzer.engine.changes.route.RouteChangeProcessor
import org.springframework.stereotype.Component

@Component
class MainChangeProcessor(
  baseNodeChangeProcessor: BaseNodeChangeProcessor,
  baseNetworkChangeProcessor: BaseNetworkChangeProcessor,
  baseRouteChangeProcessor: BaseRouteChangeProcessor,
  networkChangeProcessor: NetworkChangeProcessor,
  routeChangeProcessor: RouteChangeProcessor,
  nodeChangeProcessor: NodeChangeProcessor,
  changeSetInfoUpdater: ChangeSetInfoUpdater,
  changeSaver: ChangeSaver
) extends ChangeProcessor {

  private val processors: Seq[ChangeProcessor] = Seq(
    baseNodeChangeProcessor,
    baseNetworkChangeProcessor,
    baseRouteChangeProcessor,
    nodeChangeProcessor,
    routeChangeProcessor,
    networkChangeProcessor
  )

  def process(context: ChangeSetContext): ChangeSetContext = {
    val processedContext = executeProcessingChain(context)
    saveChanges(processedContext)
    processedContext
  }

  private def executeProcessingChain(initialContext: ChangeSetContext): ChangeSetContext = {
    processors.foldLeft(initialContext) { (currentContext, processor) =>
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
