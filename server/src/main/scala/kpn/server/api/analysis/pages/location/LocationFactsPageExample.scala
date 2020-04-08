package kpn.server.api.analysis.pages.location

import kpn.api.common.common.Ref
import kpn.api.common.location.LocationFact
import kpn.api.common.location.LocationFactsPage
import kpn.api.common.location.LocationSummary
import kpn.api.custom.Fact

object LocationFactsPageExample {

  def page: LocationFactsPage = {
    LocationFactsPage(
      LocationSummary(10, 20, 30, 40),
      Seq(
        LocationFact("route", Fact.RouteNotForward, Seq(Ref(101, "01-02"), Ref(102, "02-03"))),
        LocationFact("route", Fact.RouteNotBackward, Seq(Ref(101, "01-02"), Ref(102, "02-03"))),
        LocationFact("route", Fact.RouteFixmetodo, Seq(Ref(101, "01-02")))
      )
    )
  }
}
