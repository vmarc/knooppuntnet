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

/*
package kpn.api.common.monitor

case class MonitorRouteUpdateStatus(
  steps: Seq[MonitorRouteUpdateStep] = Seq.empty,
  done: Boolean = false,
  errors: Seq[String] = Seq.empty,
  exception: Option[String] = None
)

*/
