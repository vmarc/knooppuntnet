package kpn.server.repository

import kpn.api.custom.Timestamp
import kpn.core.analysis.Network

class AnalysisRepositoryNoop extends AnalysisRepository {

  override def saveNetwork(network: Network): Unit = {}

  override def lastUpdated(): Option[Timestamp] = None

  override def saveLastUpdated(timestamp: Timestamp): Unit = {}

}
