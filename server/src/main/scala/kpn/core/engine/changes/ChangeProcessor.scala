package kpn.core.engine.changes

import kpn.core.engine.analysis.ChangeSetInfoUpdater
import kpn.server.repository.ChangeSetRepository
import kpn.core.engine.changes.data.ChangeSetChangesMerger.merge
import kpn.core.engine.changes.network.NetworkChangeProcessor
import kpn.core.engine.changes.orphan.node.OrphanNodeChangeProcessor
import kpn.core.engine.changes.orphan.route.OrphanRouteChangeProcessor

/*
  Process all changes in the change set.
 */
class ChangeProcessor(
  changeSetRepository: ChangeSetRepository,
  networkChangeProcessor: NetworkChangeProcessor,
  orphanRouteChangeProcessor: OrphanRouteChangeProcessor,
  orphanNodeChangeProcessor: OrphanNodeChangeProcessor,
  changeSetInfoUpdater: ChangeSetInfoUpdater,
  changeSaver: ChangeSaver
) {

  def process(context: ChangeSetContext): Unit = {

    val networkChangeSetChanges = networkChangeProcessor.process(context)
    val orphanRouteChangeSetChanges = orphanRouteChangeProcessor.process(context)
    val orphanNodeChangeSetChanges = orphanNodeChangeProcessor.process(context)

    val changeSetChanges = merge(
      networkChangeSetChanges,
      orphanRouteChangeSetChanges,
      orphanNodeChangeSetChanges
    )

    if (changeSetChanges.nonEmpty) {
      changeSetInfoUpdater.changeSetInfo(context.changeSet.id)
      changeSaver.save(context.replicationId, context.changeSet, changeSetChanges)
    }
  }
}
