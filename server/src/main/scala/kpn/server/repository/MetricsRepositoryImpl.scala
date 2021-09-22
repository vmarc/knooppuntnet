package kpn.server.repository

import kpn.api.common.status.NameValue
import kpn.api.common.status.PeriodParameters
import kpn.core.action.AnalysisAction
import kpn.core.action.ApiAction
import kpn.core.action.LogAction
import kpn.core.action.ReplicationAction
import kpn.core.action.SystemStatus
import kpn.core.action.UpdateAction
import kpn.core.database.doc.AnalysisActionDoc
import kpn.core.database.doc.ApiActionDoc
import kpn.core.database.doc.LogActionDoc
import kpn.core.database.doc.ReplicationActionDoc
import kpn.core.database.doc.SystemStatusDoc
import kpn.core.database.doc.UpdateActionDoc
import kpn.core.mongo.MetricsDatabase
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
}
