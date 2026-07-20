package kpn.api.common.search;

import kpn.api.common.search.ConditionGroup;
import kpn.api.common.search.ConditionLocation;
import kpn.api.common.search.ConditionName;
import kpn.api.common.search.ConditionTag;

import java.util.Optional;

public record Condition(
  Optional<ConditionTag> tag,
  Optional<ConditionLocation> location,
  Optional<ConditionName> name,
  Optional<ConditionGroup> group
) {
}

/* TODO migrate
package kpn.api.common.search

case class Condition(
  tag: Option[ConditionTag] = None,
  location: Option[ConditionLocation] = None,
  name: Option[ConditionName] = None,
  group: Option[ConditionGroup] = None,
)

*/
