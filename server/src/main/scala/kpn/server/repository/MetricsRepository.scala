package kpn.server.repository

import kpn.api.common.status.NameValue
import kpn.api.common.status.PeriodParameters
import kpn.core.action.AnalysisAction
import kpn.core.action.ApiAction
import kpn.core.action.LogAction
import kpn.core.action.ReplicationAction
import kpn.core.action.SystemStatus
import kpn.core.action.UpdateAction

trait MetricsRepository {

  def saveApiAction(action: ApiAction): Unit

  def saveLogAction(action: LogAction): Unit

  def saveReplicationAction(replicationAction: ReplicationAction): Unit

  def saveUpdateAction(updateAction: UpdateAction): Unit

  def saveAnalysisAction(analysisAction: AnalysisAction): Unit

  def saveSystemStatus(systemStatus: SystemStatus): Unit

  def query(parameters: PeriodParameters, action: String, average: Boolean = false): Seq[NameValue]

  def lastKnownValue(action: String): Long

}