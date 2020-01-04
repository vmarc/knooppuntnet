package kpn.core.tools.country

import kpn.shared.data.raw.RawMember

class SkeletonParser {

  def parse(xml: scala.xml.Node): SkeletonData = {
    val nodes = nodesIn(xml).map(n => n.id -> n).toMap
    val ways = waysIn(xml).map(w => w.id -> w).toMap
    val relations = relationsIn(xml).map(r => r.id -> r).toMap
    SkeletonData(0L, nodes, ways, relations)
  }

  private def nodesIn(xml: scala.xml.Node): Seq[SkeletonNode] = {
    (xml \ "node").map { n =>
      val id = (n \ "@id").text.toLong
      val latitude = (n \ "@lat").text.toDouble
      val longitude = (n \ "@lon").text.toDouble
      SkeletonNode(id, latitude, longitude)
    }
  }

  private def waysIn(xml: scala.xml.Node): Seq[SkeletonWay] = {
    (xml \ "way").map { w =>
      val id = (w \ "@id").text.toLong
      val nodeIds = (w \ "nd").map(t => (t \ "@ref").text.toLong)
      SkeletonWay(id, nodeIds)
    }
  }

  private def relationsIn(xml: scala.xml.Node): Seq[SkeletonRelation] = {
    (xml \ "relation").map { r =>
      val id = (r \ "@id").text.toLong
      val members = (r \ "member").map { t =>
        val memberType = (t \ "@type").text
        val ref = (t \ "@ref").text.toLong
        val role = (t \ "@role").text
        val roleOption = if (role == "") None else Some(role)
        RawMember(memberType, ref, roleOption)
      }
      SkeletonRelation(id, members)
    }
  }
}
