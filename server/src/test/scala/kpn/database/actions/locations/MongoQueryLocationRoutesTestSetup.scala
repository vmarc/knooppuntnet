package kpn.database.actions.locations

import kpn.api.common.RouteType
import kpn.api.common.SurveyDateInfo
import kpn.api.custom.Day
import kpn.core.doc.Label
import kpn.core.test.TestObjects.newRouteBaseData
import kpn.core.test.TestObjects.newRouteDoc
import kpn.database.base.Database
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder

import java.time.ZoneId
import java.time.ZonedDateTime

class MongoQueryLocationRoutesTestSetup(database: Database) {

  def surveyDateInfo: SurveyDateInfo = {
    val local = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Brussels"))
    SurveyDateInfoBuilder.dateInfoAt(local)
  }

  def buildSurveyRoute(routeId: Long, lastSurvey: Option[Day]): Unit = {
    database.routes.save(
      newRouteDoc(
        routeId,
        base = newRouteBaseData(
          lastSurvey = lastSurvey
        ),
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location("be"),
          Label.survey
        )
      )
    )
  }

  def buildProposedRoute(routeId: Long, proposed: Boolean): Unit = {
    database.routes.save(
      newRouteDoc(
        routeId,
        base = newRouteBaseData(
          proposed = proposed
        ),
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location("be")
        )
      )
    )
  }
}
