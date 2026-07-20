package kpn.api.common.data;

import kpn.api.custom.Timestamp;

public record MetaData(
  Long version,
  Timestamp timestamp,
  Long changeSetId
) implements Meta {}
