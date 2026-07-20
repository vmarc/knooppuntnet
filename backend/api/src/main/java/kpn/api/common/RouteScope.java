package kpn.api.common;

import com.google.common.collect.ImmutableList;

public enum RouteScope {
  LOCAL,
  REGIONAL,
  NATIONAL,
  INTERNATIONAL,
  UNKNOWN;

  private static final ImmutableList<RouteScope> all = ImmutableList.of(
    LOCAL,
    REGIONAL,
    NATIONAL,
    INTERNATIONAL
  );

  public static final ImmutableList<RouteScope> all() {
    return all;
  }
}
