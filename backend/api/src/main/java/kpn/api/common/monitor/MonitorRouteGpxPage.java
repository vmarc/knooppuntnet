package kpn.api.common.monitor;

import kpn.api.custom.Timestamp;

import java.util.Optional;

public record MonitorRouteGpxPage(
  String groupName,
  String routeName,
  Long subRelationId,
  String subRelationDescription,
  Timestamp referenceTimestamp,
  Optional<String> referenceFilename,
  Long referenceDistance
) {}
