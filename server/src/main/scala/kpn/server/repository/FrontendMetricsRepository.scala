package kpn.server.repository

import kpn.core.action.ApiAction

trait FrontendMetricsRepository {

  def saveApiAction(action: ApiAction): Unit

}
