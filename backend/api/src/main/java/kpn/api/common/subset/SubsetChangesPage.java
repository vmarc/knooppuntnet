package kpn.api.common.subset;

import kpn.api.common.ChangeSetSummaryInfo;
import kpn.api.common.changes.filter.ChangesFilterOption;
import kpn.api.common.subset.SubsetInfo;

import com.google.common.collect.ImmutableList;

public record SubsetChangesPage(
  SubsetInfo subsetInfo,
  ImmutableList<ChangesFilterOption> filterOptions,
  ImmutableList<ChangeSetSummaryInfo> changes,
  Long changeCount
) {
}
