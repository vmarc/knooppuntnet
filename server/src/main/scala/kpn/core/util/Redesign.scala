package kpn.core.util

import kpn.api.custom.NetworkType

object Redesign {
  val enableNewFactTests = false
  val enableDebugPrinting = true
  val enablePendingTests = false
  val tileGenerationNetworkTypes: Seq[NetworkType] = NetworkType.all // Seq(NetworkType.hiking) // Seq.empty  NetworkType.all
}
