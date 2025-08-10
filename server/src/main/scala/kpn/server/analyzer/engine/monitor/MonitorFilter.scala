package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.Member
import kpn.api.custom.Relation

object MonitorFilter {

  private val ignoredRoles = Seq("place_of_worship", "guest_house", "outer", "inner")

  def filterWayMembers(wayMembers: Seq[Member]): Seq[Member] = {
    filterWayBuildings(wayMembers.filter(filterIgnoredRoles))
  }

  def filterRelationMembers(relationMembers: Seq[Member]): Seq[Member] = {
    filterRelationBuildings(relationMembers.filter(filterIgnoredRoles))
  }

  def relationsInRelation(parentRelation: Relation): Seq[Relation] = {
    Seq(parentRelation) ++ filterRelationMembers(parentRelation.relationMembers).flatMap { relationMember =>
      relationMember.relation.toSeq.flatMap(relationsInRelation)
    }
  }

  private def filterIgnoredRoles(member: Member): Boolean = {
    member.role match {
      case Some(role) => !ignoredRoles.contains(role)
      case None => true
    }
  }

  private def filterWayBuildings(wayMembers: Seq[Member]): Seq[Member] = {
    wayMembers.filterNot(_.hasTag("building"))
  }

  private def filterRelationBuildings(relationMembers: Seq[Member]): Seq[Member] = {
    relationMembers.filterNot(_.hasTag("building"))
  }
}
