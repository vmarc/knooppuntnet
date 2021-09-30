package kpn.database.base

import kpn.core.metrics.AnalysisActionDoc
import kpn.core.metrics.ApiActionDoc
import kpn.core.metrics.LogActionDoc
import kpn.core.metrics.ReplicationActionDoc
import kpn.core.metrics.SystemStatusDoc
import kpn.core.metrics.UpdateActionDoc
import org.mongodb.scala.MongoDatabase

class MetricsDatabaseImpl(val database: MongoDatabase) extends MetricsDatabase {

  override def api: DatabaseCollection[ApiActionDoc] = {
    new DatabaseCollectionImpl(database.getCollection[ApiActionDoc]("api"))
  }

  override def log: DatabaseCollection[LogActionDoc] = {
    new DatabaseCollectionImpl(database.getCollection[LogActionDoc]("log"))
  }

  override def replication: DatabaseCollection[ReplicationActionDoc] = {
    new DatabaseCollectionImpl(database.getCollection[ReplicationActionDoc]("replication"))
  }

  override def update: DatabaseCollection[UpdateActionDoc] = {
    new DatabaseCollectionImpl(database.getCollection[UpdateActionDoc]("update"))
  }

  override def analysis: DatabaseCollection[AnalysisActionDoc] = {
    new DatabaseCollectionImpl(database.getCollection[AnalysisActionDoc]("analysis"))
  }

  override def system: DatabaseCollection[SystemStatusDoc] = {
    new DatabaseCollectionImpl(database.getCollection[SystemStatusDoc]("system"))
  }
}
