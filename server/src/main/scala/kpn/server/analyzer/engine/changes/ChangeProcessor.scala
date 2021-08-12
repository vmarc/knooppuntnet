package kpn.server.analyzer.engine.changes

import kpn.server.analyzer.engine.analysis.ChangeSetInfoUpdater
import kpn.server.analyzer.engine.changes.data.ChangeSetChangesMerger.merge
import kpn.server.analyzer.engine.changes.network.NetworkChangeProcessor
import kpn.server.analyzer.engine.changes.node.NewNodeChangeProcessor
import kpn.server.analyzer.engine.changes.orphan.route.OrphanRouteChangeProcessor
import kpn.server.analyzer.engine.changes.route.RouteChangeProcessor
import org.springframework.stereotype.Component

/*
  Process all changes in the change set.
 */
@Component
class ChangeProcessor(
  routeChangeProcessor: RouteChangeProcessor,
  networkChangeProcessor: NetworkChangeProcessor,
  orphanRouteChangeProcessor: OrphanRouteChangeProcessor,
  newNodeChangeProcessor: NewNodeChangeProcessor,
  changeSetInfoUpdater: ChangeSetInfoUpdater,
  changeSaver: ChangeSaver
) {

  def process(context: ChangeSetContext): Unit = {

    val context1 = networkChangeProcessor.process(context)
    val routeChangeSetChanges = routeChangeProcessor.process(context1)
    // val orphanRouteChangeSetChanges = orphanRouteChangeProcessor.process(context)

    val impactedNodeIds = (
      routeChangeSetChanges.routeChanges.flatMap(_.impactedNodeIds) ++
        context1.changes.newNetworkChanges.flatMap(_.impactedNodeIds)
      ).distinct.sorted

    val nodeChangeSetChanges = newNodeChangeProcessor.process(context1, impactedNodeIds)

    val changeSetChanges = merge(
      routeChangeSetChanges,
      context1.changes,
      // orphanRouteChangeSetChanges,
      nodeChangeSetChanges
    )

    if (changeSetChanges.nonEmpty) {
      changeSetInfoUpdater.changeSetInfo(context1.changeSet.id)
      changeSaver.save(
        context1.replicationId,
        context1.changeSet,
        changeSetChanges
      )
    }
  }
}
