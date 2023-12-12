package kpn.server.monitor.repository

import kpn.server.monitor.domain.MonitorRelation

trait MonitorRelationRepository {

  def save(monitorRelation: MonitorRelation): Unit
}
