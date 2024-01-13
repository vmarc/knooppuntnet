package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.WayMember
import kpn.api.custom.Relation
import reference.WayInfoCalculator

import scala.collection.mutable
import scala.jdk.CollectionConverters.CollectionHasAsScala

class ReferencePrinter {

  def print(relation: Relation): Unit = {

    val nodeMap = mutable.Map[Long, reference.Node]()

    val referenceRelationMembers = relation.members.map { member =>
      member match {
        case wayMember: WayMember =>
          val referenceRole = wayMember.role match {
            case None => ""
            case Some(role) => role
          }
          val referenceNodes = wayMember.way.nodes.map { node =>
            nodeMap.getOrElseUpdate(node.id, new reference.Node(node.id))
          }
          val referenceWay = new reference.Way(wayMember.way.id, referenceNodes.toArray)
          new reference.RelationMember(referenceRole, referenceWay)

        case _ => throw new IllegalStateException("non way member types not implemented yet")
      }
    }.toArray
    val referenceRelation = new reference.Relation(referenceRelationMembers)
    val infos = new WayInfoCalculator().analyze(referenceRelation, java.util.Arrays.asList(referenceRelationMembers: _*)).asScala
    println("-----")
    infos.zipWithIndex.foreach { case (info, index) =>
      println(s"${index + 1} ${info.toString}")
      if (!info.linkedToNextMember && index < infos.size - 1) {
        println("-")
      }
    }
    println("-----")
  }
}
