package kpn.server.analyzer.engine.changes

import kpn.server.repository.ChangeSetRepository
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.shared.ReplicationId
import kpn.shared.changes.ChangeSet
import org.springframework.stereotype.Component

@Component
class ChangeSaverImpl(
  changeSetRepository: ChangeSetRepository
) extends ChangeSaver {

  def save(
    replicationId: ReplicationId,
    changeSet: ChangeSet,
    changes: ChangeSetChanges
  ): Unit = {

    if (changes.nonEmpty) {

      changes.networkChanges.foreach { networkChange =>
        changeSetRepository.saveNetworkChange(networkChange)
      }

      changes.routeChanges.foreach { routeChange =>
        changeSetRepository.saveRouteChange(routeChange)
      }

      changes.nodeChanges.foreach { nodeChange =>
        changeSetRepository.saveNodeChange(nodeChange)
      }

      val changeSetSummary = new ChangeSetSummaryBuilder().build(replicationId, changeSet, changes)
      changeSetRepository.saveChangeSetSummary(changeSetSummary)
    }
  }
}
