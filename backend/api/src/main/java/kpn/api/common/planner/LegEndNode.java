package kpn.api.common.planner;

import kpn.core.doc.Storable;

public record LegEndNode(
  Long nodeId
) implements Storable {}
