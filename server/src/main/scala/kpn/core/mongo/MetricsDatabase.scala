package kpn.core.mongo

import kpn.core.database.doc.AnalysisActionDoc
import kpn.core.database.doc.ApiActionDoc
import kpn.core.database.doc.LogActionDoc
import kpn.core.database.doc.ReplicationActionDoc
import kpn.core.database.doc.SystemStatusDoc
import kpn.core.database.doc.UpdateActionDoc

trait MetricsDatabase {

  def api: DatabaseCollection[ApiActionDoc]

  def log: DatabaseCollection[LogActionDoc]

  def replication: DatabaseCollection[ReplicationActionDoc]

  def update: DatabaseCollection[UpdateActionDoc]

  def analysis: DatabaseCollection[AnalysisActionDoc]

  def system: DatabaseCollection[SystemStatusDoc]

}
