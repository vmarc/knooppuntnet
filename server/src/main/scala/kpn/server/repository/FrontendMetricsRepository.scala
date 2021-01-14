package kpn.server.repository

import kpn.api.common.status.NameValue
import kpn.api.common.status.PeriodParameters
import kpn.core.action.ApiAction
import kpn.core.action.LogAction

trait FrontendMetricsRepository {

  def saveApiAction(action: ApiAction): Unit

  def saveLogAction(action: LogAction): Unit

  def query(parameters: PeriodParameters, action: String, average: Boolean, stale: Boolean = true): Seq[NameValue]

}
