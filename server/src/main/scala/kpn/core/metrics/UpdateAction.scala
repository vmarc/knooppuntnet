package kpn.core.metrics

import kpn.core.doc.Storable

case class UpdateAction(
  minuteDiff: MinuteDiffInfo
) extends Storable
