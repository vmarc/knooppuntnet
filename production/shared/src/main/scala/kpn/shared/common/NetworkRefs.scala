package kpn.shared.common

import kpn.shared.Country
import kpn.shared.NetworkType

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
