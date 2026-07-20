package kpn.api.common.monitor;

import kpn.api.common.monitor.MonitorReferenceType;
import kpn.api.common.monitor.MonitorRouteSummary;
import kpn.api.common.route.RouteDetails;
import kpn.api.custom.Timestamp;

import java.util.Optional;

public record MonitorRouteDetailsPage(
  MonitorRouteSummary summary,
  Optional<String> comment,
  Optional<String> symbol,
  Optional<Timestamp> analysisTimestamp,
  Optional<Long> analysisDuration,
  MonitorReferenceType referenceType,
  Optional<Timestamp> referenceTimestamp,
  Optional<String> referenceFilename,
  Long referenceDistance,
  Long deviationDistance,
  Boolean happy,
  Optional<RouteDetails> details
) {
}
