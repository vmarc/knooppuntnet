package kpn.api.common.diff.route

import kpn.api.common.common.ReferencedElements
import kpn.api.common.common.ToStringBuilder
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.common.FactDiffs

case class RouteDiff(
  nameDiff: Option[RouteNameDiff] = None,
  roleDiff: Option[RouteRoleDiff] = None,
  factDiffs: Option[FactDiffs] = None,
  nodeDiffs: Seq[RouteNodeDiff] = Seq.empty,
  memberOrderChanged: Boolean = false,
  tagDiffs: Option[TagDiffs] = None
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
    ReferencedElements.merge(nodeDiffs.map(_.referencedElements): _*)
  }

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("nameDiff", nameDiff).
    field("roleDiff", roleDiff).
    field("factDiffs", factDiffs).
    field("nodeDiffs", nodeDiffs).
    field("memberOrderChanged", memberOrderChanged).
    field("tagDiffs", tagDiffs).
    build
}
