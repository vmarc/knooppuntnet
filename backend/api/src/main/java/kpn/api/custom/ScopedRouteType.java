package kpn.api.custom;

import kpn.api.common.RouteScope;
import kpn.api.common.RouteType;

import java.util.stream.Stream;
import com.google.common.collect.ImmutableList;

public record ScopedRouteType(
  RouteType routeType,
  RouteScope routeScope,
  String key
) {

  public ScopedRouteType(RouteType routeType, RouteScope routeScope) {
    String routeTypeLetter = RouteTypeLetter.letter(routeType);
    String routeScopeLetter = RouteScopeLetter.letter(routeScope);
    String key = routeScopeLetter + routeTypeLetter + "n";
    this(routeType, routeScope, key);
  }

  public String nodeRefTagKey() {
    return key + "_ref";
  }

  public String nodeNameTagKey() {
    return key + "_name";
  }

  public String proposedNodeRefTagKey() {
    return "proposed:" + key + "_ref";
  }

  public String proposedNodeNameTagKey() {
    return "proposed:" + key + "_name";
  }

  public String expectedRouteRelationsTag() {
    return "expected_" + key + "_route_relations";
  }

  public static ScopedRouteType rwn = new ScopedRouteType(RouteType.HIKING, RouteScope.REGIONAL);
  public static ScopedRouteType rcn = new ScopedRouteType(RouteType.CYCLING, RouteScope.REGIONAL);
  public static ScopedRouteType rmn = new ScopedRouteType(RouteType.CANOE, RouteScope.REGIONAL);
  public static ScopedRouteType lwn = new ScopedRouteType(RouteType.HIKING, RouteScope.LOCAL);
  public static ScopedRouteType lcn = new ScopedRouteType(RouteType.CYCLING, RouteScope.LOCAL);
  public static ScopedRouteType lpn = new ScopedRouteType(RouteType.CANOE, RouteScope.LOCAL);

  public static ImmutableList<ScopedRouteType> all() {
    return Stream.of(RouteType.values()).flatMap(routeType ->
      RouteScope.all().stream().map(scope -> new ScopedRouteType(routeType, scope))
    ).collect(ImmutableList.toImmutableList());
  }

//  public static Optional<ScopedRouteType> withKey(String key) {
//    all.find(_.key == key)
//  }

//  public statuc ScopedRouteType from(RouteType routeType, RouteScope routeScope) {
//    all.find(ns => ns.routeType == routeType && ns.routeScope == routeScope).get
//  }

}
