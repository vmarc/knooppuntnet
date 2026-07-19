package kpn.api.common.data.raw;

import kpn.api.custom.Tag;
import kpn.api.custom.Timestamp;

import com.google.common.collect.ImmutableList;

public record Raw(
  Long version,
  Long changeSetId,
  Timestamp timestamp,
  ImmutableList<Tag> tags
) {
}

/*
package kpn.api.common.data.raw

import kpn.api.common.data.MetaData
import kpn.api.common.data.Tagable
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp
import kpn.core.doc.Storable

case class Raw(
  version: Long,
  changeSetId: Long,
  timestamp: Timestamp,
  tags: Seq[Tag]
) extends Storable with Tagable {
  def meta: MetaData = {
    MetaData(
      version,
      timestamp,
      changeSetId
    )
  }
}

*/
