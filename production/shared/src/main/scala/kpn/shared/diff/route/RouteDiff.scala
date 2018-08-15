package kpn.shared.diff.route

import kpn.shared.common.ReferencedElements
import kpn.shared.diff.TagDiffs
import kpn.shared.diff.common.FactDiffs

case class RouteDiff(
  nameDiff: Option[RouteNameDiff] = None,
  roleDiff: Option[RouteRoleDiff] = None,
  factDiffs: Option[FactDiffs] = None,
  nodeDiffs: Seq[RouteNodeDiff] = Seq(),
  memberOrderChanged: Boolean = false,
  tagDiffs: Option[TagDiffs] = None
) {

  def isEmpty: Boolean = ! nonEmpty

  def nonEmpty: Boolean = nameDiff.nonEmpty ||
    roleDiff.nonEmpty ||
    factDiffs.nonEmpty ||
    nodeDiffs.nonEmpty ||
    memberOrderChanged ||
    tagDiffs.nonEmpty

  def happy: Boolean = factDiffs.exists(_.happy)

  def investigate: Boolean = factDiffs.exists(_.investigate)

  def referencedElements: ReferencedElements = {
    ReferencedElements.merge(nodeDiffs.map(_.referencedElements):_*)
  }
}
