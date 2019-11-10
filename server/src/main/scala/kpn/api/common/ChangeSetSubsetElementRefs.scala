package kpn.api.common

import kpn.api.custom.Subset

case class ChangeSetSubsetElementRefs(
  subset: Subset,
  elementRefs: ChangeSetElementRefs
) {

  def elementIds: Seq[Long] = elementRefs.elementIds

  def happy: Boolean = elementRefs.happy

  def investigate: Boolean = elementRefs.investigate
}
