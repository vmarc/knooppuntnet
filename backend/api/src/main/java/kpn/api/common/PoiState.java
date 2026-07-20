package kpn.api.common;

import kpn.api.custom.Timestamp;
import kpn.core.doc.WithStringId;

import java.util.Optional;

public record PoiState(
  String _id,
  Optional<String> imageLink,
  Optional<String> imageStatus,
  Optional<String> imageStatusDetail,
  Optional<Timestamp> imageFirstSeen,
  Optional<Timestamp> imageLastSeen
) implements WithStringId {
}
