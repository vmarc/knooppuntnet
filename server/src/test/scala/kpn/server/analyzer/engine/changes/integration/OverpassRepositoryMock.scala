package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.data.Node
import kpn.api.common.data.RelationIdMember
import kpn.api.common.data.RelationMember
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.data.Data
import kpn.core.doc.RouteRelation
import kpn.core.test.Timestamps
import kpn.server.overpass.OverpassRepository

class OverpassRepositoryMock(beforeData: Data, afterData: Data) extends OverpassRepository {

  private val dataMap: Map[Timestamp, Data] = Map(
    Timestamps.before -> beforeData,
    Timestamps.after -> afterData
  )

  override def nodeIds(timestamp: Timestamp): Seq[Long] = {
    dataAt(timestamp).nodes.values.filter(isNetworkNode).map(_.id).toSeq.sorted
  }

  override def routeIds(timestamp: Timestamp, typeValue: String): Seq[Long] = {
    dataAt(timestamp).relations.values.filter(r => isRouteRelation(r, typeValue)).map(_.id).toSeq.sorted
  }

  override def networkIds(timestamp: Timestamp): Seq[Long] = {
    dataAt(timestamp).relations.values.filter(isNetworkRelation).map(_.id).toSeq.sorted
  }

  override def nodes(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[RawNode] = {
    val data = dataAt(timestamp)
    nodeIds.flatMap(data.nodes.get).map(_.toRaw).sortBy(_.id)
  }

  override def relations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[RawRelation] = {
    val data = dataAt(timestamp)
    relationIds.flatMap(data.relations.get).map(_.toRaw).sortBy(_.id)
  }

  override def fullRelations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[Relation] = {
    val data = dataAt(timestamp)
    relationIds.flatMap(data.relations.get).sortBy(_.id)
  }

  override def relationTopLevel(timestamp: Timestamp, relationId: Long): Option[Relation] = {
    dataAt(timestamp).relations.get(relationId).map { relation =>
      relation.copy(
        members = relation.members.toSeq.map {
          case m: RelationMember => RelationIdMember(m.relation.id, m.role)
          case member => member
        }
      )
    }
  }

  override def subRelationTree(timestamp: Timestamp, relationId: Long): Option[RouteRelation] = {
    dataAt(timestamp).relations.get(relationId).map { relation =>
      RouteRelation.from(relation, None)
    }
  }

  private def dataAt(timestamp: Timestamp): Data = {
    dataMap.getOrElse(
      timestamp,
      throw new IllegalArgumentException(s"unknown timestamp: ${timestamp.yyyymmddhhmmss}")
    )
  }

  private def isNetworkNode(node: Node): Boolean = {
    // matches the conditions in QueryNodeIds()
    node.hasTag("network:type", "node_network")
  }

  private def oldRouteRelationIdsIn(data: Data): Seq[Long] = {
    data.relations.values.filter(oldIsRouteRelation).map(_.id).toSeq.sorted
  }

  private def oldIsRouteRelation(relation: Relation): Boolean = {
    // matches the conditions in QueryRouteIds()
    relation.hasTag("network:type", "node_network") &&
      relation.hasTag("type", "route") &&
      relation.hasTag("network")
  }

  private def isRouteRelation(relation: Relation, typeValue: String): Boolean = {
    // matches the conditions in QueryRouteIds()
    relation.hasTag("network:type", "node_network") &&
      relation.hasTag("type", typeValue) &&
      relation.hasTag("network")
  }

  private def isNetworkRelation(relation: Relation): Boolean = {
    // matches the conditions in QueryNetworkIds()
    relation.hasTag("network:type", "node_network") &&
      relation.hasTag("type", "network") &&
      relation.hasTag("network")
  }
}
