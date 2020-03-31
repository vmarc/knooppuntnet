package kpn.core.database.views.action

import kpn.api.common.status.NameValue
import kpn.api.common.status.PeriodParameters
import kpn.core.database.Database
import kpn.core.database.views.common.View

object FrontendActionView extends View {

  override def reduce: Option[String] = Some("_sum")

  def query(database: Database, parameters: PeriodParameters, action: String, average: Boolean, stale: Boolean = true): Seq[NameValue] = {
    new ActionQuery(database, FrontendActionDesign, FrontendActionView, parameters, action, average, stale).query()
  }

}
