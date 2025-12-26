package kpn.database.base

import com.mongodb.client.MongoDatabase
import kpn.core.metrics.AnalysisActionDoc
import kpn.core.metrics.ApiActionDoc
import kpn.core.metrics.LogActionDoc
import kpn.core.metrics.ReplicationActionDoc
import kpn.core.metrics.SystemStatusDoc
import kpn.core.metrics.UpdateActionDoc

class MetricsDatabase(val database: MongoDatabase) {

  def api: DatabaseCollection[ApiActionDoc] = {
    new DatabaseCollection(database.getCollection("api", classOf[ApiActionDoc]))
  }

  def log: DatabaseCollection[LogActionDoc] = {
    new DatabaseCollection(database.getCollection("log", classOf[LogActionDoc]))
  }

  def replication: DatabaseCollection[ReplicationActionDoc] = {
    new DatabaseCollection(database.getCollection("replication", classOf[ReplicationActionDoc]))
  }

  def update: DatabaseCollection[UpdateActionDoc] = {
    new DatabaseCollection(database.getCollection("update", classOf[UpdateActionDoc]))
  }

  def analysis: DatabaseCollection[AnalysisActionDoc] = {
    new DatabaseCollection(database.getCollection("analysis", classOf[AnalysisActionDoc]))
  }

  def system: DatabaseCollection[SystemStatusDoc] = {
    new DatabaseCollection(database.getCollection("system", classOf[SystemStatusDoc]))
  }
}
