package kpn.api.common.location;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record LocationNode(
  String name,
  Long nodeCount,
  Long routeCount,
  Long factCount,
  ImmutableList<LocationNode> children
) {
}

/*
package kpn.api.common.location

case class LocationNode(
  name: String,
  nodeCount: Long,
  routeCount: Long,
  factCount: Long,
  children: Option[Seq[LocationNode]]
)

*/
