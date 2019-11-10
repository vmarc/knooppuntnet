package kpn.api.common.subset

import kpn.api.custom.Fact

case class SubsetNodeFactDetailsPage(subsetInfo: SubsetInfo, fact: Fact, networks: Seq[NetworkFactRefs]) {
  def refCount: Int = networks.map { n => n.factRefs.size }.sum
}
