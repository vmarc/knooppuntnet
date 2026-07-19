package kpn.api.common;

import kpn.api.common.LocationChangesTree;
import kpn.api.common.changes.details.ChangeKey;
import kpn.api.custom.Timestamp;

import com.google.common.collect.ImmutableList;

public record LocationChangeSetSummary(
  String _id,
  ChangeKey key,
  Timestamp timestampFrom,
  Timestamp timestampUntil,
  ImmutableList<LocationChangesTree> trees,
  Boolean happy,
  Boolean investigate,
  Boolean impact
) {
}

/*
package kpn.api.common

import kpn.api.common.changes.details.ChangeKey
import kpn.api.custom.Timestamp
import kpn.core.doc.WithStringId

case class LocationChangeSetSummary(
  _id: String,
  key: ChangeKey,
  timestampFrom: Timestamp,
  timestampUntil: Timestamp,
  trees: Seq[LocationChangesTree],
  happy: Boolean,
  investigate: Boolean,
  impact: Boolean
) extends WithStringId {
}

*/
