package kpn.server.repository

import kpn.core.action.AnalysisAction
import kpn.core.action.AnalysisActionDoc
import kpn.core.action.ReplicationAction
import kpn.core.action.ReplicationActionDoc
import kpn.core.action.UpdateAction
import kpn.core.action.UpdateActionDoc
import kpn.core.database.Database
import org.springframework.stereotype.Component

@Component
class ActionsRepositoryImpl(actionsDatabase: Database) extends ActionsRepository {

  override def saveReplicationAction(replicationAction: ReplicationAction): Unit = {
    val id = s"replication-${replicationAction.minuteDiff.id}"
    actionsDatabase.save(ReplicationActionDoc(id, replicationAction))
  }

  override def saveUpdateAction(updateAction: UpdateAction): Unit = {
    val id = s"update-${updateAction.minuteDiff.id}"
    actionsDatabase.save(UpdateActionDoc(id, updateAction))
  }

  override def saveAnalysisAction(analysisAction: AnalysisAction): Unit = {
    val id = s"analysis-${analysisAction.minuteDiff.id}"
    actionsDatabase.save(AnalysisActionDoc(id, analysisAction))
  }

}
