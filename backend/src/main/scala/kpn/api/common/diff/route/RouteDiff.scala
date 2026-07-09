package kpn.api.common.diff.route

import kpn.api.common.common.ReferencedElements
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.common.FactDiffs

object RouteDiff {
  def empty: RouteDiff = {
    RouteDiff(
      nameDiff = None,
      roleDiff = None,
      factDiffs = None,
      nodeDiffs = Seq.empty,
      memberOrderChanged = false,
      tagDiffs = None
    )
  }
}

case class RouteDiff(
  nameDiff: Option[RouteNameDiff],
  roleDiff: Option[RouteRoleDiff],
  factDiffs: Option[FactDiffs],
  nodeDiffs: Seq[RouteNodeDiff],
  memberOrderChanged: Boolean,
  tagDiffs: Option[TagDiffs]
) {

  def isEmpty: Boolean = !nonEmpty

  def nonEmpty: Boolean = nameDiff.nonEmpty ||
    roleDiff.nonEmpty ||
    factDiffs.nonEmpty ||
    nodeDiffs.nonEmpty ||
    memberOrderChanged ||
    tagDiffs.nonEmpty

  def happy: Boolean = factDiffs.exists(_.happy)

  def investigate: Boolean = factDiffs.exists(_.investigate)

  def referencedElements: ReferencedElements = {
    ReferencedElements.merge(nodeDiffs.map(_.referencedElements) *)
  }
}
