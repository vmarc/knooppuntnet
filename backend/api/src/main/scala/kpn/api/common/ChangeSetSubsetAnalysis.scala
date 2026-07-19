package kpn.api.common

import kpn.api.custom.Subset

case class ChangeSetSubsetAnalysis(
  subset: Subset,
  happy: Boolean = false,
  investigate: Boolean = false
)
