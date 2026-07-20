package kpn.api.common.poi;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record LocationPoiInfo(
  Long rowIndex,
  String _id,
  String elementType,
  Long elementId,
  ImmutableList<String> layers,
  Optional<String> description,
  Optional<String> address,
  Boolean link,
  Boolean image
) {
}
