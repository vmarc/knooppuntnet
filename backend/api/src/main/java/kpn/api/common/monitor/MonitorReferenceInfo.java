package kpn.api.common.monitor;

import kpn.api.common.Bounds;
import kpn.api.common.monitor.MonitorReferenceType;
import kpn.api.custom.Timestamp;

import java.util.Optional;

public record MonitorReferenceInfo(
  Timestamp created,
  String user,
  Bounds referenceBounds,
  Long referenceDistance,
  MonitorReferenceType referenceType,
  Timestamp referenceTimestamp,
  Long referenceSegmentCount,
  Optional<String> referenceFilename
) {
}
