package kpn.server.repository

import kpn.api.common.status.NameValue
import kpn.api.common.status.PeriodParameters
import kpn.core.metrics.AnalysisAction
import kpn.core.metrics.AnalysisActionDoc
import kpn.core.metrics.ApiAction
import kpn.core.metrics.ApiActionDoc
import kpn.core.metrics.LogAction
import kpn.core.metrics.LogActionDoc
import kpn.core.metrics.ReplicationAction
import kpn.core.metrics.ReplicationActionDoc
import kpn.core.metrics.SystemStatus
import kpn.core.metrics.SystemStatusDoc
import kpn.core.metrics.UpdateAction
import kpn.core.metrics.UpdateActionDoc
import kpn.database.base.MetricsDatabase
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.gte
import org.mongodb.scala.model.Filters.lt
import org.springframework.stereotype.Component

import scala.util.Random

@Component
class MetricsRepositoryImpl(
  metricsDatabase: MetricsDatabase
) extends MetricsRepository {

  private val random = new Random()

  override def saveApiAction(apiAction: ApiAction): Unit = {
    val id = s"api-${apiAction.timestamp.toId}-${random.nextInt(10000)}"
    metricsDatabase.api.save(ApiActionDoc(id, apiAction))
  }

  override def saveLogAction(logAction: LogAction): Unit = {
    val id = s"log-${logAction.logfile}-${logAction.timestamp.toId}}"
    metricsDatabase.log.save(LogActionDoc(id, logAction))
  }

  override def saveReplicationAction(replicationAction: ReplicationAction): Unit = {
    val id = s"replication-${replicationAction.minuteDiff.id}"
    metricsDatabase.replication.save(ReplicationActionDoc(id, replicationAction))
  }

  override def saveUpdateAction(updateAction: UpdateAction): Unit = {
    val id = s"update-${updateAction.minuteDiff.id}"
    metricsDatabase.update.save(UpdateActionDoc(id, updateAction))
  }

  override def saveAnalysisAction(analysisAction: AnalysisAction): Unit = {
    val id = s"analysis-${analysisAction.minuteDiff.id}"
    metricsDatabase.analysis.save(AnalysisActionDoc(id, analysisAction))
  }

  override def saveSystemStatus(systemStatus: SystemStatus): Unit = {
    val id = s"system-status-${systemStatus.timestamp.toId}"
    metricsDatabase.system.save(SystemStatusDoc(id, systemStatus))
  }

  override def query(parameters: PeriodParameters, action: String, average: Boolean): Seq[NameValue] = {
    // FrontendMetricsView.query(frontendActionsDatabase, parameters, action, average)
    // BackendMetricsView.query(backendActionsDatabase, parameters, action, average)
    Seq.empty
  }

  override def lastKnownValue(action: String): Long = {
    // BackendMetricsView.queryLastKnown(backendActionsDatabase, action)
    0L
  }

  override def apiActionsDay(year: Int, month: Int, day: Int): Seq[ApiAction] = {
    val from = f"api-$year-$month%02d-$day%02d"
    val until = f"api-$year-$month%02d-${day + 1}%02d"
    val pipeline = Seq(
      filter(
        and(
          gte("_id", from),
          lt("_id", until),
        )
      )
    )
    metricsDatabase.api.aggregate[ApiActionDoc](pipeline).map(_.api)
  }
}
