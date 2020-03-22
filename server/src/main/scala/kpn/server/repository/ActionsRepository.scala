package kpn.server.repository

import kpn.core.action.AnalysisAction
import kpn.core.action.ReplicationAction
import kpn.core.action.UpdateAction

trait ActionsRepository {

  def saveReplicationAction(replicationAction: ReplicationAction): Unit

  def saveUpdateAction(updateAction: UpdateAction): Unit

  def saveAnalysisAction(analysisAction: AnalysisAction): Unit

}
