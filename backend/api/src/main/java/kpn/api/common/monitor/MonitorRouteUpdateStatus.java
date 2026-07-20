package kpn.api.common.monitor;

import kpn.api.common.monitor.MonitorRouteUpdateStep;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record MonitorRouteUpdateStatus(
  ImmutableList<MonitorRouteUpdateStep> steps,
  Boolean done,
  ImmutableList<String> errors,
  Optional<String> exception
) {
}
