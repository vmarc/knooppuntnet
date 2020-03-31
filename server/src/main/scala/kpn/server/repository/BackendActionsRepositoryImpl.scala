package kpn.server.repository

import kpn.api.common.status.NameValue
import kpn.api.common.status.PeriodParameters
import kpn.core.action.AnalysisAction
import kpn.core.action.AnalysisActionDoc
import kpn.core.action.ReplicationAction
import kpn.core.action.ReplicationActionDoc
import kpn.core.action.SystemStatus
import kpn.core.action.SystemStatusDoc
import kpn.core.action.UpdateAction
import kpn.core.action.UpdateActionDoc
import kpn.core.database.Database
import kpn.core.database.views.action.BackendActionView
import kpn.core.db.couch.Couch
import org.springframework.stereotype.Component

object BackendActionsRepositoryImpl {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-frontend", "backend-actions") { database =>
      val repo = new BackendActionsRepositoryImpl(database)

      {
        val used = repo.lastKnownValue("backend-disk-space-used")
        val available = repo.lastKnownValue("backend-disk-space-available")
        val overpass = repo.lastKnownValue("backend-disk-space-overpass")
        report("backend", used, available, overpass)
      }

      {
        val used = repo.lastKnownValue("db-disk-space-used")
        val available = repo.lastKnownValue("db-disk-space-available")
        report("database", used, available, -1)
      }

      {
        val used = repo.lastKnownValue("frontend-disk-space-used")
        val available = repo.lastKnownValue("frontend-disk-space-available")
        report("frontend", used, available, -1)
      }

    }
  }

  private def report(title: String, used: Long, available: Long, overpass: Long): Unit ={
    println(title)
    println("  used=" + Math.round(used.toDouble / 1024 / 1024))
    println("  available=" + Math.round(available.toDouble / 1024 / 1024))
    println("  overpass=" + Math.round(overpass.toDouble / 1024 / 1024))
    println("  total=" + Math.round((available.toDouble + used.toDouble) / 1024 / 1024))
  }
}

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

  override def saveSystemStatus(systemStatus: SystemStatus): Unit = {
    val id = s"system-status-${systemStatus.timestamp.toId}"
    backendActionsDatabase.save(SystemStatusDoc(id, systemStatus))
  }

  override def query(parameters: PeriodParameters, action: String, average: Boolean, stale: Boolean = true): Seq[NameValue] = {
    BackendActionView.query(backendActionsDatabase, parameters, action, average, stale)
  }

  override def lastKnownValue(action: String, stale: Boolean = true): Long = {
    BackendActionView.queryLastKnown(backendActionsDatabase, action, stale)
  }

}
