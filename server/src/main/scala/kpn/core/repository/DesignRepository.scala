package kpn.core.repository

import kpn.core.db.views.Design

trait DesignRepository {
  def save(design: Design): Unit
}
