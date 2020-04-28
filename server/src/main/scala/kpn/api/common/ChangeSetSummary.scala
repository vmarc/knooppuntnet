package kpn.api.common

import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.common.ToStringBuilder
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp

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

    val subsetAnalyses = subsets.map { subset =>

      val happy = networkChanges.creates.filter(_.subsets.contains(subset)).exists(_.happy) ||
        networkChanges.updates.filter(_.subsets.contains(subset)).exists(_.happy) ||
        networkChanges.deletes.filter(_.subsets.contains(subset)).exists(_.happy) ||
        orphanRouteChanges.filter(_.subset == subset).exists(_.happy) ||
        orphanNodeChanges.filter(_.subset == subset).exists(_.happy)

      val investigate = networkChanges.creates.filter(_.subsets.contains(subset)).exists(_.investigate) ||
        networkChanges.updates.filter(_.subsets.contains(subset)).exists(_.investigate) ||
        networkChanges.deletes.filter(_.subsets.contains(subset)).exists(_.investigate) ||
        orphanRouteChanges.filter(_.subset == subset).exists(_.investigate) ||
        orphanNodeChanges.filter(_.subset == subset).exists(_.investigate)

      ChangeSetSubsetAnalysis(subset, happy, investigate)
    }

    ChangeSetSummary(
      key,
      subsets,
      timestampFrom,
      timestampUntil,
      networkChanges,
      orphanRouteChanges,
      orphanNodeChanges,
      subsetAnalyses,
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
  subsetAnalyses: Seq[ChangeSetSubsetAnalysis],
  happy: Boolean,
  investigate: Boolean
) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("key", key).
    field("subsets", subsets).
    field("timestampFrom", timestampFrom).
    field("timestampUntil", timestampUntil).
    field("networkChanges", networkChanges).
    field("orphanRouteChanges", orphanRouteChanges).
    field("orphanNodeChanges", orphanNodeChanges).
    field("subsetAnalyses", subsetAnalyses).
    field("happy", happy).
    field("investigate", investigate).
    build
}
