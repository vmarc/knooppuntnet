package kpn.api.common.network;

import kpn.api.common.Bounds;

public record NetworkShape(
  Bounds bounds,
  String coordinates
) {
}
