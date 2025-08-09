package kpn.server.repository

import kpn.api.custom.Timestamp
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.server.analyzer.engine.analysis.AnalysisStatus
import org.springframework.stereotype.Component

import scala.jdk.CollectionConverters.IterableHasAsScala

@Component
class AnalysisRepositoryImpl(database: Database) extends AnalysisRepository {

  override def lastUpdated(): Option[Timestamp] = {
    database.status.native.find(equal("_id", AnalysisStatus.id), classOf[AnalysisStatus]).asScala.headOption.map(_.timestamp)
  }

  override def saveLastUpdated(timestamp: Timestamp): Unit = {
    database.status.save(AnalysisStatus(timestamp))
  }
}
