package kpn.api.common

case class ChangeSetSummaryNetworkInfo(
  networkChanges: NetworkChanges,
  routeChanges: Seq[ChangeSetSubsetElementRefs], // orphan route changes
  nodeChanges: Seq[ChangeSetSubsetElementRefs], // orphan node changes
)
