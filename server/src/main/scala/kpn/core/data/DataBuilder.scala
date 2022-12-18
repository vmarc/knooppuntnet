package kpn.core.data

import kpn.api.common.data.Member
import kpn.api.common.data.Node
import kpn.api.common.data.NodeMember
import kpn.api.common.data.RelationMember
import kpn.api.common.data.Way
import kpn.api.common.data.WayMember
import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawMember
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.api.custom.Relation
import kpn.core.util.Haversine
import kpn.core.util.Log

object DataBuilder {
  private val log = Log(classOf[DataBuilder])
}

class DataBuilder(rawData: RawData, log: Log = DataBuilder.log) {

  private val relationsMap: scala.collection.mutable.Map[Long, Relation] = scala.collection.mutable.Map.empty

  private val nodes: Map[Long, Node] = buildNodes

  private val ways: Map[Long, Way] = buildWays

  private val relations: Map[Long, Relation] = buildRelations

  val data: Data = Data(
    rawData,
    nodes,
    ways,
    relations
  )

  private def buildRelations: Map[Long, Relation] = {
    rawData.relations.foreach { rawRelation =>
      if (!relationsMap.contains(rawRelation.id)) {
        buildRelation(Set(), rawRelation.id)
      }
    }
    relationsMap.toMap
  }

  private def buildRelation(parentRelations: Set[Long], id: Long): Option[Relation] = {
    rawData.relationWithId(id) match {
      case None =>
        inconsistant(s"relation $id not found")
        None
      case Some(rawRelation) =>
        val memberRelation = Relation(rawRelation, buildMembers(parentRelations ++ Set(id), rawRelation))
        relationsMap.put(memberRelation.id, memberRelation)
        Some(memberRelation)
    }
  }

  private def buildMembers(parentRelations: Set[Long], rawRelation: RawRelation): Seq[Member] = {
    val members: Seq[Member] = rawRelation.members.flatMap { rawMember =>
      if (rawMember.isNode) {
        buildNodeMember(rawRelation.id, rawMember)
      }
      else if (rawMember.isWay) {
        buildWayMember(rawRelation.id, rawMember)
      }
      else if (rawMember.isRelation) {
        if (rawMember.ref == rawRelation.id) {
          //noinspection SideEffectsInMonadicTransformation
          inconsistant(s"relation ${rawRelation.id} contains self reference, continue processing without this reference")
          None
        }
        else if (parentRelations.contains(rawMember.ref)) {
          //noinspection SideEffectsInMonadicTransformation
          inconsistant(s"relation ${rawRelation.id} references parent relation ${rawMember.ref} as member, continue processing without this reference")
          None
        }
        else {
          buildRelationMember(parentRelations, rawMember)
        }
      }
      else {
        //noinspection SideEffectsInMonadicTransformation
        inconsistant(s"""unknown member type "${rawMember.memberType}" in relation ${rawRelation.id}""")
        None
      }
    }
    members
  }

  private def buildRelationMember(parentRelationIds: Set[Long], rawMember: RawMember): Option[RelationMember] = {
    buildRelation(parentRelationIds, rawMember.ref).map(r => RelationMember(r, rawMember.role))
  }

  private def buildNodes: Map[Long, Node] = {
    rawData.nodes.map { raw =>
      raw.id -> Node(raw)
    }.toMap
  }

  private def buildWays: Map[Long, Way] = {
    rawData.ways.map { raw =>
      val wayNodes = buildWayNodes(raw)
      val length = Haversine.meters(wayNodes.map(_.raw))
      val way: Way = Way(raw, wayNodes, length)
      raw.id -> way
    }.toMap
  }

  private def buildWayNodes(rawWay: RawWay): Vector[Node] = {
    rawWay.nodeIds.flatMap { nodeId =>
      nodes.get(nodeId) match {
        case Some(node) => Some(node)
        case None =>
          //noinspection SideEffectsInMonadicTransformation
          inconsistant(s"node $nodeId (referenced from way ${rawWay.id}) not found in data")
          None
      }
    }
  }

  private def buildNodeMember(parentId: Long, rawMember: RawMember): Option[NodeMember] = {
    nodes.get(rawMember.ref) match {
      case Some(node) => Some(NodeMember(node, rawMember.role))
      case None =>
        inconsistant(s"node ${rawMember.ref} (referenced from relation $parentId) not found in data")
        None
    }
  }

  private def buildWayMember(parentId: Long, rawMember: RawMember): Option[WayMember] = {
    ways.get(rawMember.ref) match {
      case Some(way) => Some(WayMember(way, rawMember.role))
      case None =>
        inconsistant(s"way ${rawMember.ref} (referenced from relation $parentId) not found in data")
        None
    }
  }

  private def inconsistant(message: String): Unit = {
    log.warn("data inconsistancy: " + message)
  }
}
