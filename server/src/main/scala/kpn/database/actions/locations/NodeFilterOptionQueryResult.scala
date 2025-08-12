package kpn.database.actions.locations

import kpn.api.common.changes.filter.ServerFilterGroup
import kpn.core.doc.Storable
import kpn.database.base.CountResult

// TODO scala3 move back into using class
case class NodeFilterOptionQueryResult(
  factsTotalNodeCount: Seq[CountResult],
  facts: Seq[ServerFilterGroup],
  proposed: Seq[ServerFilterGroup],
  survey: Seq[ServerFilterGroup],
  lastUpdated: Seq[ServerFilterGroup],
  integrityCheckCount: Seq[CountResult],
  integrityCheckTotalNodeCount: Seq[CountResult],
  integrityCheckFailedCount: Seq[CountResult],
  integrityCheckFailedTotalNodeCount: Seq[CountResult],
  referencedInRoutesCount: Seq[CountResult],
  referencedInRoutesTotalNodeCount: Seq[CountResult],
  totalNodeCount: Seq[CountResult],
) extends Storable
