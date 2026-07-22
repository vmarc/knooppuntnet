package kpn.api.common.data.raw;

import kpn.api.common.data.MetaData;
import kpn.api.common.data.Tagable;
import kpn.api.custom.Tag;
import kpn.api.custom.Timestamp;
import kpn.core.doc.Storable;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record Raw(
  @NonNull Long version,
  @NonNull Long changeSetId,
  @NonNull Timestamp timestamp,
  @NonNull ImmutableList<Tag> tags
) implements Storable, Tagable {

  public MetaData meta() {
    return new MetaData(
      version,
      timestamp,
      changeSetId
    );
  }
}
