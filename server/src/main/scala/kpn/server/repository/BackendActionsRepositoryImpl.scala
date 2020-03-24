package kpn.server.repository

import kpn.api.common.status.NameValue
import kpn.api.common.status.PeriodParameters
import kpn.api.custom.Timestamp
import kpn.core.action.AnalysisAction
import kpn.core.action.AnalysisActionDoc
import kpn.core.action.ReplicationAction
import kpn.core.action.ReplicationActionDoc
import kpn.core.action.UpdateAction
import kpn.core.action.UpdateActionDoc
import kpn.core.database.Database
import kpn.core.database.views.action.BackendActionView
import org.springframework.stereotype.Component

@Component
class BackendActionsRepositoryImpl(backendActionsDatabase: Database) extends BackendActionsRepository {

  override def saveReplicationAction(replicationAction: ReplicationAction): Unit = {
    val id = s"replication-${replicationAction.minuteDiff.id}"
    backendActionsDatabase.save(ReplicationActionDoc(id, replicationAction))
  }

  override def saveUpdateAction(updateAction: UpdateAction): Unit = {
    val id = s"update-${updateAction.minuteDiff.id}"
    backendActionsDatabase.save(UpdateActionDoc(id, updateAction))
  }

  override def saveAnalysisAction(analysisAction: AnalysisAction): Unit = {
    val id = s"analysis-${analysisAction.minuteDiff.id}"
    backendActionsDatabase.save(AnalysisActionDoc(id, analysisAction))
  }

  override def dayAction(day: Timestamp, action: String): Seq[NameValue] = {
    BackendActionView.queryDay(backendActionsDatabase, day, action, stale = false)
  }

  override def dayActionAverage(day: Timestamp, action: String): Seq[NameValue] = {
    BackendActionView.queryDayAverage(backendActionsDatabase, day, action, stale = false)
  }

  override def query(parameters: PeriodParameters, action: String, average: Boolean, stale: Boolean = true): Seq[NameValue] = {
    BackendActionView.query(backendActionsDatabase, parameters, action, average, stale)
  }

}
