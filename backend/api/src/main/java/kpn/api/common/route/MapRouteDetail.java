package kpn.api.common.route;

import kpn.api.common.common.Ref;

import com.google.common.collect.ImmutableList;

public record MapRouteDetail(
  Long id,
  String name,
  ImmutableList<Ref> networkReferences
) {
}

/*
package kpn.api.common.route

import kpn.api.common.common.Ref

case class MapRouteDetail(
  id: Long,
  name: String,
  networkReferences: Seq[Ref]
)

*/
