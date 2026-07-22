package kpn.core.metrics

import kpn.api.id.Storable

case class AnalysisAction(
  minuteDiff: MinuteDiffInfo
) extends Storable
