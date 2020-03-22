package kpn.server.repository

import kpn.core.action.ApiAction

trait FrontendActionsRepository {

  def saveApiAction(action: ApiAction): Unit

}
