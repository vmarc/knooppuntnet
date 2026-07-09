package kpn.server.repository

import kpn.core.doc.Storable

case class NetworkFactElementIds(
  networkId: Long,
  networkName: String,
  elementIds: Seq[Long] = Seq.empty
) extends Storable
