package kpn.server.repository

import kpn.api.custom.Timestamp
import kpn.core.mongo.Database
import kpn.server.analyzer.engine.analysis.AnalysisStatus
import org.mongodb.scala.model.Filters.equal
import org.springframework.stereotype.Component

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

@Component
class AnalysisRepositoryImpl(database: Database) extends AnalysisRepository {

  override def lastUpdated(): Option[Timestamp] = {
    val future = database.status.native.find[AnalysisStatus](equal("_id", AnalysisStatus.id)).headOption
    Await.result(future, Duration(30, TimeUnit.SECONDS)).map(_.timestamp)
  }

  override def saveLastUpdated(timestamp: Timestamp): Unit = {
    database.status.save(AnalysisStatus(timestamp))
  }
}
