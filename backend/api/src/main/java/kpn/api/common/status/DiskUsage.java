package kpn.api.common.status;

import kpn.api.common.status.BarChart;

public record DiskUsage(
  BarChart frontend,
  BarChart database,
  BarChart backend
) {}
