package kpn.api.common.subset;

import kpn.api.common.Fact;
import kpn.api.common.subset.NetworkFactRefs;
import kpn.api.common.subset.SubsetInfo;

import com.google.common.collect.ImmutableList;

public record SubsetFactDetailsPage(
  SubsetInfo subsetInfo,
  Fact fact,
  ImmutableList<NetworkFactRefs> networks
) {
}

/*
package kpn.api.common.subset

import kpn.api.common.Fact

case class SubsetFactDetailsPage(subsetInfo: SubsetInfo, fact: Fact, networks: Seq[NetworkFactRefs]) {
  def refCount: Int = networks.map { n => n.factRefs.size }.sum
}

*/
