package kpn.server.repository

import kpn.api.common.status.NameValue
import kpn.api.common.status.PeriodParameters
import kpn.api.custom.Timestamp
import kpn.core.action.AnalysisAction
import kpn.core.action.ReplicationAction
import kpn.core.action.SystemStatus
import kpn.core.action.UpdateAction

trait BackendActionsRepository {

  def saveReplicationAction(replicationAction: ReplicationAction): Unit

  def saveUpdateAction(updateAction: UpdateAction): Unit

  def saveAnalysisAction(analysisAction: AnalysisAction): Unit

  def saveSystemStatus(systemStatus: SystemStatus): Unit

  def dayAction(day: Timestamp, action: String): Seq[NameValue]

  def dayActionAverage(day: Timestamp, action: String): Seq[NameValue]

  def query(parameters: PeriodParameters, action: String, average: Boolean = false, stale: Boolean = true): Seq[NameValue]

}
