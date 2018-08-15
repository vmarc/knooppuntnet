package kpn.client.components.subset

import kpn.shared.Subset
import kpn.shared.subset.SubsetInfo

object SubsetInfoCache {

  private val subsetInfoMap = scala.collection.mutable.Map[Subset, SubsetInfo](
    Subset.beHiking -> SubsetInfo("be", "rwn"),
    Subset.nlHiking -> SubsetInfo("nl", "rwn"),
    Subset.deHiking -> SubsetInfo("de", "rwn"),
    Subset.beBicycle -> SubsetInfo("be", "rcn"),
    Subset.nlBicycle -> SubsetInfo("nl", "rcn"),
    Subset.deBicycle -> SubsetInfo("de", "rcn"),
    Subset.nlHorse -> SubsetInfo("nl", "rhn"),
    Subset.nlMotorboat -> SubsetInfo("nl", "rmn"),
    Subset.nlCanoe -> SubsetInfo("nl", "rpn")
  )

  def get(subset: Subset): SubsetInfo = {
    subsetInfoMap(subset)
  }

  def put(subset: Subset, subsetInfo: SubsetInfo): Unit = {
    subsetInfoMap.put(subset, subsetInfo)
  }
}
