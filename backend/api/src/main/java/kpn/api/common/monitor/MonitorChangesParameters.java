package kpn.api.common.monitor;

public record MonitorChangesParameters(
  Long pageSize,
  Long pageIndex,
  Boolean impact
) {
}

/*
package kpn.api.common.monitor

case class MonitorChangesParameters(
  pageSize: Long = 5,
  pageIndex: Long = 0,
  impact: Boolean = false
)

*/
