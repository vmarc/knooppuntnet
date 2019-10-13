package kpn.server.analyzer.engine.changes.diff

import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.shared.Fact
import kpn.shared.Subset
import kpn.shared.common.Ref
import kpn.shared.common.ReferencedElements
import kpn.shared.data.raw.RawWay
import kpn.shared.diff.WayUpdate
import kpn.shared.diff.route.RouteDiff

case class RouteUpdate(
  before: RouteAnalysis,
  after: RouteAnalysis,
  removedWays: Seq[RawWay] = Seq.empty,
  addedWays: Seq[RawWay] = Seq.empty,
  updatedWays: Seq[WayUpdate] = Seq.empty,
  diffs: RouteDiff = RouteDiff(),
  facts: Seq[Fact] = Seq.empty
) {

  def subsets: Seq[Subset] = (before.subset ++ after.subset).toSeq.distinct.sorted

  def id: Long = after.id

  def name: String = after.route.summary.name

  def toRef: Ref = Ref(id, name)

  def nonEmpty: Boolean = removedWays.nonEmpty || addedWays.nonEmpty || updatedWays.nonEmpty || diffs.nonEmpty

  def isNewVersion: Boolean = before.relation.version != after.relation.version

  def happy: Boolean = diffs.happy

  def investigate: Boolean = diffs.investigate

  def referencedElements: ReferencedElements = {
    diffs.referencedElements
  }
}
