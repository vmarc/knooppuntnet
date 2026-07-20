package kpn.api.common.changes.filter;

public record ServerFilterOption(
  String name,
  Long count,
  Boolean selected
) {
}
