package kpn.server.repository

import kpn.core.app.stats.Figure

trait OverviewRepository {

  def figures(stale: Boolean = true): Map[String, Figure]

}
