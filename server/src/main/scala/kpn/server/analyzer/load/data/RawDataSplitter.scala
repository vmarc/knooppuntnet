package kpn.server.analyzer.load.data

import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.core.util.Log

/*
  Extracts a new Data object from a given Data, containing the relation
  with given id, and its entire hierachy of child elements.
*/
object RawDataSplitter {

  private val log = Log(classOf[RawDataSplitter])

  def extractRelation(data: RawData, id: Long): RawData = {
    new RawDataSplitter(log).extractRelation(data, id)
  }
}

class RawDataSplitter(log: Log = RawDataSplitter.log) {

  def extractRelation(data: RawData, id: Long): RawData = {
    extractRelation(data, Set(id), id)
  }

  private def extractRelation(data: RawData, alreadyExtractedRelationIds: Set[Long], id: Long): RawData = {
    data.relationWithId(id) match {
      case None => RawData(data.timestamp)
      case Some(relation) =>
        val nodesInRelation = collectNodes(data, relation)
        val ways = collectWays(data, relation)
        val relations = collectRelations(data, relation)
        val nodesInWays = collectNodesInWays(data, ways)
        val referencedRelationDatas = relations.flatMap { relation =>
          if (!alreadyExtractedRelationIds.contains(relation.id)) {
            Some(extractRelation(data, alreadyExtractedRelationIds + relation.id, relation.id))
          }
          else {
            // avoids choking on circular references
            None
          }
        }
        val nodes = (nodesInRelation.toSet ++ nodesInWays.toSet).toSeq
        val datas = referencedRelationDatas :+ RawData(None, nodes, ways, relations :+ relation)
        RawData.merge(datas: _*)
    }
  }

  private def collectNodes(data: RawData, relation: RawRelation): Seq[RawNode] = {
    val nodeOptions = relation.nodeMembers.map(_.ref).map(id => id -> data.nodeWithId(id))
    val missingNodeIds = nodeOptions.filter(_._2.isEmpty).map(_._1)
    if (missingNodeIds.nonEmpty) {
      log.error(s"Inconsistant data: missing nodes in relation ${relation.id}: " + missingNodeIds.mkString(", "))
    }
    nodeOptions.flatMap(_._2)
  }

  private def collectWays(data: RawData, relation: RawRelation): Seq[RawWay] = {
    val wayOptions = relation.wayMembers.map(_.ref).map(id => id -> data.wayWithId(id))
    val missingWayIds = wayOptions.filter(_._2.isEmpty).map(_._1)
    if (missingWayIds.nonEmpty) {
      log.error(s"Inconsistant data: missing ways in relation ${relation.id}: " + missingWayIds.mkString(", "))
    }
    wayOptions.flatMap(_._2)
  }

  private def collectRelations(data: RawData, relation: RawRelation): Seq[RawRelation] = {
    val relationOptions = relation.relationMembers.filterNot(_.ref == relation.id).map(_.ref).map(id => id -> data.relationWithId(id))
    val missingRelationIds = relationOptions.filter(_._2.isEmpty).map(_._1)
    if (missingRelationIds.nonEmpty) {
      log.error(s"Inconsistant data: missing referred relation in relation ${relation.id}: " + missingRelationIds.mkString(", "))
    }
    relationOptions.flatMap(_._2)
  }

  private def collectNodesInWays(data: RawData, ways: Seq[RawWay]): Seq[RawNode] = {
    val nodeOptions = ways.flatMap(way => way.nodeIds.map(nodeId => nodeId -> data.nodeWithId(nodeId)))
    val missingNodeIds = nodeOptions.filter(_._2.isEmpty).map(_._1)
    if (missingNodeIds.nonEmpty) {
      log.error(s"Inconsistant data: missing nodes in ways: " + missingNodeIds.mkString(", "))
    }
    nodeOptions.flatMap(_._2)
  }
}
