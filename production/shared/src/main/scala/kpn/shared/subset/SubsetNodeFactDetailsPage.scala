package kpn.shared.subset

import kpn.shared.Fact

case class SubsetNodeFactDetailsPage(subsetInfo: SubsetInfo, fact: Fact, networks: Seq[NetworkFactRefs]) {
  def refCount: Int = networks.map { n => n.factRefs.size }.sum
}
