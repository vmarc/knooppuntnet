package kpn.core.repository

import akka.util.Timeout
import kpn.core.app.stats.Figure

trait OverviewRepository {

  def figures(timeout: Timeout, stale: Boolean = true): Map[String, Figure]

  def figure(timeout: Timeout, factName: String, stale: Boolean = true): Option[Figure]
}
