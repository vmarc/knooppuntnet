package kpn.server.repository

import kpn.api.id.Storable

case class NetworkElement(
  networkId: Long,
  networkName: String,
  elementId: Long
) extends Storable
