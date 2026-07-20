package kpn.api.common.network;

import kpn.api.common.RouteScope;
import kpn.api.common.RouteType;
import kpn.api.common.data.raw.Raw;
import kpn.api.common.data.raw.RawMember;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record NetworkBaseData(
  Raw raw,
  Optional<String> name,
  RouteType routeType,
  RouteScope routeScope,
  ImmutableList<RawMember> members
) {
}

