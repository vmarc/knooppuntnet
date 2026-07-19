package kpn.api.common.subset;

import kpn.api.common.FactCount;
import kpn.api.common.subset.SubsetInfo;

import com.google.common.collect.ImmutableList;

public record SubsetFactsPage(
  SubsetInfo subsetInfo,
  ImmutableList<FactCount> factCounts
) {
}

/*
package kpn.api.common.subset

import kpn.api.common.FactCount

case class SubsetFactsPage(
  subsetInfo: SubsetInfo,
  factCounts: Seq[FactCount]
) {

  def hasFacts: Boolean = factCounts.exists(_.count > 0)

}

*/
