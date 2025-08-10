package kpn.core.common

import kpn.api.custom.Relation

object RelationUtil {
  def relationsInRelation(parentRelation: Relation): Seq[Relation] = {
    Seq(parentRelation) ++ parentRelation.relationMembers.flatMap { relationMember =>
      relationMember.relation.toSeq.flatMap(relation => relationsInRelation(relation))
    }
  }
}
