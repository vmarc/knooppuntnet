package kpn.server.monitor.repository

import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.monitor.domain.MonitorRelation

class MonitorRelationRepositoryImpl(database: Database) extends MonitorRelationRepository {

  private val log = Log(classOf[MonitorRelationRepositoryImpl])

  override def save(monitorRelation: MonitorRelation): Unit = {
    database.monitorRelations.save(monitorRelation, log)
  }
}
