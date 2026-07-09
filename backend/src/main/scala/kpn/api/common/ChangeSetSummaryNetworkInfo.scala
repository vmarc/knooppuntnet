package kpn.api.common

case class ChangeSetSummaryNetworkInfo(
  networkChanges: NetworkChanges,
  orphanRouteChanges: Seq[ChangeSetSubsetElementRefs],
  orphanNodeChanges: Seq[ChangeSetSubsetElementRefs]
)
