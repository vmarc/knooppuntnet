package kpn.api.common.data;

import kpn.api.custom.Tag;
import kpn.api.custom.Tags;

import java.util.List;
import java.util.Optional;
import com.google.common.collect.ImmutableList;

public interface Tagable {

  List<Tag> tags();

  default Optional<String> tagValue(String key) {
    return Tags.get(tags(), key);
  }

  default ImmutableList<String> tagValues(String key) {
    return Tags.values(tags(), key);
  }

  default boolean hasTag(String key, String... allowedValues) {
    return Tags.has(tags(), key, allowedValues);
  }
}
