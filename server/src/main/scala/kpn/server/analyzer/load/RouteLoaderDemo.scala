package kpn.server.analyzer.load

import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.util.Log
import kpn.server.analyzer.engine.AnalysisContext
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.shared.Timestamp

object RouteLoaderDemo {

  def main(args: Array[String]): Unit = {
    new RouteLoaderDemo().run()
  }
}

class RouteLoaderDemo() {

  val log = Log(classOf[RouteLoaderDemo])

  val analysisContext = new AnalysisContext()
  val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
  val countryAnalyzer = new CountryAnalyzerImpl(relationAnalyzer)
  val executor = new OverpassQueryExecutorImpl()
  val routeLoader = new RouteLoaderImpl(executor, countryAnalyzer)

  def run(): Unit = {
    log.unitElapsed {
      routeLoader.loadRoute(Timestamp(2018, 5, 17, 3, 19, 24), 8305522L) match {
        case Some(route) =>
          println(route.name)
        case _ =>
          println("could not load route")
      }
      "routeLoader"
    }
  }
}
