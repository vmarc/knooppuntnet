package kpn.api.common.subset

import kpn.api.common.Fact

case class SubsetFactDetailsPage(subsetInfo: SubsetInfo, fact: Fact, networks: Seq[NetworkFactRefs]) {
  def refCount: Int = networks.map { n => n.factRefs.size }.sum
}
