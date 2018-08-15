package kpn.shared

import kpn.shared.changes.details.ChangeKey
import kpn.shared.common.ToStringBuilder

object ChangeSetSummary {

  def apply(
    key: ChangeKey,
    timestampFrom: Timestamp,
    timestampUntil: Timestamp,
    networkChanges: NetworkChanges,
    orphanRouteChanges: Seq[ChangeSetSubsetElementRefs],
    orphanNodeChanges: Seq[ChangeSetSubsetElementRefs]
  ): ChangeSetSummary = {

    val subsets = (networkChanges.subsets ++ orphanRouteChanges.map(_.subset) ++ orphanNodeChanges.map(_.subset)).toSeq.sorted
    val happy = networkChanges.happy || orphanRouteChanges.exists(_.happy) || orphanNodeChanges.exists(_.happy)
    val investigate = networkChanges.investigate || orphanRouteChanges.exists(_.investigate) || orphanNodeChanges.exists(_.investigate)

    ChangeSetSummary(
      key,
      subsets,
      timestampFrom,
      timestampUntil,
      networkChanges,
      orphanRouteChanges,
      orphanNodeChanges,
      happy,
      investigate
    )
  }
}

case class ChangeSetSummary(
  key: ChangeKey,
  subsets: Seq[Subset],
  timestampFrom: Timestamp,
  timestampUntil: Timestamp,
  networkChanges: NetworkChanges,
  orphanRouteChanges: Seq[ChangeSetSubsetElementRefs],
  orphanNodeChanges: Seq[ChangeSetSubsetElementRefs],
  happy: Boolean,
  investigate: Boolean
) {

  def noImpact: Boolean = !(happy || investigate)

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("key", key).
    field("subsets", subsets).
    field("timestampFrom", timestampFrom).
    field("timestampUntil", timestampUntil).
    field("networkChanges", networkChanges).
    field("orphanRouteChanges", orphanRouteChanges).
    field("orphanNodeChanges", orphanNodeChanges).
    field("happy", happy).
    field("investigate", investigate).
    build
}
