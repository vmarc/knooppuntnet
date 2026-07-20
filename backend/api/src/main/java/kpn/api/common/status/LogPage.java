package kpn.api.common.status;

import kpn.api.common.status.ActionTimestamp;
import kpn.api.common.status.BarChart;

public record LogPage(
  ActionTimestamp timestamp,
  String periodType,
  String periodTitle,
  String previous,
  String next,
  BarChart tile,
  BarChart tileRobot,
  BarChart api,
  BarChart apiRobot,
  BarChart analysis,
  BarChart analysisRobot,
  BarChart robot,
  BarChart nonRobot
) {}

