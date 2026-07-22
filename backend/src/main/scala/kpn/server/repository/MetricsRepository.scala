package kpn.server.repository

import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.gte
import com.mongodb.client.model.Filters.lt
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
import kpn.database.base.MongoAggregates.filter
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

import scala.util.Random

@Component
@Profile(Array("web"))
class MetricsRepository(metricsDatabase: MetricsDatabase) {

  private val random = new Random()

  def saveApiAction(apiAction: ApiAction): Unit = {
    val id = s"api-${apiAction.timestamp.toId}-${random.nextInt(10000)}"
    metricsDatabase.api.save(ApiActionDoc(id, apiAction))
  }

  def saveLogAction(logAction: LogAction): Unit = {
    val id = s"log-${logAction.logfile}-${logAction.timestamp.toId}}"
    metricsDatabase.log.save(LogActionDoc(id, logAction))
  }

  def saveReplicationAction(replicationAction: ReplicationAction): Unit = {
    val id = s"replication-${replicationAction.minuteDiff.id}"
    metricsDatabase.replication.save(ReplicationActionDoc(id, replicationAction))
  }

  def saveUpdateAction(updateAction: UpdateAction): Unit = {
    val id = s"update-${updateAction.minuteDiff.id}"
    metricsDatabase.update.save(UpdateActionDoc(id, updateAction))
  }

  def saveAnalysisAction(analysisAction: AnalysisAction): Unit = {
    val id = s"analysis-${analysisAction.minuteDiff.id}"
    metricsDatabase.analysis.save(AnalysisActionDoc(id, analysisAction))
  }

  def saveSystemStatus(systemStatus: SystemStatus): Unit = {
    val id = s"system-status-${systemStatus.timestamp.toId}"
    metricsDatabase.system.save(SystemStatusDoc(id, systemStatus))
  }

  def query(parameters: PeriodParameters, action: String, average: Boolean = false): Seq[NameValue] = {
    // FrontendMetricsView.query(frontendActionsDatabase, parameters, action, average)
    // BackendMetricsView.query(backendActionsDatabase, parameters, action, average)
    Seq.empty
  }

  def lastKnownValue(action: String): Long = {
    // BackendMetricsView.queryLastKnown(backendActionsDatabase, action)
    0L
  }

  def apiActionsDay(year: Int, month: Int, day: Int): Seq[ApiAction] = {
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
    metricsDatabase.api.aggregate(pipeline, classOf[ApiActionDoc]).map(_.api)
  }
}
