package kpn.api.common.monitor;

import java.util.Optional;

public record MonitorRouteInfoPage(
  Long relationId,
  Boolean active,
  Boolean hasRouteTags,
  Optional<String> name,
  Optional<String> ref,
  Optional<String> from,
  Optional<String> to,
  Optional<String> operator,
  Optional<String> website,
  Optional<String> symbol
) {}

/* TODO migrate
package kpn.api.common.monitor

case class MonitorRouteInfoPage(
  relationId: Long,
  active: Boolean = false,
  hasRouteTags: Boolean = false,
  name: Option[String] = None,
  ref: Option[String] = None,
  from: Option[String] = None,
  to: Option[String] = None,
  operator: Option[String] = None,
  website: Option[String] = None,
  symbol: Option[String] = None
)

*/
