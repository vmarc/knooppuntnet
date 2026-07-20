package kpn.api.custom;

import java.util.Optional;

public record ApiResponse<T>(
  Optional<Timestamp> situationOn,
  Long version,
  Optional<T> result
) {
}
