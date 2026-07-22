package kpn.server.analyzer.engine.monitor.analysis

import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.core.util.CoordinateUtil
import kpn.core.util.Haversine
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteDeviationAnalysis
import org.locationtech.jts.geom.LineString
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web", "analysis"))
class MonitorRouteDeviationAnalyzer {

  def analyze(routeLines: Seq[LineString], referenceLines: Seq[LineString]): MonitorRouteDeviationAnalysis = {

    val referenceAnalysis = MonitorAnalyzer.analyze(referenceLines, routeLines)

    val matchesLines = referenceAnalysis.matchesLines
    val deviationLines = referenceAnalysis.deviationLines

    buildResult(
      referenceLines,
      matchesLines,
      deviationLines,
    )
  }

  private def buildResult(
    referenceLines: Seq[LineString],
    matchesLines: Seq[LineString],
    deviationLines: Seq[DistanceLineString],
  ): MonitorRouteDeviationAnalysis = {

    val referenceDistance = Haversine.meters(referenceLines)
    val matchesDistance = Haversine.meters(matchesLines)
    val matchesLineStrings = matchesLines.map(CoordinateUtil.lineStringToCoordinates)
    val deviations = buildDeviations(deviationLines)

    MonitorRouteDeviationAnalysis(
      referenceDistance,
      matchesDistance,
      matchesLineStrings,
      deviations,
    )
  }

  private def buildDeviations(deviationLines: Seq[DistanceLineString]): Seq[MonitorRouteDeviation] = {
    deviationLines.sortBy(_.distance).reverse.zipWithIndex.map { case (deviation, index) =>
      val line = CoordinateUtil.lineStringToCoordinates(deviation.line)
      val meters = Math.round(Haversine.meters(deviation.line))
      val bounds = MonitorRouteAnalysisSupport.toBounds(deviation.line.getCoordinates.toSeq)
      MonitorRouteDeviation(
        id = index + 1,
        meters,
        deviation.distance,
        bounds,
        Seq(line)
      )
    }
  }
}
