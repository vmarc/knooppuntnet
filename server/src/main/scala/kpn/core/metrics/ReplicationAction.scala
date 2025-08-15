package kpn.core.metrics

import kpn.core.doc.Storable

case class ReplicationAction(
  minuteDiff: MinuteDiffInfo,
  fileSize: Long,
  elementCount: Long,
  changeSetCount: Long
) extends Storable
