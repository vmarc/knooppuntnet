package kpn.api.common

import kpn.api.common.changes.details.ChangeKey
import kpn.api.custom.Subset

case class ChangeSetSummaryInfo(
  rowIndex: Long,
  key: ChangeKey,
  comment: Option[String],
  subsets: Seq[Subset],
  network: Option[ChangeSetSummaryNetworkInfo],
  location: Option[ChangeSetSummaryLocationInfo],
  happy: Boolean,
  investigate: Boolean,
  impact: Boolean,
)
