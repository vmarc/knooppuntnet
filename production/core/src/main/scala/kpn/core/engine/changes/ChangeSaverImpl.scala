package kpn.core.engine.changes

import kpn.core.repository.ChangeSetRepository
import kpn.core.engine.changes.data.ChangeSetChanges
import kpn.shared.ReplicationId
import kpn.shared.changes.ChangeSet

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
