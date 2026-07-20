package kpn.api.common.data;

import kpn.api.common.data.raw.Raw;
import kpn.api.custom.Tag;
import kpn.api.custom.Timestamp;

import com.google.common.collect.ImmutableList;

public interface Element {

  Long id();

  Long version();

  Timestamp timestamp();

  Long changeSetId();

  ImmutableList<Tag> tags();

  default boolean isNode() {
    return false;
  }

  default boolean isWay() {
    return false;
  }

  default boolean isRelation() {
    return false;
  }

  default MetaData toMeta() {
    return new MetaData(version(), timestamp(), changeSetId());
  }

  default Raw raw() {
    return new Raw(
      version(),
      changeSetId(),
      timestamp(),
      tags()
    );
  }
}
