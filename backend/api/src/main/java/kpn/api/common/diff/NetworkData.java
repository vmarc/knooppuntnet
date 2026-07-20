package kpn.api.common.diff;

import kpn.api.common.data.MetaData;

import java.util.Optional;

public record NetworkData(
  MetaData metaData,
  Optional<String> name
) {}
