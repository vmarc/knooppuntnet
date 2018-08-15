package kpn.core.changes

import kpn.core.engine.analysis.Interpreter
import kpn.shared.NetworkType
import kpn.shared.Timestamp
import kpn.shared.data.Way
import kpn.shared.data._

object RelationAnalyzer {

  def routeName(relation: Relation): String = relation.tags("note").getOrElse("no-name")

  def networkType(relation: Relation): Option[NetworkType] = {
    relation.tags("network").flatMap(NetworkType.withName)
  }

  def toElementIds(relation: Relation): ElementIds = {
    ElementIds(
      referencedNodes(relation).map(_.id),
      referencedWays(relation).map(_.id),
      referencedRelations(relation).map(_.id)
    )
  }

  def referencedNetworkNodes(relation: Relation): Set[Node] = {
    networkType(relation) match {
      case None => Set()
      case Some(networkType) =>
        val interpreter = new Interpreter(networkType)
        referencedNodes(relation).filter(n => interpreter.isNetworkNode(n.raw))
    }
  }

  def referencedRoutes(relation: Relation): Set[Relation] = {
    networkType(relation) match {
      case None => Set()
      case Some(networkType) =>
        val interpreter = new Interpreter(networkType)
        referencedRelations(relation).filter(r => interpreter.isRouteRelation(r.raw))
    }
  }

  def referencedNetworks(relation: Relation): Set[Relation] = {
    networkType(relation) match {
      case None => Set()
      case Some(networkType) =>
        val interpreter = new Interpreter(networkType)
        referencedRelations(relation).filter(r => r.id != relation.id && interpreter.isNetworkRelation(r.raw))
    }
  }

  def referencedNodes(relation: Relation): Set[Node] = {
    findReferencedNodes(relation, Set(relation.id))
  }

  private def findReferencedNodes(relation: Relation, visitedRelationIds: Set[Long]): Set[Node] = {
    relation.members.flatMap { member =>
      member match {
        case nodeMember: NodeMember => Set(nodeMember.node)
        case wayMember: WayMember => wayMember.way.nodes
        case relationMember: RelationMember =>
          val referencedRelation = relationMember.relation
          if (visitedRelationIds.contains(referencedRelation.id)) {
            Set()
          }
          else {
            findReferencedNodes(referencedRelation, visitedRelationIds ++ Set(referencedRelation.id))
          }
      }
    }.toSet
  }

  def referencedNonConnectionNodes(relation: Relation): Set[Node] = {
    findReferencedNonConnectionNodes(relation, Set(relation.id))
  }

  private def findReferencedNonConnectionNodes(relation: Relation, visitedRelationIds: Set[Long]): Set[Node] = {
    relation.members.flatMap { member =>
      member.role match {
        case Some("connection") => Set()
        case _ =>
          member match {
            case nodeMember: NodeMember =>
              member.role match {
                case Some("connection") => Set()
                case _ => Set(nodeMember.node)
              }
            case wayMember: WayMember => wayMember.way.nodes
            case relationMember: RelationMember =>
              val referencedRelation = relationMember.relation
              if (visitedRelationIds.contains(referencedRelation.id)) {
                Set()
              }
              else {
                findReferencedNodes(referencedRelation, visitedRelationIds ++ Set(referencedRelation.id))
              }
          }
      }

    }.toSet
  }

  def referencedWays(relation: Relation): Set[Way] = {
    findReferencedWays(relation, Set(relation.id))
  }

  private def findReferencedWays(relation: Relation, visitedRelationIds: Set[Long]): Set[Way] = {
    relation.members.flatMap {
      case wayMember: WayMember => Seq(wayMember.way)
      case relationMember: RelationMember =>
        val referencedRelation = relationMember.relation
        if (visitedRelationIds.contains(referencedRelation.id)) {
          Set()
        }
        else {
          findReferencedWays(referencedRelation, visitedRelationIds ++ Set(referencedRelation.id))
        }
      case _ => Set()
    }.toSet
  }

  def referencedRelations(relation: Relation): Set[Relation] = {
    findReferencedRelations(relation, Set(relation.id))
  }

  def lastUpdated(relation: Relation): Timestamp = {
    val relationUpdates = Seq(relation.timestamp)
    val nodeUpdates = referencedNetworkNodes(relation).map(_.timestamp)
    val routeUpdates = referencedRoutes(relation).map(_.timestamp)
    val networkUpdates = referencedNetworks(relation).map(_.timestamp)
    val elements: Seq[Timestamp] = relationUpdates ++ nodeUpdates ++ routeUpdates ++ networkUpdates
    elements.max
  }

  private def findReferencedRelations(relation: Relation, visitedRelationIds: Set[Long]): Set[Relation] = {
    val refs = relation.members.flatMap {
      case relationMember: RelationMember =>
        val referencedRelation = relationMember.relation
        if (visitedRelationIds.contains(referencedRelation.id)) {
          Seq()
        }
        else {
          findReferencedRelations(referencedRelation, visitedRelationIds ++ Set(referencedRelation.id)) + referencedRelation
        }
      case _ => Seq()
    }
    refs.toSet
  }

  def waysLength(relation: Relation): Int = {
    relation.wayMembers.map(_.way.length).sum
    // TODO ROUTE use the logic below for a more accurate result --> need to use the above to avoid MISMATCH between new and old
    // referencedWays(relation).map(_.length).sum
  }

}
