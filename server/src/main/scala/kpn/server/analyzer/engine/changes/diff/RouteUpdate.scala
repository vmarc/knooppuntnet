package kpn.server.analyzer.engine.changes.diff

import kpn.api.common.common.Ref
import kpn.api.common.common.ReferencedElements
import kpn.api.common.data.raw.RawWay
import kpn.api.common.diff.RouteData
import kpn.api.common.diff.WayUpdate
import kpn.api.common.diff.route.RouteDiff
import kpn.api.custom.Fact
import kpn.api.custom.Subset

case class RouteUpdate(
  before: RouteData,
  after: RouteData,
  removedWays: Seq[RawWay] = Seq.empty,
  addedWays: Seq[RawWay] = Seq.empty,
  updatedWays: Seq[WayUpdate] = Seq.empty,
  diffs: RouteDiff = RouteDiff(),
  facts: Seq[Fact] = Seq.empty
) {

  def subsets: Seq[Subset] = (before.subsets ++ after.subsets).toSeq.distinct.sorted

  def id: Long = after.relationId

  def name: String = after.name

  def toRef: Ref = Ref(id, name)

  def nonEmpty: Boolean = removedWays.nonEmpty || addedWays.nonEmpty || updatedWays.nonEmpty || diffs.nonEmpty

  def isNewVersion: Boolean = before.meta.version != after.meta.version

  def happy: Boolean = diffs.happy

  def investigate: Boolean = diffs.investigate

  def referencedElements: ReferencedElements = {
    diffs.referencedElements
  }
}
