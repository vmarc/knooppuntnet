package kpn.api.common.network;

import kpn.api.common.network.NetworkShape;

public record NetworkMapInfo(
  Long id,
  String name,
  NetworkShape map
) {
}
