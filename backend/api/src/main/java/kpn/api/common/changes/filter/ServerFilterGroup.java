package kpn.api.common.changes.filter;

import kpn.api.common.changes.filter.ServerFilterOption;

import com.google.common.collect.ImmutableList;

public record ServerFilterGroup(
  String selected,
  ImmutableList<ServerFilterOption> options
) {
}
