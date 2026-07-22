package kpn.core.metrics

import kpn.api.id.Storable

case class UpdateAction(
  minuteDiff: MinuteDiffInfo
) extends Storable
