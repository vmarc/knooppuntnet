package kpn.database.base

import com.mongodb.client.MongoDatabase
import kpn.core.metrics.AnalysisActionDoc
import kpn.core.metrics.ApiActionDoc
import kpn.core.metrics.LogActionDoc
import kpn.core.metrics.ReplicationActionDoc
import kpn.core.metrics.SystemStatusDoc
import kpn.core.metrics.UpdateActionDoc

class MetricsDatabaseImpl(val database: MongoDatabase) extends MetricsDatabase {

  override def api: DatabaseCollection[ApiActionDoc] = {
    new DatabaseCollectionImpl(database.getCollection("api", classOf[ApiActionDoc]))
  }

  override def log: DatabaseCollection[LogActionDoc] = {
    new DatabaseCollectionImpl(database.getCollection("log", classOf[LogActionDoc]))
  }

  override def replication: DatabaseCollection[ReplicationActionDoc] = {
    new DatabaseCollectionImpl(database.getCollection("replication", classOf[ReplicationActionDoc]))
  }

  override def update: DatabaseCollection[UpdateActionDoc] = {
    new DatabaseCollectionImpl(database.getCollection("update", classOf[UpdateActionDoc]))
  }

  override def analysis: DatabaseCollection[AnalysisActionDoc] = {
    new DatabaseCollectionImpl(database.getCollection("analysis", classOf[AnalysisActionDoc]))
  }

  override def system: DatabaseCollection[SystemStatusDoc] = {
    new DatabaseCollectionImpl(database.getCollection("system", classOf[SystemStatusDoc]))
  }
}
