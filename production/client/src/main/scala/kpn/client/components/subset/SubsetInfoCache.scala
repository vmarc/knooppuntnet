// Migrated to Angular: subset-cache.service.ts
package kpn.client.components.subset

import kpn.shared.Subset
import kpn.shared.subset.SubsetInfo

object SubsetInfoCache {

  private val subsetInfoMap = scala.collection.mutable.Map[Subset, SubsetInfo](
    Subset.nlBicycle -> SubsetInfo("nl", "rcn"),
    Subset.nlHiking -> SubsetInfo("nl", "rwn"),
    Subset.nlHorseRiding -> SubsetInfo("nl", "rhn"),
    Subset.nlMotorboat -> SubsetInfo("nl", "rmn"),
    Subset.nlCanoe -> SubsetInfo("nl", "rpn"),
    Subset.nlInlineSkates -> SubsetInfo("nl", "rpn"),
    Subset.beBicycle -> SubsetInfo("be", "rcn"),
    Subset.beHiking -> SubsetInfo("be", "rwn"),
    Subset.beHorseRiding -> SubsetInfo("be", "rhn"),
    Subset.deHiking -> SubsetInfo("de", "rwn"),
    Subset.deBicycle -> SubsetInfo("de", "rcn"),
    Subset.deHorseRiding -> SubsetInfo("de", "rhn"),
    Subset.frHiking -> SubsetInfo("fr", "rwn"),
    Subset.frBicycle -> SubsetInfo("fr", "rcn"),
    Subset.frHorseRiding -> SubsetInfo("fr", "rhn"),
    Subset.atBicycle -> SubsetInfo("at", "rcn")
  )

  def get(subset: Subset): SubsetInfo = {
    subsetInfoMap(subset)
  }

  def put(subset: Subset, subsetInfo: SubsetInfo): Unit = {
    subsetInfoMap.put(subset, subsetInfo)
  }
}
