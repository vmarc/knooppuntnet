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
import kpn.server.overpass.OverpassRepository

class OverpassRepositoryMock(beforeData: Data, afterData: Data) extends OverpassRepository {

  val timestampBeforeValue: Timestamp = Timestamp(2015, 8, 11, 0, 0, 1)
  val timestampAfterValue: Timestamp = Timestamp(2015, 8, 11, 0, 0, 4)

  override def nodeIds(timestamp: Timestamp): Seq[Long] = {
    if (timestamp == timestampBeforeValue) {
      nodeIdsIn(beforeData)
    }
    else if (timestamp == timestampAfterValue) {
      nodeIdsIn(afterData)
    }
    else {
      throw new IllegalArgumentException(s"unknown timestamp: ${timestamp.yyyymmddhhmmss}")
    }
  }

  override def oldRouteIds(timestamp: Timestamp): Seq[Long] = {
    if (timestamp == timestampBeforeValue) {
      oldRouteRelationIdsIn(beforeData)
    }
    else if (timestamp == timestampAfterValue) {
      oldRouteRelationIdsIn(afterData)
    }
    else {
      throw new IllegalArgumentException(s"unknown timestamp: ${timestamp.yyyymmddhhmmss}")
    }
  }

  override def routeIds(timestamp: Timestamp, typeValue: String): Seq[Long] = {
    if (timestamp == timestampBeforeValue) {
      routeRelationIdsIn(beforeData, typeValue)
    }
    else if (timestamp == timestampAfterValue) {
      routeRelationIdsIn(afterData, typeValue)
    }
    else {
      throw new IllegalArgumentException(s"unknown timestamp: ${timestamp.yyyymmddhhmmss}")
    }
  }

  override def networkIds(timestamp: Timestamp): Seq[Long] = {
    if (timestamp == timestampBeforeValue) {
      networkRelationIdsIn(beforeData)
    }
    else if (timestamp == timestampAfterValue) {
      networkRelationIdsIn(afterData)
    }
    else {
      throw new IllegalArgumentException(s"unknown timestamp: ${timestamp.yyyymmddhhmmss}")
    }
  }

  override def nodes(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[RawNode] = {
    if (timestamp == timestampBeforeValue) {
      nodeIds.flatMap(beforeData.nodes.get).map(_.toRaw).sortBy(_.id)
    }
    else if (timestamp == timestampAfterValue) {
      nodeIds.flatMap(afterData.nodes.get).map(_.toRaw).sortBy(_.id)
    }
    else {
      throw new IllegalArgumentException(s"unknown timestamp: ${timestamp.yyyymmddhhmmss}")
    }
  }

  override def relations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[RawRelation] = {
    if (timestamp == timestampBeforeValue) {
      relationIds.flatMap(beforeData.relations.get).map(_.toRaw).sortBy(_.id)
    }
    else if (timestamp == timestampAfterValue) {
      relationIds.flatMap(afterData.relations.get).map(_.toRaw).sortBy(_.id)
    }
    else {
      throw new IllegalArgumentException(s"unknown timestamp: ${timestamp.yyyymmddhhmmss}")
    }
  }

  override def fullRelations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[Relation] = {
    if (timestamp == timestampBeforeValue) {
      relationIds.flatMap(beforeData.relations.get).sortBy(_.id)
    }
    else if (timestamp == timestampAfterValue) {
      relationIds.flatMap(afterData.relations.get).sortBy(_.id)
    }
    else {
      throw new IllegalArgumentException(s"unknown timestamp: ${timestamp.yyyymmddhhmmss}")
    }
  }

  override def relationTopLevel(timestamp: Timestamp, relationId: Long): Option[Relation] = {
    if (timestamp == timestampBeforeValue) {
      beforeData.relations.get(relationId).map { relation =>
        relation.copy(
          members = relation.members.toSeq.map {
            case m: RelationMember => RelationIdMember(m.relation.id, m.role)
            case member => member
          }
        )
      }
    }
    else if (timestamp == timestampAfterValue) {
      afterData.relations.get(relationId).map { relation =>
        relation.copy(
          members = relation.members.toSeq.map {
            case m: RelationMember => RelationIdMember(m.relation.id, m.role)
            case member => member
          }
        )
      }
    }
    else {
      throw new IllegalArgumentException(s"unknown timestamp: ${timestamp.yyyymmddhhmmss}")
    }
  }

  override def relationHierarchy(timestamp: Timestamp, relationId: Long): Option[RouteRelation] = ???

  private def nodeIdsIn(data: Data): Seq[Long] = {
    data.nodes.values.filter(isNetworkNode).map(_.id).toSeq.sorted
  }

  private def isNetworkNode(node: Node): Boolean = {
    // matches the conditions in QueryNodeIds()
    node.hasTag("network:type", "node_network")
  }

  private def oldRouteRelationIdsIn(data: Data): Seq[Long] = {
    data.relations.values.filter(oldIsRouteRelation).map(_.id).toSeq.sorted
  }

  private def routeRelationIdsIn(data: Data, typeValue: String): Seq[Long] = {
    data.relations.values.filter(r => isRouteRelation(r, typeValue)).map(_.id).toSeq.sorted
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

  private def networkRelationIdsIn(data: Data): Seq[Long] = {
    data.relations.values.filter(isNetworkRelation).map(_.id).toSeq.sorted
  }

  private def isNetworkRelation(relation: Relation): Boolean = {
    // matches the conditions in QueryNetworkIds()
    relation.hasTag("network:type", "node_network") &&
      relation.hasTag("type", "network") &&
      relation.hasTag("network")
  }
}
