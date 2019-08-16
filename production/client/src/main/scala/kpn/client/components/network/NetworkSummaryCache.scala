// Migrated to Angular: network-cache.service.ts
package kpn.client.components.network

import kpn.shared.network.NetworkSummary

object NetworkSummaryCache {

  private val summaries = scala.collection.mutable.Map[Long, NetworkSummary]()

  def get(networkId: Long): Option[NetworkSummary] = {
    summaries.get(networkId)
  }

  def put(networkId: Long, networkSummary: NetworkSummary): Unit = {
    summaries.put(networkId, networkSummary)
  }
}
