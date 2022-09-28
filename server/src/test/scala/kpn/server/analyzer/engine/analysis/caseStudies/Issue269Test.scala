package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.NetworkType
import kpn.database.util.Mongo
import kpn.server.api.planner.leg.LegBuilderImpl
import kpn.server.repository.GraphRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

object Issue269Test {

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      val routeRepository = new RouteRepositoryImpl(database)
      val graphRepository = new GraphRepositoryImpl(database, graphLoadEnabled = true)
      graphRepository.loadGraphs()
      val legBuilder = new LegBuilderImpl(graphRepository, routeRepository)
      val planString = "lrsyl6-49cwgb-49cwg4-49cwfq-hupkpb-lvmn1z-49cvvz-lvmmn6-lvmmgh-1cchpa3-26odhbe-ls6vbh-48fwv.2t-26odhbl-xpdr2o-48gdh.1-xpdr2i-16jquvq-lnauuz-e4yyes-e4yyde-e4yyd5-1u3s79p-e4yyb3-lnaveg-lxndsj-6echj6-lsesk8-49gfy6-o7xwz2-lrsyl6"
      legBuilder.plan(NetworkType.hiking, planString, proposed = false)
    }
  }
}
