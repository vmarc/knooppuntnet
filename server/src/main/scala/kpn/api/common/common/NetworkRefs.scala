package kpn.api.common.common

import kpn.api.custom.Country
import kpn.api.custom.NetworkType

/*
  List of 'node' or 'route' references in a given network.
 */
case class NetworkRefs(
  country: Country,
  networkType: NetworkType,
  networkRef: Option[Ref],
  refType: String /* "node" | "route" */,
  refs: Seq[Ref]
) {
  def factCount: Int = if(refs.isEmpty) 1 else refs.size
}
