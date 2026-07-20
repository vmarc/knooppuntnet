package kpn.api.common;

import kpn.api.common.ChangeSetSummaryInfo;
import kpn.api.common.changes.filter.ChangesFilterOption;

import com.google.common.collect.ImmutableList;

public record ChangesPage(
  ImmutableList<ChangesFilterOption> filterOptions,
  ImmutableList<ChangeSetSummaryInfo> changes,
  Long changeCount
) {
}

/* TODO migrate
package kpn.api.common

import kpn.api.common.changes.filter.ChangesFilterOption

case class ChangesPage(
  filterOptions: Seq[ChangesFilterOption] = Seq.empty,
  changes: Seq[ChangeSetSummaryInfo] = Seq.empty,
  changeCount: Long = 0
)

*/
