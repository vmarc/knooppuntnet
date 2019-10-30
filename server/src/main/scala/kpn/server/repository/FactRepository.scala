package kpn.server.repository

import akka.util.Timeout
import kpn.shared.Fact
import kpn.shared.Subset
import kpn.shared.subset.NetworkFactRefs

trait FactRepository {
  def factsPerNetwork(subset: Subset, fact: Fact, timeout: Timeout, stale: Boolean = true): Seq[NetworkFactRefs]
}
