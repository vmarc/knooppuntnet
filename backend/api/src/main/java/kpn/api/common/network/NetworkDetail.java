package kpn.api.common.network;

import kpn.api.common.Bounds;
import kpn.api.common.LatLonImpl;
import kpn.api.common.network.Integrity;
import kpn.api.custom.Day;
import kpn.api.custom.Timestamp;

import java.util.Optional;

public record NetworkDetail(
  Long km,
  Long meters,
  Timestamp lastUpdated,
  Timestamp relationLastUpdated,
  Optional<Day> lastSurvey,
  Long brokenRouteCount,
  String brokenRoutePercentage,
  Integrity integrity,
  Long inaccessibleRouteCount,
  Long connectionCount,
  Optional<Bounds> bounds,
  Optional<LatLonImpl> center
) {}
