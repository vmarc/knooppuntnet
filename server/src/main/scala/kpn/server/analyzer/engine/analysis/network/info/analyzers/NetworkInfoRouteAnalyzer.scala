package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.core.mongo.Database
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkRouteDetail
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.springframework.stereotype.Component

@Component
class NetworkInfoRouteAnalyzer(database: Database) extends NetworkInfoAnalyzer {

  private val log = Log(classOf[NetworkInfoRouteAnalyzer])

  override def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    val routeIds = context.networkDoc.routeRefs.map(_.routeId)
    val routeDetails = queryRouteDetails(routeIds)

    val meters = routeDetails.map(_.length).sum
    val km = Math.round(meters.toDouble / 1000)


    context.copy(
      routeDetails = routeDetails,
      meters = meters,
      km = km,
    )
  }

  private def queryRouteDetails(routeIds: Seq[Long]): Seq[NetworkRouteDetail] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            in("_id", routeIds: _*)
          )
        ),
        project(
          fields(
            computed("id", "$_id"),
            computed("name", "$summary.name"),
            computed("length", "$summary.meters"),
            include("facts"),
            include("proposed"),
            include("lastUpdated"),
            include("lastSurvey"),
            include("tags"),
            include("nodeRefs")
          )
        )
      )
      val routeDetails = database.routes.aggregate[NetworkRouteDetail](pipeline, log)
      (s"routeDetails: ${routeDetails.size}", routeDetails)
    }
  }

}
