package kpn.server.repository

import kpn.api.common.Fact
import kpn.api.common.subset.SubsetFactRefs
import kpn.api.custom.Subset

trait FactRefRepository {
  def factRefs(subset: Subset, fact: Fact): SubsetFactRefs
}
