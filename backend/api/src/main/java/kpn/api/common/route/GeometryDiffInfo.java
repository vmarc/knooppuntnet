package kpn.api.common.route;

import kpn.api.common.route.GeometryDiffInfoDetail;

public record GeometryDiffInfo(
  GeometryDiffInfoDetail common,
  GeometryDiffInfoDetail added,
  GeometryDiffInfoDetail removed
) {}
