package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryRelation

object ReadOverpassRelation {

  def main(args: Array[String]): Unit = {
    val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
    println(overpassQueryExecutor.executeQuery(None, QueryRelation(11527464L)))
  }
}
