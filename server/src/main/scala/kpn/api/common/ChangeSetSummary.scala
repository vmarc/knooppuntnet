package kpn.api.common

import kpn.api.base.WithStringId
import kpn.api.common.changes.details.ChangeKey
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp

object ChangeSetSummary {

  def apply(
    key: ChangeKey,
    timestampFrom: Timestamp,
    timestampUntil: Timestamp,
    networkChanges: NetworkChanges,
    orphanRouteChanges: Seq[ChangeSetSubsetElementRefs],
    orphanNodeChanges: Seq[ChangeSetSubsetElementRefs],
    locationChanges: Seq[LocationChanges]
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

    val locations = locationChanges.flatMap(_.locationNames).distinct.sorted

    ChangeSetSummary(
      key.toShortId,
      key,
      subsets,
      timestampFrom,
      timestampUntil,
      networkChanges,
      orphanRouteChanges,
      orphanNodeChanges,
      subsetAnalyses,
      locationChanges,
      locations,
      happy,
      investigate,
      happy || investigate
    )
  }
}

case class ChangeSetSummary(
  _id: String,
  key: ChangeKey,
  subsets: Seq[Subset],
  timestampFrom: Timestamp,
  timestampUntil: Timestamp,
  // network oriented analysis results:
  networkChanges: NetworkChanges,
  orphanRouteChanges: Seq[ChangeSetSubsetElementRefs],
  orphanNodeChanges: Seq[ChangeSetSubsetElementRefs],
  subsetAnalyses: Seq[ChangeSetSubsetAnalysis],
  // location oriented analysis results:
  locationChanges: Seq[LocationChanges],
  locations: Seq[String],
  // overall analysis results
  happy: Boolean,
  investigate: Boolean,
  impact: Boolean
) extends WithStringId
