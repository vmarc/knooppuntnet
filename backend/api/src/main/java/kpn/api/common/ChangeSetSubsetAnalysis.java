package kpn.api.common;

import kpn.api.custom.Subset;

public record ChangeSetSubsetAnalysis(
  Subset subset,
  Boolean happy,
  Boolean investigate
) {
}

/*
package kpn.api.common

import kpn.api.custom.Subset

case class ChangeSetSubsetAnalysis(
  subset: Subset,
  happy: Boolean = false,
  investigate: Boolean = false
)

*/
