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

/*
package kpn.api.common.changes.filter

case class ChangesFilterPeriod(
  name: Long,
  totalCount: Long,
  impactedCount: Long,
  current: Boolean = false,
  selected: Boolean = false,
  periods: Seq[ChangesFilterPeriod] = Seq.empty
)

*/
