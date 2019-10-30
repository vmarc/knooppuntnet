package kpn.shared.subset

import kpn.shared.common.Ref

case class NetworkFactRefs(networkId: Long, networkName: String, factRefs: Seq[Ref] = Seq())
