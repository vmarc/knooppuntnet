package kpn.api.common.status;

import kpn.api.common.status.ActionTimestamp;
import kpn.api.common.status.BarChart;

public record SystemStatusPage(
  ActionTimestamp timestamp,
  String periodType,
  String periodTitle,
  String previous,
  String next,
  BarChart backendDiskSpaceUsed,
  BarChart backendDiskSpaceAvailable,
  BarChart backendDiskSpaceOverpass,
  BarChart analysisDocCount,
  BarChart analysisDiskSize,
  BarChart analysisDiskSizeExternal,
  BarChart analysisDataSize,
  BarChart changesDocCount,
  BarChart changesDiskSize,
  BarChart changesDiskSizeExternal,
  BarChart changesDataSize
) {
}
