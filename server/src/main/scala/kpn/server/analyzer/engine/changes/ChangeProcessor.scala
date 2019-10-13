package kpn.server.analyzer.engine.changes

import kpn.server.analyzer.engine.analysis.ChangeSetInfoUpdater
import kpn.server.repository.ChangeSetRepository
import kpn.server.analyzer.engine.changes.data.ChangeSetChangesMerger.merge
import kpn.server.analyzer.engine.changes.network.NetworkChangeProcessor
import kpn.server.analyzer.engine.changes.orphan.node.OrphanNodeChangeProcessor
import kpn.server.analyzer.engine.changes.orphan.route.OrphanRouteChangeProcessor
import org.springframework.stereotype.Component

/*
  Process all changes in the change set.
 */
@Component
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
