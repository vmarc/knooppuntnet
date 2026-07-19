package kpn.api.common.changes.filter;

public record ServerFilterOption(
  String name,
  Long count,
  Boolean selected
) {
}

/*
package kpn.api.common.changes.filter

case class ServerFilterOption(
  name: String,
  count: Long,
  selected: Boolean = false
)

*/
