package kpn.api.common.diff.route;

import java.util.Optional;

public record RouteNameDiff(
  Optional<String> before,
  Optional<String> after
) {}
