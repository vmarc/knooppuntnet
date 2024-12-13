package kpn.core.util

import kpn.api.common.NetworkType

object Redesign {
  val enableNewFactTests = false
  val enableDebugPrinting = true
  val enablePendingTests = false
  val tileGenerationNetworkTypes: Seq[NetworkType] = Seq(NetworkType.hiking) // NetworkType.all // Seq.empty
}
