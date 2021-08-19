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
    routeChanges: Seq[ChangeSetSubsetElementRefs],
    nodeChanges: Seq[ChangeSetSubsetElementRefs],
    trees: Seq[LocationChangesTree]
  ): ChangeSetSummary = {

    val subsets = (networkChanges.subsets ++ routeChanges.map(_.subset) ++ nodeChanges.map(_.subset)).toSeq.sorted
    val happy = networkChanges.happy || routeChanges.exists(_.happy) || nodeChanges.exists(_.happy)
    val investigate = networkChanges.investigate || routeChanges.exists(_.investigate) || nodeChanges.exists(_.investigate)

    val subsetAnalyses = subsets.map { subset =>

      val happy = networkChanges.creates.filter(_.subsets.contains(subset)).exists(_.happy) ||
        networkChanges.updates.filter(_.subsets.contains(subset)).exists(_.happy) ||
        networkChanges.deletes.filter(_.subsets.contains(subset)).exists(_.happy) ||
        routeChanges.filter(_.subset == subset).exists(_.happy) ||
        nodeChanges.filter(_.subset == subset).exists(_.happy)

      val investigate = networkChanges.creates.filter(_.subsets.contains(subset)).exists(_.investigate) ||
        networkChanges.updates.filter(_.subsets.contains(subset)).exists(_.investigate) ||
        networkChanges.deletes.filter(_.subsets.contains(subset)).exists(_.investigate) ||
        routeChanges.filter(_.subset == subset).exists(_.investigate) ||
        nodeChanges.filter(_.subset == subset).exists(_.investigate)

      ChangeSetSubsetAnalysis(subset, happy, investigate)
    }

    val locations = locationsIn(trees)

    ChangeSetSummary(
      key.toShortId,
      key,
      subsets,
      timestampFrom,
      timestampUntil,
      networkChanges,
      routeChanges,
      nodeChanges,
      subsetAnalyses,
      trees,
      locations,
      happy,
      investigate,
      happy || investigate
    )
  }

  private def locationsIn(trees: Seq[LocationChangesTree]): Seq[String] = {
    trees.flatMap { tree =>
      locationsInTreeNodes(tree.children) :+ tree.locationName
    }.distinct.sorted
  }

  private def locationsInTreeNodes(treeNodes: Seq[LocationChangesTreeNode]): Seq[String] = {
    treeNodes.flatMap { treeNode =>
      locationsInTreeNodes(treeNode.children) :+ treeNode.locationName
    }
  }
}

case class ChangeSetSummary(
  _id: String,
  key: ChangeKey,
  subsets: Seq[Subset],
  timestampFrom: Timestamp,
  timestampUntil: Timestamp,
  networkChanges: NetworkChanges,
  routeChanges: Seq[ChangeSetSubsetElementRefs],
  nodeChanges: Seq[ChangeSetSubsetElementRefs],
  subsetAnalyses: Seq[ChangeSetSubsetAnalysis],
  trees: Seq[LocationChangesTree],
  locations: Seq[String],
  happy: Boolean,
  investigate: Boolean,
  impact: Boolean
) extends WithStringId
