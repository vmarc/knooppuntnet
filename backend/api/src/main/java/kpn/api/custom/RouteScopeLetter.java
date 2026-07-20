package kpn.api.custom;

import kpn.api.common.RouteScope;

public class RouteScopeLetter {

  public static String letter(RouteScope scope) {
    // Match the scope to determine the letter
    switch (scope) {
      case RouteScope.LOCAL:
        return "l";
      case RouteScope.REGIONAL:
        return "r";
      case RouteScope.NATIONAL:
        return "n";
      case RouteScope.INTERNATIONAL:
        return "i";
      case RouteScope.UNKNOWN:
        return "l";
      default:
        throw new IllegalStateException("Unexpected value: " + scope);
    }
  }

}
