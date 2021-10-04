package kpn.server.analyzer.engine.changes

import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkInfoRepository
import org.springframework.stereotype.Component

@Component
class ChangeSaverImpl(
  changeSetRepository: ChangeSetRepository,
  networkInfoRepository: NetworkInfoRepository
) extends ChangeSaver {

  def save(context: ChangeSetContext): Unit = {

    if (context.changes.nonEmpty) {

      context.changes.networkChanges.foreach { networkChange =>
        changeSetRepository.saveNetworkChange(networkChange)
      }

      context.changes.networkInfoChanges.foreach { networkInfoChange =>
        changeSetRepository.saveNetworkInfoChange(networkInfoChange)
        networkInfoRepository.updateNetworkChangeCount(networkInfoChange.key.elementId)
      }

      context.changes.routeChanges.foreach { routeChange =>
        changeSetRepository.saveRouteChange(routeChange)
      }

      context.changes.nodeChanges.foreach { nodeChange =>
        changeSetRepository.saveNodeChange(nodeChange)
      }

      val changeSetSummary = new ChangeSetSummaryBuilder().build(context)
      changeSetRepository.saveChangeSetSummary(changeSetSummary)
    }
  }
}
