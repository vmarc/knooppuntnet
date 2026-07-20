package kpn.api.common.status;

import kpn.api.common.status.ActionTimestamp;
import kpn.api.common.status.DiskUsage;

public record Status(
  ActionTimestamp timestamp,
  DiskUsage diskUsage
) {
}
