package kpn.core.metrics

import kpn.core.doc.Storable

case class AnalysisAction(
  minuteDiff: MinuteDiffInfo
) extends Storable
