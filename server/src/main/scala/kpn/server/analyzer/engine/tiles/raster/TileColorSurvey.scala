package kpn.server.analyzer.engine.tiles.raster

import java.awt.Color

import kpn.api.common.SurveyDateInfo
import kpn.api.custom.Day
import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute
import kpn.server.analyzer.engine.tiles.domain.TileRouteSegment

class TileColorSurvey(dateInfo: SurveyDateInfo) extends TileColor {

  override def routeColor(route: TileDataRoute, segment: TileRouteSegment): Color = {
    surveyColor(route.surveyDate)
  }

  override def nodeColor(node: TileDataNode): Color = {
    surveyColor(node.surveyDate)
  }

  private def surveyColor(surveyDayOption: Option[Day]): Color = {
    surveyDayOption match {
      case None => TileColor.gray
      case Some(surveyDay) =>
        if (!surveyDay.isBefore(dateInfo.lastMonthStart)) {
          TileColor.lightGreen;
        } else if (!surveyDay.isBefore(dateInfo.lastHalfYearStart)) {
          TileColor.green;
        } else if (!surveyDay.isBefore(dateInfo.lastYearStart)) {
          TileColor.darkGreen;
        } else if (!surveyDay.isBefore(dateInfo.lastTwoYearsStart)) {
          TileColor.veryDarkGreen;
        } else {
          TileColor.darkRed;
        }
    }
  }
}
