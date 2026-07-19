package kpn.api.common.route;

import com.google.common.collect.ImmutableList;

public record RoutePath(
  Long id,
  String name,
  ImmutableList<Long> elementIds
) {
}

/*
package kpn.api.common.route

case class RoutePath(
  id: Long,
  name: String, // forward, backward, start-tentacle-1, ...
  elementIds: Seq[Long]
)

*/
