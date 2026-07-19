package kpn.server.analyzer.engine.changes.network.base

import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.ChangeSet
import kpn.api.common.data.raw.RawRelation
import kpn.core.analysis.TagInterpreter
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.BlacklistRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class BaseNetworkChangeAnalyzer(
  analysisContext: AnalysisContext,
  blacklistRepository: BlacklistRepository
) {

  private val log = Log(classOf[BaseNetworkChangeAnalyzer])

  def analyze(context: ChangeSetContext): ElementChanges = {

    log.debugElapsed {

      val networkCreateIds1 = findNetworkRelationChanges(context.changeSet, ChangeAction.Create)
      val networkCreateIds2 = findUpdatesToUnknownNetworks(context.changeSet)

      val networkUpdateIds1 = context.elementIds.relationIds
        .filter(id =>
          analysisContext.watched.networks.contains(id)
        )
      val networkUpdateIds2 = findNetworkRelationChanges(context.changeSet, ChangeAction.Modify)

      val deletes = {
        val networkRelationDeletes = findNetworkRelationChanges(context.changeSet, ChangeAction.Delete)
          .filter(id =>
            analysisContext.watched.networks.contains(id)
          )
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
    val networkIds = findNetworkRelationChanges(changeSet, ChangeAction.Modify)
    networkIds.filterNot(id => analysisContext.watched.networks.contains(id))
  }

  private def findNetworkRelationChanges(changeSet: ChangeSet, action: ChangeAction): Set[Long] = {
    changeSet.relations(action).
      filter(TagInterpreter.isNetworkRelation).
      filterNot(isBlackListed).
      map(_.id).
      toSet
  }

  private def findKnownNetworkDeletes(changeSet: ChangeSet): Set[Long] = {
    changeSet
      .relations(ChangeAction.Delete).map(_.id)
      .filter(id => analysisContext.watched.networks.contains(id))
      .toSet
  }

  private def isBlackListed(relation: RawRelation): Boolean = {
    blacklistRepository.get().containsNetwork(relation.id)
  }
}
