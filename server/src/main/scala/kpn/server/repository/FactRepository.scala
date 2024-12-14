package kpn.server.repository

import kpn.api.common.Fact
import kpn.api.common.subset.NetworkFactRefs
import kpn.api.custom.Subset

trait FactRepository {
  def factsPerNetwork(subset: Subset, fact: Fact): Seq[NetworkFactRefs]
}
