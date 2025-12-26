package kpn.server.repository

import kpn.api.custom.Timestamp
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.server.analyzer.engine.analysis.AnalysisStatus
import org.springframework.stereotype.Component

import scala.jdk.CollectionConverters.IterableHasAsScala

@Component
class AnalysisRepository(database: Database) {

  /*
    Returns the time of the most recent minute diff that was processed by the analyzer. This provides
    an indication of how up-to-date the information in the analysis database is.
   */
  def lastUpdated(): Option[Timestamp] = {
    database.status.native.find(equal("_id", AnalysisStatus.id), classOf[AnalysisStatus]).asScala.headOption.map(_.timestamp)
  }

  def saveLastUpdated(timestamp: Timestamp): Unit = {
    database.status.save(AnalysisStatus(timestamp))
  }
}
