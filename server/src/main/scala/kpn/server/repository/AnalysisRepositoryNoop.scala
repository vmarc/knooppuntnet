package kpn.server.repository

import kpn.api.custom.Timestamp

class AnalysisRepositoryNoop extends AnalysisRepository {

  override def lastUpdated(): Option[Timestamp] = None

  override def saveLastUpdated(timestamp: Timestamp): Unit = {}

}
