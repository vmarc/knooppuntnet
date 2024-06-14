package kpn.database.actions.locations

import kpn.api.common.SharedTestObjects
import kpn.api.common.SurveyDateInfo
import kpn.api.custom.Day
import kpn.database.base.Database
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder

import java.time.ZoneId
import java.time.ZonedDateTime

class MongoQueryLocationRoutesTestSetup(database: Database) extends SharedTestObjects {

  def surveyDateInfo: SurveyDateInfo = {
    val local = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Brussels"))
    SurveyDateInfoBuilder.dateInfoAt(local)
  }

  def buildSurveyRoute(routeId: Long, lastSurvey: Option[Day]): Unit = {
    database.routes.save(
      newRouteDoc(
        newRouteSummary(routeId),
        labels = Seq(
          "active",
          "network-type-hiking",
          "location-be",
          "survey",
        ),
        lastSurvey = lastSurvey
      )
    )
  }

  def buildProposedRoute(routeId: Long, proposed: Boolean): Unit = {
    database.routes.save(
      newRouteDoc(
        newRouteSummary(routeId),
        labels = Seq(
          "active",
          "network-type-hiking",
          "location-be",
        ),
        proposed = proposed
      )
    )
  }
}
