package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.data.Member
import kpn.api.custom.Relation
import kpn.server.analyzer.engine.analysis.route.structure.reference.JavaMember
import kpn.server.analyzer.engine.analysis.route.structure.reference.JavaNode
import kpn.server.analyzer.engine.analysis.route.structure.reference.JavaRelation
import kpn.server.analyzer.engine.analysis.route.structure.reference.JavaWay

import java.util.Collections
import scala.collection.mutable
import scala.jdk.CollectionConverters.MapHasAsJava
import scala.jdk.CollectionConverters.SeqHasAsJava

object JavaRelationConverter {

  def toJava(relation: Relation): JavaRelation = {
    new JavaRelationConverter().toJava(relation)
  }
}

class JavaRelationConverter {

  private val nodeMap = mutable.Map[Long, JavaNode]()

  def toJava(relation: Relation): JavaRelation = {
    val wayMembers = relation.members.zipWithIndex.flatMap { case (member, index) =>
      member match {
        case m if m.isWay => Some(toJavaMember(index, m))
        case _ => None
      }
    }.asJava
    new JavaRelation(Collections.unmodifiableList(wayMembers))
  }

  private def toJavaMember(memberIndex: Long, wayMember: Member): JavaMember = {
    val role = toJavaRole(wayMember)
    val nodes = toJavaNodes(wayMember)
    val tags = toJavaTags(wayMember)
    val referenceWay = new JavaWay(
      wayMember.way.get.id,
      tags,
      nodes
    )
    new JavaMember(memberIndex, role, referenceWay)
  }

  private def toJavaRole(member: Member): String = {
    member.role.getOrElse("")
  }

  private def toJavaNodes(wayMember: Member): java.util.List[JavaNode] = {
    Collections.unmodifiableList(
      wayMember.wayNodes
        .map(node => nodeMap.getOrElseUpdate(node.id, new JavaNode(node.id)))
        .asJava
    )
  }

  private def toJavaTags(wayMember: Member): java.util.Map[String, String] = {
    Collections.unmodifiableMap(
      wayMember.tags
        .map(tag => tag.key -> tag.value)
        .toMap
        .asJava
    )
  }
}
