package kpn.server.analyzer.engine.changes.network

import kpn.api.common.changes.ChangeAction.ChangeAction
import kpn.api.common.changes.ChangeAction.Create
import kpn.api.common.changes.ChangeAction.Delete
import kpn.api.common.changes.ChangeAction.Modify
import kpn.api.common.changes.ChangeSet
import kpn.api.common.data.raw.RawRelation
import kpn.core.analysis.TagInterpreter
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.BlacklistRepository
import org.springframework.stereotype.Component

@Component
class NetworkChangeAnalyzerImpl(
  analysisContext: AnalysisContext,
  blacklistRepository: BlacklistRepository
) extends NetworkChangeAnalyzer {

  private val log = Log(classOf[NetworkChangeAnalyzerImpl])

  def analyze(context: ChangeSetContext): ElementChanges = {

    log.debugElapsed {

      val networkCreateIds1 = findNetworkRelationChanges(context.changeSet, Create)
      val networkCreateIds2 = findUpdatesToUnknownNetworks(context.changeSet)

      val networkUpdateIds1 = context.elementIds.relationIds.filter(analysisContext.watched.networks.contains)
      val networkUpdateIds2 = findNetworkRelationChanges(context.changeSet, Modify)

      val deletes = {
        val networkRelationDeletes = findNetworkRelationChanges(context.changeSet, Delete).filter(analysisContext.watched.networks.contains)
        val knownNetworkDeletes = findKnownNetworkDeletes(context.changeSet)
        networkRelationDeletes ++ knownNetworkDeletes
      }

      val updates = (networkUpdateIds1 ++ networkUpdateIds2) -- networkCreateIds2 -- deletes
      val creates = networkCreateIds1 ++ networkCreateIds2 -- updates -- deletes

      val sortedCreates = creates.toList.sorted
      val sortedUpdates = updates.toList.sorted
      val sortedDeletes = deletes.toList.sorted

      val message = s"creates=${creates.size}, updates=${updates.size}, deletes=${deletes.size}"
      (message, ElementChanges(sortedCreates, sortedUpdates, sortedDeletes))
    }
  }

  private def findUpdatesToUnknownNetworks(changeSet: ChangeSet): Set[Long] = {
    val networkIds = findNetworkRelationChanges(changeSet, Modify)
    networkIds.filterNot(analysisContext.watched.networks.contains)
  }

  private def findNetworkRelationChanges(changeSet: ChangeSet, action: ChangeAction): Set[Long] = {
    changeSet.relations(action).
      filter(r => TagInterpreter.isNetworkRelation(r.tags)).
      filterNot(isBlackListed).
      map(_.id).
      toSet
  }

  private def findKnownNetworkDeletes(changeSet: ChangeSet): Set[Long] = {
    changeSet.relations(Delete).map(_.id).filter(analysisContext.watched.networks.contains).toSet
  }

  private def isBlackListed(relation: RawRelation): Boolean = {
    blacklistRepository.get().containsNetwork(relation.id)
  }

}
