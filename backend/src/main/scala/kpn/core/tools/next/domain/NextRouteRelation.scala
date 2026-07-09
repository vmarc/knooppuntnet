package kpn.core.tools.next.domain

import kpn.api.common.Relation
import kpn.core.doc.RouteRelation
import kpn.core.doc.WithId

case class NextRouteRelation(
  _id: Long,
  relation: Relation,
  structure: Option[RouteRelation]
) extends WithId
