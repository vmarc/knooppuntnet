package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.Member
import kpn.api.common.data.RelationMember
import kpn.api.common.data.WayMember
import kpn.api.custom.Relation

object MonitorRouteFilter {

  private val ignoredRoles = Seq("place_of_worship", "guest_house", "outer", "inner")

  def filterWayMembers(wayMembers: Seq[WayMember]): Seq[WayMember] = {
    filterWayBuildings(wayMembers.filter(filterIgnoredRoles))
  }

  def filterRelationMembers(relationMembers: Seq[RelationMember]): Seq[RelationMember] = {
    filterRelationBuildings(relationMembers.filter(filterIgnoredRoles))
  }

  def relationsInRelation(parentRelation: Relation): Seq[Relation] = {
    Seq(parentRelation) ++ filterRelationMembers(parentRelation.relationMembers).flatMap { relationMember =>
      relationsInRelation(relationMember.relation)
    }
  }

  private def filterIgnoredRoles(member: Member): Boolean = {
    member.role match {
      case Some(role) => !ignoredRoles.contains(role)
      case None => true
    }
  }

  private def filterWayBuildings(wayMembers: Seq[WayMember]): Seq[WayMember] = {
    wayMembers.filterNot(_.way.tags.has("building"))
  }

  private def filterRelationBuildings(relationMembers: Seq[RelationMember]): Seq[RelationMember] = {
    relationMembers.filterNot(_.relation.tags.has("building"))
  }
}
