package kpn.api.common.changes.filter;

import com.google.common.collect.ImmutableList;

public record ChangesFilterPeriod(
  Long name,
  Long totalCount,
  Long impactedCount,
  Boolean current,
  Boolean selected,
  ImmutableList<ChangesFilterPeriod> periods
) {
}
