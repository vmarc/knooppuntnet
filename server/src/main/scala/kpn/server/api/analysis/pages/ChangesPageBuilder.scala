package kpn.server.api.analysis.pages

import kpn.api.common.ChangesPage
import kpn.api.common.changes.filter.ChangesParameters

trait ChangesPageBuilder {
  def build(user: Option[String], parameters: ChangesParameters): ChangesPage
}
