package kpn.api.common;

import kpn.api.common.LocationChangesInfo;
import kpn.api.common.changes.details.ChangeKey;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record LocationChangeSetInfo(
  Long rowIndex,
  ChangeKey key,
  Optional<String> comment,
  Boolean happy,
  Boolean investigate,
  ImmutableList<LocationChangesInfo> locationChanges
) {
}

/*
package kpn.api.common

import kpn.api.common.changes.details.ChangeKey

case class LocationChangeSetInfo(
  rowIndex: Long,
  key: ChangeKey,
  comment: Option[String],
  happy: Boolean,
  investigate: Boolean,
  locationChanges: Seq[LocationChangesInfo]
)

*/
