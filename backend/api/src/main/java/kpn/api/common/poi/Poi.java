package kpn.api.common.poi;

import kpn.api.common.LatLon;
import kpn.api.common.data.Tagable;
import kpn.api.common.location.Location;
import kpn.api.custom.Tag;
import kpn.core.doc.WithStringId;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record Poi(
  String _id,
  String elementType,
  Long elementId,
  String latitude,
  String longitude,
  ImmutableList<String> layers,
  ImmutableList<Tag> tags,
  Location location,
  ImmutableList<String> tiles,
  Optional<String> description,
  Optional<String> address,
  Boolean link,
  Boolean image
) implements LatLon, WithStringId, Tagable {
}
