package kpn.api.common.subset

import kpn.api.common.common.Ref

case class NetworkFactRefs(networkId: Long, networkName: String, factRefs: Seq[Ref] = Seq())
