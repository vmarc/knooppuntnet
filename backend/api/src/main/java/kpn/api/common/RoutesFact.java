package kpn.api.common;

import kpn.api.common.common.Ref;

import com.google.common.collect.ImmutableList;

public record RoutesFact(
  ImmutableList<Ref> routes
) {
}

/*
package kpn.api.common

import kpn.api.common.common.Ref

case class RoutesFact(routes: Seq[Ref])

*/
