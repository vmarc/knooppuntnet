package kpn.database.actions.locations

import kpn.api.common.SurveyDateInfo
import kpn.core.test.SharedTestObjects
import kpn.database.base.Database
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder

import java.time.ZoneId
import java.time.ZonedDateTime

class MongoQueryLocationNodesTestSetup(database: Database) extends SharedTestObjects {

  def surveyDateInfo: SurveyDateInfo = {
    val local = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Brussels"))
    SurveyDateInfoBuilder.dateInfoAt(local)
  }
}
