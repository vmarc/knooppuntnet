package kpn.server.analyzer.engine.changes

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSet
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.repository.ChangeSetRepository
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

      changes.newNetworkChanges.foreach { networkChange =>
        changeSetRepository.saveNewNetworkChange(networkChange)
      }

      changes.routeChanges.foreach { routeChange =>
        changeSetRepository.saveRouteChange(routeChange)
      }

      changes.nodeChanges.foreach { nodeChange =>
        changeSetRepository.saveNodeChange(nodeChange)
      }

      val changeSetSummary = new ChangeSetSummaryBuilder().build(replicationId, changeSet, changes)
      changeSetRepository.saveChangeSetSummary(changeSetSummary)

      val locationChangeSetSummary = new LocationChangeSetSummaryBuilder().build(replicationId, changeSet, changes)
      changeSetRepository.saveLocationChangeSetSummary(locationChangeSetSummary)
    }
  }
}
