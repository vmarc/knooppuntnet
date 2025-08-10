package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.data.Member
import kpn.api.common.data.Node
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.data.Data
import kpn.core.doc.RouteRelation
import kpn.server.overpass.OverpassRepository

import scala.collection.mutable

class OverpassRepositoryMock extends OverpassRepository {

  private val dataMap: mutable.Map[Timestamp, Data] = mutable.Map.empty

  def setData(timestamp: Timestamp, data: Data): Unit = {
    dataMap.put(timestamp, data)
  }

  override def nodeIds(timestamp: Timestamp): Seq[Long] = {
    dataAt(timestamp).nodes.values
      .filter(isNetworkNode)
      .map(_.id)
      .toSeq
      .sorted
  }

  override def routeIds(timestamp: Timestamp, typeValue: String): Seq[Long] = {
    dataAt(timestamp).relations.values
      .filter(r => isRouteRelation(r, typeValue))
      .map(_.id)
      .toSeq
      .sorted
  }

  override def networkIds(timestamp: Timestamp): Seq[Long] = {
    dataAt(timestamp).relations.values
      .filter(isNetworkRelation)
      .map(_.id)
      .toSeq
      .sorted
  }

  override def nodes(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[RawNode] = {
    val data = dataAt(timestamp)
    nodeIds
      .flatMap(data.nodes.get)
      .map(_.toRaw)
      .sortBy(_.id)
  }

  override def relations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[RawRelation] = {
    val data = dataAt(timestamp)
    relationIds
      .flatMap(data.relations.get)
      .map(_.toRaw)
      .sortBy(_.id)
  }

  override def fullRelations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[Relation] = {
    val data = dataAt(timestamp)
    relationIds
      .flatMap(data.relations.get)
      .sortBy(_.id)
  }

  override def relationTopLevel(timestamp: Timestamp, relationId: Long): Option[Relation] = {
    dataAt(timestamp).relations.get(relationId).map { relation =>
      relation.copy(
        members = relation.members.map {
          case m if m.isRelation => Member(relationId = Some(m.memberId), role = m.role)
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
