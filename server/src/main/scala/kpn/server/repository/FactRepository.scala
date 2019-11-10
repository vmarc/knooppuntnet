package kpn.server.repository

import akka.util.Timeout
import kpn.api.common.subset.NetworkFactRefs
import kpn.api.custom.Fact
import kpn.api.custom.Subset

trait FactRepository {
  def factsPerNetwork(subset: Subset, fact: Fact, timeout: Timeout, stale: Boolean = true): Seq[NetworkFactRefs]
}
