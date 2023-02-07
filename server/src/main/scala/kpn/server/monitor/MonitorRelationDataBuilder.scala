package kpn.server.monitor

import kpn.api.common.data.Member
import kpn.api.common.data.RelationMember
import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawMember
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Relation
import kpn.core.data.Data
import kpn.core.util.Log

object MonitorRelationDataBuilder {
  private val log = Log(classOf[MonitorRelationDataBuilder])
}

class MonitorRelationDataBuilder(rawData: RawData, log: Log = MonitorRelationDataBuilder.log) {

  private val relationsMap: scala.collection.mutable.Map[Long, Relation] = scala.collection.mutable.Map.empty

  private val relations: Map[Long, Relation] = buildRelations

  val data: Data = Data(
    rawData,
    Map.empty,
    Map.empty,
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
    rawRelation.members.flatMap { rawMember =>
      if (rawMember.isRelation) {
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
        None
      }
    }
  }

  private def buildRelationMember(parentRelationIds: Set[Long], rawMember: RawMember): Option[RelationMember] = {
    buildRelation(parentRelationIds, rawMember.ref).map(r => RelationMember(r, rawMember.role))
  }

  private def inconsistant(message: String): Unit = {
    log.warn("data inconsistancy: " + message)
  }
}
