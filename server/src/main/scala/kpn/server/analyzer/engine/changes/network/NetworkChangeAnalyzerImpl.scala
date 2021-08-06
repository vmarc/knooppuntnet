package kpn.server.analyzer.engine.changes.network

import kpn.api.common.changes.ChangeAction.ChangeAction
import kpn.api.common.changes.ChangeAction.Create
import kpn.api.common.changes.ChangeAction.Delete
import kpn.api.common.changes.ChangeAction.Modify
import kpn.api.common.changes.ChangeSet
import kpn.api.common.data.raw.RawRelation
import kpn.core.analysis.TagInterpreter
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.BlackListRepository
import org.springframework.stereotype.Component

@Component
class NetworkChangeAnalyzerImpl(
  analysisContext: AnalysisContext,
  blackListRepository: BlackListRepository
) extends NetworkChangeAnalyzer {

  private val log = Log(classOf[NetworkChangeAnalyzerImpl])

  def analyze(changeSet: ChangeSet): ElementChanges = {

    log.debugElapsed {

      val networkCreateIds1 = findNetworkRelationChanges(changeSet, Create)
      val networkCreateIds2 = findUpdatesToUnknownNetworks(changeSet)

      val networkUpdateIds1 = analysisContext.data.networks.watched.referencedBy(ChangeSetBuilder.elementIdsIn(changeSet)).toSet
      val networkUpdateIds2 = findNetworkRelationChanges(changeSet, Modify)

      val deletes = {
        val networkRelationDeletes = findNetworkRelationChanges(changeSet, Delete).filter(analysisContext.data.networks.watched.contains)
        val knownNetworkDeletes = findKnownNetworkDeletes(changeSet)
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
    networkIds.filterNot(analysisContext.data.networks.watched.contains)
  }

  private def findNetworkRelationChanges(changeSet: ChangeSet, action: ChangeAction): Set[Long] = {
    changeSet.relations(action).
      filter(r => TagInterpreter.isNetworkRelation(r.tags)).
      filterNot(isBlackListed).
      map(_.id).
      toSet
  }

  private def findKnownNetworkDeletes(changeSet: ChangeSet): Set[Long] = {
    changeSet.relations(Delete).map(_.id).filter(analysisContext.data.networks.contains).toSet
  }

  private def isBlackListed(relation: RawRelation): Boolean = {
    blackListRepository.get.containsNetwork(relation.id)
  }

}
