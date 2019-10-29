package kpn.server.repository

import akka.util.Timeout
import kpn.core.app.stats.Figure

trait OverviewRepository {

  def figures(timeout: Timeout, stale: Boolean = true): Map[String, Figure]

}
