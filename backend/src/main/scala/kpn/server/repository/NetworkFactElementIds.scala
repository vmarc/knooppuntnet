package kpn.server.repository

import kpn.api.id.Storable

case class NetworkFactElementIds(
  networkId: Long,
  networkName: String,
  elementIds: Seq[Long] = Seq.empty
) extends Storable
