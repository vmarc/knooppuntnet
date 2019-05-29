// TODO migrate to Angular
package kpn.client.components.subset

import kpn.shared.Subset
import kpn.shared.subset.SubsetInfo

object SubsetInfoCache {

  private val subsetInfoMap = scala.collection.mutable.Map[Subset, SubsetInfo](
    Subset.nlBicycle -> SubsetInfo("nl", "rcn"),
    Subset.nlHiking -> SubsetInfo("nl", "rwn"),
    Subset.nlHorse -> SubsetInfo("nl", "rhn"),
    Subset.nlMotorboat -> SubsetInfo("nl", "rmn"),
    Subset.nlCanoe -> SubsetInfo("nl", "rpn"),
    Subset.nlInlineSkates -> SubsetInfo("nl", "rpn"),
    Subset.beBicycle -> SubsetInfo("be", "rcn"),
    Subset.beHiking -> SubsetInfo("be", "rwn"),
    Subset.beHorse -> SubsetInfo("be", "rhn"),
    Subset.deHiking -> SubsetInfo("de", "rwn"),
    Subset.deBicycle -> SubsetInfo("de", "rcn"),
    Subset.deHorse -> SubsetInfo("de", "rhn")
  )

  def get(subset: Subset): SubsetInfo = {
    subsetInfoMap(subset)
  }

  def put(subset: Subset, subsetInfo: SubsetInfo): Unit = {
    subsetInfoMap.put(subset, subsetInfo)
  }
}
