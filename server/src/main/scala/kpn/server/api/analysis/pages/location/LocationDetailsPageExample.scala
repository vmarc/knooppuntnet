package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationDetailsPage
import kpn.api.common.location.LocationSummary
import kpn.api.custom.Tags

object LocationDetailsPageExample {

  def page: LocationDetailsPage = {
    LocationDetailsPage(
      LocationSummary(10, 20, 30, 40),
      0,
      0,
      Seq.empty,
      Tags.empty
    )
  }
}
