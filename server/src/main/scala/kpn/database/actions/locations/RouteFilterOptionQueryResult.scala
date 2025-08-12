package kpn.database.actions.locations

import kpn.api.common.changes.filter.ServerFilterGroup
import kpn.core.doc.Storable
import kpn.database.base.CountResult

// TODO scala3 move back into using class
case class RouteFilterOptionQueryResult(
  factsTotalRouteCount: Seq[CountResult],
  facts: Seq[ServerFilterGroup],
  proposed: Seq[ServerFilterGroup],
  survey: Seq[ServerFilterGroup],
  lastUpdated: Seq[ServerFilterGroup],
) extends Storable

