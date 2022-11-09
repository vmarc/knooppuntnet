package kpn.core.common

import kpn.api.custom.Relation

object RelationUtil {
  def relationsInRelation(parentRelation: Relation): Seq[Relation] = {
    Seq(parentRelation) ++ parentRelation.relationMembers.flatMap { relationMember =>
      relationsInRelation(relationMember.relation)
    }
  }
}
