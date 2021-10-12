package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.custom.Fact
import kpn.core.doc.Label
import kpn.core.doc.NetworkInfoRouteDetail
import kpn.core.util.Log
import kpn.core.util.NaturalSorting
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.springframework.stereotype.Component

@Component
class NetworkInfoRouteAnalyzer(database: Database) extends NetworkInfoAnalyzer {

  private val log = Log(classOf[NetworkInfoRouteAnalyzer])

  override def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    val routeIds = context.networkDoc.relationMembers.map(_.relationId)
    val routeDetails = queryRouteDetails(routeIds)
    val meters = routeDetails.map(_.length).sum
    val km = Math.round(meters.toDouble / 1000)
    val enrichedRouteDetails = routeDetails.map { networkRouteDetail =>
      val role = context.networkDoc.relationMembers.find(_.relationId == networkRouteDetail.id).flatMap(_.role) match {
        case Some(role) => Some(role)
        case None => None
      }
      val investigate = networkRouteDetail.facts.contains(Fact.RouteBroken)
      val accessible = !networkRouteDetail.facts.contains(Fact.RouteUnaccessible)
      val roleConnection = role.contains("connection")
      networkRouteDetail.copy(
        role = role,
        investigate = investigate,
        accessible = accessible,
        roleConnection = roleConnection,
      )
    }
    val sortedRouteDetails = NaturalSorting.sortBy(enrichedRouteDetails)(_.name)
    context.copy(
      routeDetails = sortedRouteDetails,
      meters = meters,
      km = km,
    )
  }

  private def queryRouteDetails(routeIds: Seq[Long]): Seq[NetworkInfoRouteDetail] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("labels", Label.active),
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
      val routeDetails = database.routes.aggregate[NetworkInfoRouteDetail](pipeline, log)
      (s"routeDetails: ${routeDetails.size}", routeDetails)
    }
  }
}
