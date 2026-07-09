package kpn.server.repository

import kpn.core.doc.Storable

case class NetworkElement(
  networkId: Long,
  networkName: String,
  elementId: Long
) extends Storable
