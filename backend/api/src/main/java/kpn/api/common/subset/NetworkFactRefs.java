package kpn.api.common.subset;

import kpn.api.common.common.Ref;

import com.google.common.collect.ImmutableList;

public record NetworkFactRefs(
  Long networkId,
  String networkName,
  ImmutableList<Ref> factRefs
) {
}

/*
package kpn.api.common.subset

import kpn.api.common.common.Ref

case class NetworkFactRefs(networkId: Long, networkName: String, factRefs: Seq[Ref] = Seq.empty)

*/
