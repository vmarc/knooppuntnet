package kpn.server.analyzer.engine.changes.changes

import kpn.api.common.Relation
import kpn.api.common.RouteType
import kpn.api.common.data.Node
import kpn.api.common.data.Tagable
import kpn.api.common.data.Way
import kpn.api.custom.ScopedRouteType
import kpn.api.custom.Timestamp
import kpn.core.analysis.TagInterpreter
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.analyzer.engine.context.RouteElementIds

object RelationAnalyzerHelper {

  def toElementIds(relation: Relation): ElementIds = {
    val nodeIds = referencedNodes(relation).map(_.id)
    val wayIds = referencedWays(relation).map(_.id)
    val relationIds = referencedRelations(relation).map(_.id)
    val relationIds2 = relation.members.flatMap(_.relationId)

    ElementIds(
      nodeIds,
      wayIds,
      relationIds ++ relationIds2
    )
  }

  def toRouteElementIds(relation: Relation): RouteElementIds = {
    val routeElementIds = new RouteElementIds()
    referencedNodes(relation).map(_.id).foreach(routeElementIds.nodeIds.add)
    referencedWays(relation).map(_.id).foreach(routeElementIds.wayIds.add)
    referencedRelations(relation).map(_.id).foreach(routeElementIds.relationIds.add)
    relation.members.flatMap(_.relationId).foreach(routeElementIds.relationIds.add)
    routeElementIds
  }

  def referencedNodes(relation: Relation): Set[Node] = {
    findReferencedNodes(relation, Set(relation.id))
  }

  def findReferencedNodes(relation: Relation, visitedRelationIds: Set[Long]): Set[Node] = {
    relation.members.flatMap {
      case m if m.isNode =>
        Set(m.node.get)
      case m if m.isWay =>
        m.wayNodes
      case m if m.isRelation =>
        val referencedRelation = m.relation.get
        if (visitedRelationIds.contains(referencedRelation.id)) {
          Set()
        }
        else {
          findReferencedNodes(referencedRelation, visitedRelationIds ++ Set(referencedRelation.id))
        }
      case _ =>
        Seq.empty
    }.toSet
  }

  def referencedWays(relation: Relation): Set[Way] = {
    findReferencedWays(relation, Set(relation.id))
  }

  private def findReferencedWays(relation: Relation, visitedRelationIds: Set[Long]): Set[Way] = {
    relation.members.toSeq.flatMap {
      case m if m.isWay =>
        m.way.toSeq
      case m if m.isRelation =>
        val referencedRelation = m.relation.get
        if (visitedRelationIds.contains(referencedRelation.id)) {
          Seq.empty
        }
        else {
          val updatedVisitedIds = visitedRelationIds + referencedRelation.id
          findReferencedWays(referencedRelation, updatedVisitedIds).toSeq
        }
      case _ =>
        Seq.empty
    }.toSet
  }

  def referencedRelations(relation: Relation): Set[Relation] = {
    findReferencedRelations(relation, Set(relation.id))
  }

  private def findReferencedRelations(relation: Relation, visitedRelationIds: Set[Long]): Set[Relation] = {
    relation.members.flatMap(_.relation).flatMap { referencedRelation =>
      if (visitedRelationIds.contains(referencedRelation.id)) {
        Set.empty
      }
      else {
        findReferencedRelations(referencedRelation, visitedRelationIds ++ Set(referencedRelation.id)) ++ Set(referencedRelation)
      }
    }.toSet
  }
}

object RelationAnalyzer {

  def toElementIds(relation: Relation): ElementIds = {
    RelationAnalyzerHelper.toElementIds(relation)
  }

  def referencedNetworkNodes(relation: Relation): Set[Node] = {
    RelationAnalyzer.scopedRouteType(relation) match {
      case Some(scopedRouteType) =>
        RelationAnalyzerHelper.referencedNodes(relation).filter(n => TagInterpreter.isReferencedNetworkNode(scopedRouteType, n))
      case None => Set()
    }
  }

  def referencedRoutes(relation: Relation): Set[Relation] = {
    RelationAnalyzer.scopedRouteType(relation) match {
      case None => Set()
      case Some(scopedRouteType) =>
        referencedRelations(relation).filter(r => TagInterpreter.isReferencedRouteRelation(scopedRouteType, r))
    }
  }

  def referencedNetworks(relation: Relation): Set[Relation] = {
    RelationAnalyzer.routeType(relation) match {
      case None => Set()
      case Some(routeType) =>
        referencedRelations(relation).filter(r => r.id != relation.id && TagInterpreter.isNetworkRelation(routeType, r))
    }
  }

  def referencedNodes(relation: Relation): Set[Node] = {
    RelationAnalyzerHelper.referencedNodes(relation)
  }

  def referencedNonConnectionNodes(relation: Relation): Set[Node] = {
    findReferencedNonConnectionNodes(relation, Set(relation.id))
  }

  private def findReferencedNonConnectionNodes(relation: Relation, visitedRelationIds: Set[Long]): Set[Node] = {
    relation.members
      .filterNot(_.role.contains("connection"))
      .flatMap {
        case m if m.isNode =>
          m.node.toSet
        case m if m.isWay =>
          m.wayNodes
        case m if m.isRelation =>
          m.relation match {
            case Some(referencedRelation) if !visitedRelationIds.contains(referencedRelation.id) =>
              val updatedVisitedIds = visitedRelationIds + referencedRelation.id
              RelationAnalyzerHelper.findReferencedNodes(referencedRelation, updatedVisitedIds)
            case _ =>
              Set.empty
          }
        case _ =>
          Set.empty
      }.toSet
  }

  def referencedWays(relation: Relation): Set[Way] = {
    RelationAnalyzerHelper.referencedWays(relation)
  }

  def referencedRelations(relation: Relation): Set[Relation] = {
    RelationAnalyzerHelper.referencedRelations(relation)
  }

  def lastUpdated(relation: Relation): Timestamp = {
    val relationUpdates = Seq(relation.timestamp)
    val nodeUpdates = referencedNetworkNodes(relation).map(_.timestamp)
    val routeUpdates = referencedRoutes(relation).map(_.timestamp)
    val networkUpdates = referencedNetworks(relation).map(_.timestamp)
    val elements: Seq[Timestamp] = relationUpdates ++ nodeUpdates ++ routeUpdates ++ networkUpdates
    elements.max
  }

  def routeType(relation: Relation): Option[RouteType] = {
    relation.tagValue("network").flatMap { tagValue =>
      ScopedRouteType.all.find(_.key == tagValue).map(_.routeType)
    }
  }

  def scopedRouteType(relation: Tagable): Option[ScopedRouteType] = {
    relation.tagValue("network").flatMap { tagValue =>
      ScopedRouteType.all.find(_.key == tagValue)
    }
  }
}
