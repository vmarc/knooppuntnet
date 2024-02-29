package kpn.core.tools.next.domain

import kpn.api.base.WithId
import kpn.api.custom.Relation

case class NextRouteRelation(
  _id: Long,
  relation: Relation
) extends WithId
