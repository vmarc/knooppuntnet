package kpn.server.repository

import kpn.core.database.views.common.Design

trait DesignRepository {
  def save(design: Design): Unit
}
