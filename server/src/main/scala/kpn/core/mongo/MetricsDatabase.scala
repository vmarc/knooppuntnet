package kpn.core.mongo

import kpn.core.metrics.AnalysisActionDoc
import kpn.core.metrics.ApiActionDoc
import kpn.core.metrics.LogActionDoc
import kpn.core.metrics.ReplicationActionDoc
import kpn.core.metrics.SystemStatusDoc
import kpn.core.metrics.UpdateActionDoc

trait MetricsDatabase {

  def api: DatabaseCollection[ApiActionDoc]

  def log: DatabaseCollection[LogActionDoc]

  def replication: DatabaseCollection[ReplicationActionDoc]

  def update: DatabaseCollection[UpdateActionDoc]

  def analysis: DatabaseCollection[AnalysisActionDoc]

  def system: DatabaseCollection[SystemStatusDoc]

}
