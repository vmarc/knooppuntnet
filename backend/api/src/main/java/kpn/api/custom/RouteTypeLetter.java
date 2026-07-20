package kpn.api.custom;

import kpn.api.common.RouteType;

public class RouteTypeLetter {

  public static String letter(RouteType routeType) {
    switch (routeType) {
      case RouteType.HIKING:
        return "w";
      case RouteType.CYCLING:
        return "c";
      case RouteType.HORSE_RIDING:
        return "h";
      case RouteType.CANOE:
        return "p";
      case RouteType.MOTORBOAT:
        return "m";
      case RouteType.INLINE_SKATING:
        return "i";
      case RouteType.MTB:
        return "?";
      default:
        throw new IllegalArgumentException("Unknown RouteType");
    }
  }
}
