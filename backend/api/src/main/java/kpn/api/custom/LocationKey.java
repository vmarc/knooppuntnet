package kpn.api.custom;

import kpn.api.common.Country;
import kpn.api.common.RouteType;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public record LocationKey(
  RouteType routeType,
  Country country,
  String name
) {}
