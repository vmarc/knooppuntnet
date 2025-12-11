package kpn.server.analyzer.engine.changes

import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkInfoRepository
import org.springframework.stereotype.Component

@Component
class ChangeSaverImpl(
  changeSetRepository: ChangeSetRepository,
  networkInfoRepository: NetworkInfoRepository
) extends ChangeSaver {
  def save(context: ChangeSetContext): Unit = {
    if (context.changes.isEmpty) {
      return
    }
    saveAllChanges(context.changes)
    saveChangeSummary(context)
  }

  private def saveAllChanges(changes: ChangeSetChanges): Unit = {
    saveNetworkChanges(changes.networkChanges)
    saveBaseRouteChanges(changes.baseRouteChanges)
    saveRouteChanges(changes.routeChanges)
    saveNodeChanges(changes.nodeChanges)
  }

  private def saveNetworkChanges(changes: Seq[NetworkChange]): Unit = {
    changes.foreach(changeSetRepository.saveNetworkChange)
  }

  private def saveBaseRouteChanges(changes: Seq[BaseRouteChange]): Unit = {
    changes.foreach(changeSetRepository.saveBaseRouteChange)
  }

  private def saveRouteChanges(changes: Seq[RouteChange]): Unit = {
    changes.foreach(changeSetRepository.saveRouteChange)
  }

  private def saveNodeChanges(changes: Seq[NodeChange]): Unit = {
    changes.foreach(changeSetRepository.saveNodeChange)
  }

  private def saveChangeSummary(context: ChangeSetContext): Unit = {
    val changeSetSummary = new ChangeSetSummaryBuilder().build(context)
    changeSetRepository.saveChangeSetSummary(changeSetSummary)
  }
}
