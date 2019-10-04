package kpn.core.engine.changes.orphan.route

import kpn.core.changes.ChangeSetBuilder
import kpn.core.engine.analysis.Interpreter
import kpn.core.engine.changes.ElementChanges
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.repository.BlackListRepository
import kpn.shared.NetworkType
import kpn.shared.changes.ChangeAction.Create
import kpn.shared.changes.ChangeAction.Delete
import kpn.shared.changes.ChangeAction.Modify
import kpn.shared.changes.ChangeSet
import kpn.shared.data.raw.RawRelation

class OrphanRouteChangeAnalyzer(
  analysisData: AnalysisData,
  blackListRepository: BlackListRepository
) {

  def analyze(changeSet: ChangeSet): ElementChanges = {

    val createdRelationsById = routeMap(changeSet, Create)
    val updatedRelationsById = routeMap(changeSet, Modify)
    val deletedRelationsById = routeMap(changeSet, Delete)

    val createdRouteIds = routeRelationIds(createdRelationsById)
    val updatedRouteIds = routeRelationIds(updatedRelationsById)

    val routeCreateIds1 = createdRouteIds.filterNot(isKnownRoute)
    val routeCreateIds2 = updatedRouteIds.filterNot(isKnownRoute)

    val routeUpdateIds1 = analysisData.orphanRoutes.watched.referencedBy(ChangeSetBuilder.elementIdsIn(changeSet)).toSet
    val routeUpdateIds2 = updatedRouteIds.filter(isKnownOrphanRoute)

    val deletes = {
      val knownOrphanRouteDeletes = deletedRelationsById.keySet.filter(isKnownOrphanRoute)
      val knownOrphanRoutesWithRequiredTagsMissing = updatedRelationsById.values.filter(isKnownOrphanRouteWithRequiredTagsMissing).map(_.id)
      knownOrphanRouteDeletes ++ knownOrphanRoutesWithRequiredTagsMissing
    }

    val updates = routeUpdateIds1 ++ routeUpdateIds2 -- deletes
    val creates = routeCreateIds1 ++ routeCreateIds2 -- updates -- deletes

    val sortedCreates = creates.toList.sorted
    val sortedUpdates = updates.toList.sorted
    val sortedDeletes = deletes.toList.sorted

    ElementChanges(sortedCreates, sortedUpdates, sortedDeletes)
  }

  private def routeMap(changeSet: ChangeSet, action: Int): Map[Long, RawRelation] = {
    changeSet.changes.filter(_.action == action).flatMap(_.elements).collect { case e: RawRelation => e }.map(n => n.id -> n).toMap
  }

  private def routeRelationIds(relationsById: Map[Long, RawRelation]): Set[Long] = {
    relationsById.values.
      filter(isRouteRelation).
      filterNot(isBlackListed).
      map(_.id).
      toSet
  }

  private def isRouteRelation(relation: RawRelation): Boolean = {
    new Interpreter(NetworkType.hiking).isRouteRelation(relation) ||
      new Interpreter(NetworkType.bicycle).isRouteRelation(relation) ||
      new Interpreter(NetworkType.horseRiding).isRouteRelation(relation) ||
      new Interpreter(NetworkType.motorboat).isRouteRelation(relation) ||
      new Interpreter(NetworkType.canoe).isRouteRelation(relation) ||
      new Interpreter(NetworkType.inlineSkates).isRouteRelation(relation)
  }

  private def isKnownOrphanRouteWithRequiredTagsMissing(relation: RawRelation): Boolean = {
    isKnownOrphanRoute(relation.id) && !isRouteRelation(relation)
  }

  private def isKnownRoute(routeId: Long): Boolean = {
    isKnownOrphanRoute(routeId) ||
      analysisData.networks.watched.isReferencingRelation(routeId)
  }

  private def isKnownOrphanRoute(routeId: Long): Boolean = {
    analysisData.orphanRoutes.watched.contains(routeId)
  }

  private def isBlackListed(relation: RawRelation): Boolean = {
    blackListRepository.get.containsRoute(relation.id)
  }
}
