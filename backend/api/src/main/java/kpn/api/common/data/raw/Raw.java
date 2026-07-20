package kpn.api.common.data.raw;

import kpn.api.common.data.MetaData;
import kpn.api.common.data.Tagable;
import kpn.api.custom.Tag;
import kpn.api.custom.Timestamp;
import kpn.core.doc.Storable;

import com.google.common.collect.ImmutableList;

public record Raw(
  Long version,
  Long changeSetId,
  Timestamp timestamp,
  ImmutableList<Tag> tags
) implements Storable, Tagable {

  public MetaData meta() {
    return new MetaData(
      version,
      timestamp,
      changeSetId
    );
  }
}
