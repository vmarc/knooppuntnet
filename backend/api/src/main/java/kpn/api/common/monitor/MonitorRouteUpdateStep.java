package kpn.api.common.monitor;

public record MonitorRouteUpdateStep(
  String name,
  String status
) {}

/* TODO migrate
package kpn.api.common.monitor

case class MonitorRouteUpdateStep(
  name: String,
  status: String = "todo" // "todo" | "busy" | "done"
)

*/
