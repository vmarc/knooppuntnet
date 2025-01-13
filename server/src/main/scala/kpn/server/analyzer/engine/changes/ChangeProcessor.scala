package kpn.server.analyzer.engine.changes

import kpn.server.analyzer.engine.analysis.ChangeSetInfoUpdater
import kpn.server.analyzer.engine.changes.network.BaseNetworkChangeProcessor
import kpn.server.analyzer.engine.changes.network.NetworkChangeProcessor
import kpn.server.analyzer.engine.changes.network.info.NetworkInfoChangeProcessor
import kpn.server.analyzer.engine.changes.node.BaseNodeChangeProcessor
import kpn.server.analyzer.engine.changes.node.NodeChangeProcessor
import kpn.server.analyzer.engine.changes.route.RouteChangeProcessor
import org.springframework.stereotype.Component

@Component
class ChangeProcessor(
  baseNodeChangeProcessor: BaseNodeChangeProcessor,
  baseNetworkChangeProcessor: BaseNetworkChangeProcessor,
  networkChangeProcessor: NetworkChangeProcessor,
  routeChangeProcessor: RouteChangeProcessor,
  nodeChangeProcessor: NodeChangeProcessor,
  networkInfoChangeProcessor: NetworkInfoChangeProcessor,
  changeSetInfoUpdater: ChangeSetInfoUpdater,
  changeSaver: ChangeSaver
) {

  def process(context: ChangeSetContext): ChangeSetContext = {

    val context1 = baseNodeChangeProcessor.process(context)
    val context2 = baseNetworkChangeProcessor.process(context1)
    val context3 = routeChangeProcessor.process(context2)
    val context4 = nodeChangeProcessor.process(context3)
    val context5 = networkChangeProcessor.process(context4)
    val context6 = networkInfoChangeProcessor.analyze(context5)

    if (context6.changes.nonEmpty) {
      changeSetInfoUpdater.changeSetInfo(context6.changeSet.id)
      changeSaver.save(context6)
    }
    context6
  }
}
