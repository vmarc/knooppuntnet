package kpn.core.database.views.action

import kpn.api.common.status.NameValue
import kpn.api.common.status.PeriodParameters
import kpn.core.database.Database
import kpn.core.database.views.common.View

object BackendActionView extends View {

  override def reduce: Option[String] = sumAndCount

  def query(database: Database, parameters: PeriodParameters, action: String, average: Boolean, stale: Boolean = true): Seq[NameValue] = {
    new ActionQuery(database, BackendActionDesign, BackendActionView, parameters, action, average, stale).query()
  }

  def queryLastKnown(database: Database, action: String, stale: Boolean = true): Long = {
    new LastKnownActionQuery(database, BackendActionDesign, BackendActionView, action, stale).value()
  }

}
