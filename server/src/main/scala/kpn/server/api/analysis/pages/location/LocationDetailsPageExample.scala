package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationDetailsPage
import kpn.api.common.location.LocationSummary

object LocationDetailsPageExample {

  def page: LocationDetailsPage = {
    LocationDetailsPage(
      LocationSummary(10, 20, 30, 40),
      Seq.empty
    )
  }
}
