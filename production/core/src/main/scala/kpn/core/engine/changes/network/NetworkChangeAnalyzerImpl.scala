package kpn.core.engine.changes.network

import kpn.core.changes.ChangeSetBuilder
import kpn.core.engine.analysis.Interpreter
import kpn.core.engine.changes.ElementChanges
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.repository.BlackListRepository
import kpn.core.util.Log
import kpn.shared.NetworkType
import kpn.shared.changes.ChangeAction.Create
import kpn.shared.changes.ChangeAction.Delete
import kpn.shared.changes.ChangeAction.Modify
import kpn.shared.changes.ChangeSet
import kpn.shared.data.raw.RawRelation

class NetworkChangeAnalyzerImpl(
  analysisData: AnalysisData,
  blackListRepository: BlackListRepository
) extends NetworkChangeAnalyzer {

  private val log = Log(classOf[NetworkChangeAnalyzerImpl])

  def analyze(changeSet: ChangeSet): ElementChanges = {

    log.debugElapsed {

      val networkCreateIds1 = findNetworkRelationChanges(changeSet, Create)
      val networkCreateIds2 = findUpdatesToUnknownNetworks(changeSet)

      val networkUpdateIds1 = analysisData.networks.watched.referencedBy(ChangeSetBuilder.elementIdsIn(changeSet)).toSet
      val networkUpdateIds2 = findNetworkRelationChanges(changeSet, Modify)

      val deletes = {
        val networkRelationDeletes = findNetworkRelationChanges(changeSet, Delete).filter(analysisData.networks.watched.contains)
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
    networkIds.filterNot(analysisData.networks.watched.contains)
  }

  private def findNetworkRelationChanges(changeSet: ChangeSet, action: Int): Set[Long] = {
    changeSet.relations(action).
      filter(isNetworkRelation).
      filterNot(isNetworkCollection).
      filterNot(isBlackListed).
      filterNot(isIgnoredNetwork).
      map(_.id).
      toSet
  }

  private def findKnownNetworkDeletes(changeSet: ChangeSet): Set[Long] = {
    changeSet.relations(Delete).map(_.id).filter(analysisData.networks.contains).toSet
  }

  private def isNetworkRelation(relation: RawRelation): Boolean = {
    new Interpreter(NetworkType.hiking).isNetworkRelation(relation) ||
      new Interpreter(NetworkType.bicycle).isNetworkRelation(relation) ||
      new Interpreter(NetworkType.horseRiding).isNetworkRelation(relation) ||
      new Interpreter(NetworkType.motorboat).isNetworkRelation(relation) ||
      new Interpreter(NetworkType.canoe).isNetworkRelation(relation) ||
      new Interpreter(NetworkType.inlineSkates).isNetworkRelation(relation)
  }

  private def isNetworkCollection(relation: RawRelation): Boolean = {
    analysisData.networkCollections.contains(relation.id)
  }

  private def isBlackListed(relation: RawRelation): Boolean = {
    blackListRepository.get.containsNetwork(relation.id)
  }

  private def isIgnoredNetwork(relation: RawRelation): Boolean = {
    analysisData.networks.ignored.contains(relation.id)
  }
}
