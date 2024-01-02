package kpn.database.base

import kpn.api.common.common.UserSession

trait SessionDatabase {

  def sessions: DatabaseCollection[UserSession]

}
