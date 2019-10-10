package kpn.core.tools.analyzer

import kpn.core.common.Time

object FullAnalysisTrigger {

  def shouldPerformFullAnalysis: Boolean = {
    val filename = "analysis-time"
    new FullAnalysisTrigger(new AnalysisTimeRepositoryImpl(filename)).shouldPerformFullAnalysis
  }

  private val fullAnalysisTimes = Seq(
    "00:00",
    "02:00",
    "04:00",
    "06:00",
    "08:00",
    "10:00",
    "12:00",
    "14:00",
    "16:00",
    "18:00",
    "20:00",
    "22:00"
  )
}

class FullAnalysisTrigger(analysisTime: AnalysisTimeRepository) {

  import kpn.core.tools.analyzer.FullAnalysisTrigger._

  def shouldPerformFullAnalysis: Boolean = {
    val now = Time.now.yyyymmddhhmm
    val today = Time.now.yyyymmdd
    analysisTime.get match {
      case Some(previous) =>
        val expectedPrevious = fullAnalysisTimes.reverse.map(hhmm => today + " " + hhmm).find { time => now >= time }.get
        if (previous >= expectedPrevious) {
          // analysis already done
          false
        }
        else {
          analysisTime.put(now)
          true
        }

      case None =>
        analysisTime.put(now)
        true
    }
  }
}
