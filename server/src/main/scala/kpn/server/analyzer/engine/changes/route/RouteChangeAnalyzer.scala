package kpn.server.analyzer.engine.changes.route

import kpn.api.common.changes.ChangeAction.ChangeAction
import kpn.api.common.changes.ChangeAction.Create
import kpn.api.common.changes.ChangeAction.Delete
import kpn.api.common.changes.ChangeAction.Modify
import kpn.api.common.changes.ChangeSet
import kpn.api.common.data.raw.RawRelation
import kpn.core.analysis.TagInterpreter
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.changes.ElementIdAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.BlacklistRepository
import org.springframework.stereotype.Component

@Component
class RouteChangeAnalyzer(
  analysisContext: AnalysisContext,
  blacklistRepository: BlacklistRepository,
  elementIdAnalyzer: ElementIdAnalyzer
) {

  def analyze(context: ChangeSetContext): ElementChanges = {

    val createdRelationsById = buildRelationMap(context.changeSet, Create)
    val updatedRelationsById = buildRelationMap(context.changeSet, Modify)
    val deletedRelationsById = buildRelationMap(context.changeSet, Delete)

    val createdRouteIds = findRouteRelationIds(createdRelationsById)
    val updatedRouteIds = findRouteRelationIds(updatedRelationsById)

    val routeCreateIds1 = createdRouteIds.filterNot(isKnownRoute)
    val routeCreateIds2 = updatedRouteIds.filterNot(isKnownRoute)

    val routeUpdateIds1 = elementIdAnalyzer.referencedBy(
      analysisContext.watched.routes,
      context.elementIds
    )

    val routeUpdateIds2 = updatedRouteIds.filter(isKnownRoute)

    val deletes = deletedRelationsById.keySet.filter { routeId =>
      isKnownRoute(routeId) && !isBlackListed(routeId)
    }

    val updates = routeUpdateIds1 ++ routeUpdateIds2 -- deletes
    val creates = routeCreateIds1 ++ routeCreateIds2 -- updates -- deletes

    val sortedCreates = creates.toList.sorted
    val sortedUpdates = updates.toList.sorted
    val sortedDeletes = deletes.toList.sorted

    ElementChanges(sortedCreates, sortedUpdates, sortedDeletes)
  }

  private def buildRelationMap(changeSet: ChangeSet, action: ChangeAction): Map[Long, RawRelation] = {
    changeSet.changes
      .filter(_.action == action)
      .flatMap(_.elements)
      .collect { case e: RawRelation => e }
      .map(n => n.id -> n)
      .toMap
  }

  private def findRouteRelationIds(relationsById: Map[Long, RawRelation]): Set[Long] = {
    relationsById.values.
      filter(r => TagInterpreter.isRouteRelation(r.tags)).
      filterNot(r => isBlackListed(r.id)).
      map(_.id).
      toSet
  }

  private def isKnownRoute(routeId: Long): Boolean = {
    analysisContext.watched.routes.contains(routeId)
  }

  private def isBlackListed(routeId: Long): Boolean = {
    blacklistRepository.get().containsRoute(routeId)
  }
}
