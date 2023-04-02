package kpn.server.repository

import kpn.api.common.status.NameValue
import kpn.api.common.status.PeriodParameters
import kpn.core.metrics.AnalysisAction
import kpn.core.metrics.ApiAction
import kpn.core.metrics.LogAction
import kpn.core.metrics.ReplicationAction
import kpn.core.metrics.SystemStatus
import kpn.core.metrics.UpdateAction

trait MetricsRepository {

  def saveApiAction(action: ApiAction): Unit

  def saveLogAction(action: LogAction): Unit

  def saveReplicationAction(replicationAction: ReplicationAction): Unit

  def saveUpdateAction(updateAction: UpdateAction): Unit

  def saveAnalysisAction(analysisAction: AnalysisAction): Unit

  def saveSystemStatus(systemStatus: SystemStatus): Unit

  def query(parameters: PeriodParameters, action: String, average: Boolean = false): Seq[NameValue]

  def lastKnownValue(action: String): Long

  def apiActionsDay(year: Int, month: Int, day: Int): Seq[ApiAction]
}
