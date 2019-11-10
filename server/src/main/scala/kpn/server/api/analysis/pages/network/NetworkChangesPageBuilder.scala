package kpn.server.api.analysis.pages.network

import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.network.NetworkChangesPage

trait NetworkChangesPageBuilder {
  def build(user: Option[String], parameters: ChangesParameters): Option[NetworkChangesPage]
}
