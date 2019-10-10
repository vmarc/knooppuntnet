package kpn.shared

case class ChangeSetSubsetAnalysis(
  subset: Subset,
  happy: Boolean = false,
  investigate: Boolean = false
)
