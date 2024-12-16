package kpn.core.tools.next.domain

import kpn.api.base.WithId
import kpn.api.custom.Relation
import kpn.core.doc.RouteRelation

case class NextRouteRelation(
  _id: Long,
  relation: Relation,
  structure: Option[RouteRelation]
) extends WithId
