package kpn.api.common.status;

import kpn.api.common.status.NameValue;

import com.google.common.collect.ImmutableList;

public record BarChart2dValue(
  String name,
  ImmutableList<NameValue> series
) {
}
