package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.data.Node
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.data.Data
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

  override def routeIds(timestamp: Timestamp): Seq[Long] = {
    if (timestamp == timestampBeforeValue) {
      routeRelationIdsIn(beforeData)
    }
    else if (timestamp == timestampAfterValue) {
      routeRelationIdsIn(afterData)
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
      nodeIds.flatMap(beforeData.nodes.get).map(_.raw).sortBy(_.id)
    }
    else if (timestamp == timestampAfterValue) {
      nodeIds.flatMap(afterData.nodes.get).map(_.raw).sortBy(_.id)
    }
    else {
      throw new IllegalArgumentException(s"unknown timestamp: ${timestamp.yyyymmddhhmmss}")
    }
  }

  override def relations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[RawRelation] = {
    if (timestamp == timestampBeforeValue) {
      relationIds.flatMap(beforeData.relations.get).map(_.raw).sortBy(_.id)
    }
    else if (timestamp == timestampAfterValue) {
      relationIds.flatMap(afterData.relations.get).map(_.raw).sortBy(_.id)
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


  private def nodeIdsIn(data: Data): Seq[Long] = {
    data.nodes.values.filter(isNetworkNode).map(_.id).toSeq.sorted
  }

  private def isNetworkNode(node: Node): Boolean = {
    // matches the conditions in QueryNodeIds()
    node.tags.has("network:type", "node_network")
  }

  private def routeRelationIdsIn(data: Data): Seq[Long] = {
    data.relations.values.filter(isRouteRelation).map(_.id).toSeq.sorted
  }

  private def isRouteRelation(relation: Relation): Boolean = {
    // matches the conditions in QueryRouteIds()
    relation.tags.has("network:type", "node_network") &&
      relation.tags.has("type", "route") &&
      relation.tags.has("network")
  }

  private def networkRelationIdsIn(data: Data): Seq[Long] = {
    data.relations.values.filter(isNetworkRelation).map(_.id).toSeq.sorted
  }

  private def isNetworkRelation(relation: Relation): Boolean = {
    // matches the conditions in QueryNetworkIds()
    relation.tags.has("network:type", "node_network") &&
      relation.tags.has("type", "network") &&
      relation.tags.has("network")
  }
}
