package kpn.server.analyzer.analyzer

import kpn.server.analyzer.engine.AnalysisTimeRepository
import kpn.server.analyzer.engine.FullAnalysisTrigger
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.Matchers

class FullAnalysisTriggerTest extends FunSuite with Matchers with MockFactory {

  // TODO these tests are ignored for now because they have a UTC versus local time problem

  ignore("trigger is fired during first time execution (no previous analysis time known)") {
    assertFullAnalysis(None, "2015-08-11 07:00")
  }

  ignore("trigger not fired in period following previous analysis") {
    assertNoFullAnalysis(Some("2015-08-11 06:00"), "2015-08-11 07:00")
  }

  ignore("trigger is fired after period following previous analysis has finished") {
    assertFullAnalysis(Some("2015-08-11 06:00"), "2015-08-11 08:05")
  }

  ignore("trigger is fired after period following previous analysis has just finished") {
    assertFullAnalysis(Some("2015-08-11 06:00"), "2015-08-11 08:00")
  }

  ignore("this work ok before midnight") {
    assertNoFullAnalysis(Some("2015-08-11 22:03"), "2015-08-11 23:55")
  }

  ignore("this work ok after midnight") {
    assertFullAnalysis(Some("2015-08-11 22:03"), "2015-08-12 00:05")
  }

  ignore("this work ok at midnight") {
    assertFullAnalysis(Some("2015-08-11 22:03"), "2015-08-12 00:00")
  }

  private def assertFullAnalysis(previousAnalysisTime: Option[String], now: String): Unit = {
    withTimestamp(now) {
      val analysisTime = stub[AnalysisTimeRepository]
      (analysisTime.get _).when().once().returns(previousAnalysisTime)
      new FullAnalysisTrigger(analysisTime).shouldPerformFullAnalysis should equal(true)
      (analysisTime.put _).verify(now)
      ()
    }
  }

  private def assertNoFullAnalysis(previousAnalysisTime: Option[String], now: String): Unit = {
    withTimestamp(now) {
      val analysisTime = stub[AnalysisTimeRepository]
      (analysisTime.get _).when().returns(previousAnalysisTime)
      new FullAnalysisTrigger(analysisTime).shouldPerformFullAnalysis should equal(false)
      (analysisTime.put _).verify(*).never()
      ()
    }
  }

  private def withTimestamp(timestamp: String)(fn: => Unit): Unit = {
    ??? // Timestamp.set(Timestamp.parseYyyymmddhhmm(timestamp))
    fn
    ??? // Timestamp.clear()
  }
}
