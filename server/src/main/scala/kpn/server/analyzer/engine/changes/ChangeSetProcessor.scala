package kpn.server.analyzer.engine.changes

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSet
import kpn.core.util.Log
import kpn.server.analyzer.engine.ReplicationContext
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import kpn.server.analyzer.engine.context.ChangeElementIds
import org.springframework.stereotype.Component

@Component
class ChangeSetProcessor(
  changeProcessorPipeline: ChangeProcessorPipeline,
) {

  private val log = Log(classOf[ChangeSetProcessor])

  def processChangeSets(replicationId: ReplicationId, changeSets: Seq[ChangeSet]): ReplicationContext = {
    log.debug(s"${changeSets.size} changeSets")
    val initialContext = ReplicationContext(replicationId)
    val result = changeSets.foldLeft(initialContext) { (context, changeSet) =>
      log.debug(s"processing changeSet ${changeSet.id}")
      processChangeSet(context, changeSet)
    }
    log.debug(s"${changeSets.size} changeSets processed")
    result
  }

  private def processChangeSet(replicationContext: ReplicationContext, changeSet: ChangeSet): ReplicationContext = {
    Log.context(s"${changeSet.id}") {
      val elementIds = ChangeSetBuilder.elementIdsIn(changeSet)
      val initialChangeSetContext = ChangeSetContext(replicationContext.replicationId, changeSet, elementIds)
      val changeSetContext = changeProcessorPipeline.process(initialChangeSetContext)
      updateReplicationContext(replicationContext, elementIds, changeSetContext)
    }
  }

  private def updateReplicationContext(
    replicationContext: ReplicationContext,
    elementIds: ChangeElementIds,
    changeSetContext: ChangeSetContext
  ): ReplicationContext = {
    val changeSetElementCount = replicationContext.changeSetElementCount + elementIds.size
    val hasChanges = replicationContext.hasChanges || changeSetContext.changes.nonEmpty
    val tiles = (replicationContext.tiles ++ changeSetContext.impactedTileIds).distinct.sorted
    replicationContext.copy(
      changeSetElementCount = changeSetElementCount,
      hasChanges = hasChanges,
      tiles = tiles,
    )
  }
}
