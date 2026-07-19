package kpn.api.common;

import kpn.api.common.ChangeSetSummaryLocationInfo;
import kpn.api.common.ChangeSetSummaryNetworkInfo;
import kpn.api.common.changes.details.ChangeKey;
import kpn.api.custom.Subset;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record ChangeSetSummaryInfo(
  Long rowIndex,
  ChangeKey key,
  Optional<String> comment,
  ImmutableList<Subset> subsets,
  Optional<ChangeSetSummaryNetworkInfo> network,
  Optional<ChangeSetSummaryLocationInfo> location,
  Boolean happy,
  Boolean investigate,
  Boolean impact
) {
}

/*
package kpn.api.common

import kpn.api.common.changes.details.ChangeKey
import kpn.api.custom.Subset

case class ChangeSetSummaryInfo(
  rowIndex: Long,
  key: ChangeKey,
  comment: Option[String],
  subsets: Seq[Subset],
  network: Option[ChangeSetSummaryNetworkInfo],
  location: Option[ChangeSetSummaryLocationInfo],
  happy: Boolean,
  investigate: Boolean,
  impact: Boolean,
)

*/
