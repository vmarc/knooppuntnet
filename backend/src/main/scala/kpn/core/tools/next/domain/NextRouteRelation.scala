package kpn.core.tools.next.domain

import kpn.api.common.Relation
import kpn.api.id.WithId
import kpn.core.doc.RouteRelation

case class NextRouteRelation(
  _id: Long,
  relation: Relation,
  structure: Option[RouteRelation]
) extends WithId
