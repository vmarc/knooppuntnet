package kpn.api.custom;

import kpn.api.common.Country;
import kpn.api.common.RouteType;

import java.util.Locale;

public record Subset(
  Country country,
  RouteType routeType
) implements Comparable<Subset> {

  public String key() {
    return country.name().toLowerCase() + ":" + routeType.name().toLowerCase();
  }

  public String name() {
    return country.name().toLowerCase() + "-" + routeType.name().toLowerCase();
  }

  public String string() {
    return country.name().toLowerCase() + "/" + routeType.name().toLowerCase();
  }

  public int compareTo(Subset that) {
    return java.util.Comparator
      .comparing((Subset s) -> s.country.name())
      .thenComparing(s -> s.routeType.name())
      .compare(this, that);
  }
}
