package kpn.server.repository

import kpn.api.custom.Timestamp

trait AnalysisRepository {

  /*
    Returns the time of the most recent minute diff that was processed by the analyzer. This provides
    an indication of how up-to-date the information in the analysis database is.
   */
  def lastUpdated(): Option[Timestamp]

  def saveLastUpdated(timestamp: Timestamp): Unit

}
