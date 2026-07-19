package kpn.api.common.common;

import kpn.api.common.Country;
import kpn.api.common.RouteType;
import kpn.api.common.common.Ref;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record NetworkRefs(
  Country country,
  RouteType routeType,
  Optional<Ref> networkRef,
  String refType,
  ImmutableList<Ref> refs
) {
}

/*
package kpn.api.common.common

import kpn.api.common.Country
import kpn.api.common.RouteType

*** 
  List of 'node' or 'route' references in a given network.
 *** 
case class NetworkRefs(
  country: Country,
  routeType: RouteType,
  networkRef: Option[Ref],
  refType: String,
  refs: Seq[Ref]
) {
  def factCount: Int = if (refs.isEmpty) 1 else refs.size
}

*/
