package kpn.server.repository

import kpn.api.common.FactCount
import kpn.api.common.subset.SubsetInfo
import kpn.api.custom.Subset

trait SubsetRepository {

  def subsetInfo(subset: Subset): SubsetInfo

  def subsetFactCounts(subset: Subset): Seq[FactCount]

}