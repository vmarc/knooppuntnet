package kpn.api.custom;

import kpn.api.common.Country;
import kpn.api.common.RouteType;

import java.util.Locale;
import java.util.Optional;
import com.google.common.collect.ImmutableList;

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

  // Subset instances
  public static Subset beHiking      = new Subset(Country.BE, RouteType.HIKING);
  public static Subset beCycling     = new Subset(Country.BE, RouteType.CYCLING);
  public static Subset beHorseRiding = new Subset(Country.BE, RouteType.HORSE_RIDING);

  public static Subset nlHiking       = new Subset(Country.NL, RouteType.HIKING);
  public static Subset nlCycling      = new Subset(Country.NL, RouteType.CYCLING);
  public static Subset nlHorseRiding  = new Subset(Country.NL, RouteType.HORSE_RIDING);
  public static Subset nlCanoe        = new Subset(Country.NL, RouteType.CANOE);
  public static Subset nlMotorboat    = new Subset(Country.NL, RouteType.MOTORBOAT);
  public static Subset nlInlineSkates = new Subset(Country.NL, RouteType.INLINE_SKATING);

  public static Subset deHiking      = new Subset(Country.DE, RouteType.HIKING);
  public static Subset deCycling     = new Subset(Country.DE, RouteType.CYCLING);
  public static Subset deHorseRiding = new Subset(Country.DE, RouteType.HORSE_RIDING);

  public static Subset frHiking      = new Subset(Country.FR, RouteType.HIKING);
  public static Subset frCycling     = new Subset(Country.FR, RouteType.CYCLING);
  public static Subset frHorseRiding = new Subset(Country.FR, RouteType.HORSE_RIDING);
  public static Subset frCanoe       = new Subset(Country.FR, RouteType.CANOE);

  public static Subset atCycling = new Subset(Country.AT, RouteType.CYCLING);

  public static Subset esHiking  = new Subset(Country.ES, RouteType.HIKING);
  public static Subset esCycling = new Subset(Country.ES, RouteType.CYCLING);

  public static Subset dkCycling = new Subset(Country.DK, RouteType.CYCLING);

  public static Subset plHiking  = new Subset(Country.PL, RouteType.HIKING);
  public static Subset plCycling = new Subset(Country.PL, RouteType.CYCLING);

  public static ImmutableList<Subset> all = ImmutableList.of(
    nlCycling,
    beCycling,
    deCycling,
    frCycling,
    atCycling,
    esCycling,
    dkCycling,
    plCycling,
    nlHiking,
    beHiking,
    deHiking,
    frHiking,
    esHiking,
    plHiking,
    nlHorseRiding,
    beHorseRiding,
    deHorseRiding,
    frHorseRiding,
    nlCanoe,
    frCanoe,
    nlMotorboat,
    nlInlineSkates
  );

  public static Optional<Subset> ofName(String domain, String routeTypeName) {
    return all.stream()
      .filter(s -> s.country().name().equals(domain) && s.routeType().name().equals(routeTypeName))
      .findFirst();
  }

  public static Optional<Subset> of(Country country, RouteType routeType) {
    return all.stream()
      .filter(s -> s.country().equals(country) && s.routeType().equals(routeType))
      .findFirst();
  }
}
