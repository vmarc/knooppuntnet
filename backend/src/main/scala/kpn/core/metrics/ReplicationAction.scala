package kpn.core.metrics

import kpn.api.id.Storable

case class ReplicationAction(
  minuteDiff: MinuteDiffInfo,
  fileSize: Long,
  elementCount: Long,
  changeSetCount: Long
) extends Storable
