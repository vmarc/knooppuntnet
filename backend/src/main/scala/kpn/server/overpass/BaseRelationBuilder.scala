package kpn.server.overpass

import kpn.api.common.Relation
import kpn.api.common.data.Member
import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawMember
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.core.util.Haversine
import kpn.core.util.Log

class BaseRelationBuilder(rawData: RawData, log: Log) {

  private val nodes: Map[Long, Node] = buildNodes

  private val ways: Map[Long, Way] = buildWays

  def build(relationId: Long): Option[Relation] = {
    rawData.relationWithId(relationId) match {
      case None =>
        inconsistant(s"relation $relationId not found")
        None
      case Some(rawRelation) =>
        val baseMembers = buildMembers(rawRelation)
        Some(
          Relation(
            rawRelation.id,
            rawRelation.version,
            rawRelation.timestamp,
            rawRelation.changeSetId,
            rawRelation.tags,
            baseMembers
          )
        )
    }
  }

  private def buildMembers(rawRelation: RawRelation): Seq[Member] = {
    rawRelation.members.flatMap { rawMember =>
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
        else {
          Some(
            Member(relationId = Some(rawMember.ref), role = rawMember.role)
          )
        }
      }
      else {
        //noinspection SideEffectsInMonadicTransformation
        inconsistant(s"""unknown member type "${rawMember.memberType}" in relation ${rawRelation.id}""")
        None
      }
    }
  }

  private def buildNodes: Map[Long, Node] = {
    rawData.nodes.map { raw =>
      raw.id -> Node(
        raw.id,
        raw.latitude,
        raw.longitude,
        raw.version,
        raw.timestamp,
        raw.changeSetId,
        raw.tags
      )
    }.toMap
  }

  private def buildWays: Map[Long, Way] = {
    rawData.ways.map { raw =>
      val wayNodes = buildWayNodes(raw)
      val length = Haversine.meters(wayNodes)
      val way: Way = Way(
        raw.id,
        raw.version,
        raw.timestamp,
        raw.changeSetId,
        raw.tags,
        wayNodes,
        length
      )
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

  private def buildNodeMember(parentId: Long, rawMember: RawMember): Option[Member] = {
    nodes.get(rawMember.ref) match {
      case Some(node) => Some(Member(node = Some(node), role = rawMember.role))
      case None =>
        inconsistant(s"node ${rawMember.ref} (referenced from relation $parentId) not found in data")
        None
    }
  }

  private def buildWayMember(parentId: Long, rawMember: RawMember): Option[Member] = {
    ways.get(rawMember.ref) match {
      case Some(way) => Some(Member(way = Some(way), role = rawMember.role))
      case None =>
        inconsistant(s"way ${rawMember.ref} (referenced from relation $parentId) not found in data")
        None
    }
  }

  private def inconsistant(message: String): Unit = {
    log.warn(s"data inconsistancy: $message")
  }
}
