package kpn.core.database.views.metrics

import kpn.api.common.status.NameValue
import kpn.api.common.status.PeriodParameters
import kpn.core.database.Database
import kpn.core.database.views.common.View

object BackendMetricsView extends View {

  override def reduce: Option[String] = sumAndCount

  def query(database: Database, parameters: PeriodParameters, action: String, average: Boolean): Seq[NameValue] = {
    new MetricsQuery(database, BackendMetricsDesign, BackendMetricsView, parameters, action, average).query()
  }

  def queryLastKnown(database: Database, action: String): Long = {
    new LastKnownMetricsQuery(database, BackendMetricsDesign, BackendMetricsView, action).value()
  }

}
